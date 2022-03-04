//
//  ResultDataTests.swift
//
//  Copyright © 2020-2022 Sage Bionetworks. All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification,
// are permitted provided that the following conditions are met:
//
// 1.  Redistributions of source code must retain the above copyright notice, this
// list of conditions and the following disclaimer.
//
// 2.  Redistributions in binary form must reproduce the above copyright notice,
// this list of conditions and the following disclaimer in the documentation and/or
// other materials provided with the distribution.
//
// 3.  Neither the name of the copyright holder(s) nor the names of any contributors
// may be used to endorse or promote products derived from this software without
// specific prior written permission. No license is granted to the trademarks of
// the copyright holders even if such marks are included in this software.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
// CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
// OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
//

import XCTest
@testable import JsonModel
@testable import AssessmentModel

class ResultDataTests: XCTestCase {
    
    let decoder: JSONDecoder = AssessmentFactory().createJSONDecoder()

    let encoder: JSONEncoder = AssessmentFactory().createJSONEncoder()

    override func setUp() {
        // Put setup code here. This method is called before the invocation of each test method in the class.
        
        // Use a statically defined timezone.
        ISO8601TimestampFormatter.timeZone = TimeZone(secondsFromGMT: Int(-2.5 * 60 * 60))
    }

    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }

    func testCreateJsonSchemaDocumentation() {
        let factory = AssessmentFactory()
        let doc = JsonDocumentBuilder(factory: factory)
        
        do {
            let schemas = try doc.buildSchemas()

            XCTAssertEqual(schemas.count, 3)
            
            guard let resultDataSchema = schemas.first(where: { $0.id.className == "ResultData" })
            else {
                XCTFail("Failed to build the expected JSON schema for `ResultData`.")
                return
            }
            
            let resultDataJson = try factory.createJSONEncoder().encode(resultDataSchema)
            let resultDataJsonString = String(data: resultDataJson, encoding: .utf8)!
            print(resultDataJsonString)
            
            if let properties = resultDataSchema.root.orderedProperties?.orderedDictionary {
                XCTAssertEqual(0, properties.keys.first(where: { $0.stringValue == "type" })?.sortOrderIndex)
                XCTAssertEqual(1000, properties.keys.first(where: { $0.stringValue == "identifier" })?.sortOrderIndex)
                XCTAssertEqual(2000, properties.keys.first(where: { $0.stringValue == "startDate" })?.sortOrderIndex)
                XCTAssertEqual(2001, properties.keys.first(where: { $0.stringValue == "endDate" })?.sortOrderIndex)
            }
            else {
                XCTFail("Failed to build the expected properties for `ResultData`.")
            }
            XCTAssertEqual(["type","identifier","startDate"], resultDataSchema.root.required)
            XCTAssertNil(resultDataSchema.root.allOf)
            
            let sharedKeys = ["identifier", "startDate", "endDate"]
            let expectedSerializableType = "SerializableResultType"
            
            let expectedClassAndType = [
                ("AnswerResultObject","answer"),
                ("CollectionResultObject","collection"),
                ("FileResultObject","file"),
                ("ErrorResultObject","error"),
            ]
            expectedClassAndType.forEach {
                guard let def = checkDefinitions(on: resultDataSchema,
                                                 className: $0.0,
                                                 expectedType: $0.1,
                                                 sharedKeys: sharedKeys,
                                                 expectedSerializableType: expectedSerializableType)
                else {
                    XCTFail("Unexpected nil for \($0.0)")
                    return
                }
                // Check that the circle reference to the parent is set properly.
                if def.className == "CollectionResultObject" {
                   if let childrenProp = def.properties?["children"],
                       case .array(let arrayProp) = childrenProp,
                       case .reference(let objRef) = arrayProp.items {
                        XCTAssertEqual("#", objRef.ref)
                    }
                    else {
                        XCTFail("Failed to find the children property: \(String(describing: def.properties)) ")
                    }
                }
            }
            
            guard let answerTypeSchema = schemas.first(where: { $0.id.className == "AnswerType" })
            else {
                XCTFail("Failed to build the expected JSON schema for `AnswerType`.")
                return
            }
            
            let expectedAnswerTypeClassAndType = [
                ("AnswerTypeMeasurement","measurement"),
                ("AnswerTypeDateTime","date-time"),
                ("AnswerTypeArray","array"),
                ("AnswerTypeBoolean","boolean"),
                ("AnswerTypeInteger","integer"),
                ("AnswerTypeNumber","number"),
                ("AnswerTypeObject","object"),
                ("AnswerTypeString","string"),
            ]
            expectedAnswerTypeClassAndType.forEach {
                guard let _ = checkDefinitions(on: answerTypeSchema,
                                               className: $0.0,
                                               expectedType: $0.1,
                                               sharedKeys: [],
                                               expectedSerializableType: "AnswerTypeType",
                                               expectedSerializableTypeSortIndex: nil)
                else {
                    XCTFail("Unexpected nil for \($0.0)")
                    return
                }
            }
        }
        catch let err {
            XCTFail("Failed to build the JsonSchema: \(err)")
        }
    }
    
    @discardableResult
    func checkDefinitions(on rootSchema: JsonSchema,
                          className: String,
                          expectedType: String,
                          sharedKeys: [String],
                          expectedSerializableType: String,
                          expectedSerializableTypeSortIndex: Int? = 0) -> JsonSchemaObject? {
        guard let definition = rootSchema.definitions?[className],
               case .object(let schema) = definition
        else {
            XCTFail("Failed to add `\(className)` to the `\(rootSchema.id.className)` definitions.")
            return nil
        }
                
        XCTAssertEqual(.init(className), schema.id)
        // Each class that implements the parent interface should have a reference to "#" as something it conforms to.
        XCTAssertEqual(["#"], schema.allOf?.map { $0.ref })
        
        if let defProperties = schema.orderedProperties?.orderedDictionary {

            // identifier, startDate, endDate should use the property definitions on the interface
            sharedKeys.forEach { sharedKey in
                XCTAssertNil(defProperties.first(where: { $0.key.stringValue == sharedKey }), "\(className) has \(sharedKey)")
            }
            
            if let serializationType = defProperties.first(where: { $0.key.stringValue == "type" }) {
                if let expectedIndex = expectedSerializableTypeSortIndex {
                    XCTAssertEqual(expectedIndex, serializationType.key.sortOrderIndex)
                }
                XCTAssertEqual(
                    .const(.init(const: expectedType, ref: .init(expectedSerializableType), description: nil)),
                    serializationType.value,
                    "\(className) does not match expected type."
                )
            }
            else {
                XCTFail("\(className) does not have required 'type' key.")
            }
            
            defProperties.values.forEach { prop in
                switch prop {
                case .primitive(let defProp):
                    XCTAssertNotNil(defProp.description, "\(className) property \(prop) has a nil description")
                case .array(let defProp):
                    XCTAssertNotNil(defProp.description, "\(className) property \(prop) has a nil description")
                case .dictionary(let defProp):
                    XCTAssertNotNil(defProp.description, "\(className) property \(prop) has a nil description")
                case .reference(let defProp):
                    XCTAssertNotNil(defProp.description, "\(className) property \(prop) has a nil description")
                default:
                    break
                }
            }
        }
        else {
            XCTFail("Failed to build the expected properties for `ResultData`.")
        }
        
        return schema
    }
    
    func testCollectionResultObject_Codable() {
        let collectionResult = CollectionResultObject(identifier: "foo")
        let answerResult1 = AnswerResultObject(identifier: "input1", value: .boolean(true))
        let answerResult2 = AnswerResultObject(identifier: "input2", value: .integer(42))
        collectionResult.children = [answerResult1, answerResult2]
        
        XCTAssertNil(collectionResult.endDateTime)
        XCTAssertNil(answerResult1.endDateTime)
        XCTAssertNil(answerResult2.endDateTime)
        
        do {
            let jsonData = try encoder.encode(collectionResult)
            guard let dictionary = try JSONSerialization.jsonObject(with: jsonData, options: []) as? [String : Any]
                else {
                    XCTFail("Encoded object is not a dictionary")
                    return
            }
            
            XCTAssertEqual(dictionary["identifier"] as? String, "foo")
            XCTAssertNotNil(dictionary["startDate"])
            XCTAssertNil(dictionary["endDate"])
            if let results = dictionary["children"] as? [[String : Any]] {
                XCTAssertEqual(results.count, 2)
                if let result1 = results.first {
                    XCTAssertEqual(result1["identifier"] as? String, "input1")
                    XCTAssertNil(result1["endDate"])
                }
            } else {
                XCTFail("Failed to encode the input results.")
            }
            
            let object = try decoder.decode(CollectionResultObject.self, from: jsonData)
            
            XCTAssertEqual(object.identifier, collectionResult.identifier)
            XCTAssertEqual(object.startDate.timeIntervalSinceNow, collectionResult.startDate.timeIntervalSinceNow, accuracy: 1)
            XCTAssertNil(object.endDateTime)
            XCTAssertEqual(object.children.count, 2)
            
            if let result1 = object.children.first as? AnswerResultObject {
                XCTAssertEqual(result1.identifier, answerResult1.identifier)
                let expected = AnswerTypeBoolean()
                XCTAssertEqual(expected, answerResult1.jsonAnswerType as? AnswerTypeBoolean)
                XCTAssertEqual(result1.startDate.timeIntervalSinceNow, answerResult1.startDate.timeIntervalSinceNow, accuracy: 1)
                XCTAssertNil(result1.endDateTime)
                XCTAssertEqual(result1.jsonValue, answerResult1.jsonValue)
            } else {
                XCTFail("\(object.children) did not decode the results as expected")
            }
            
        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
        }
    }
    
    func testFileResultObject_Init() {
        let fileResult = FileResultObject(identifier: "fileResult", url: URL(string: "file://temp/foo.json")!, contentType: "application/json", startUptime: 1234.567)
        XCTAssertEqual("/foo.json", fileResult.relativePath)
    }
    
    func testFileResultObject_Codable() {
        let json = """
        {
            "identifier": "foo",
            "type": "file",
            "startDate": "2017-10-16T22:28:09.000-02:30",
            "endDate": "2017-10-16T22:30:09.000-02:30",
            "relativePath": "temp.json",
            "contentType": "application/json"
        }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        do {
            
            let object = try decoder.decode(FileResultObject.self, from: json)
            
            XCTAssertEqual(object.identifier, "foo")
            XCTAssertEqual(object.serializableType, "file")
            XCTAssertGreaterThan(object.endDate, object.startDate)
            XCTAssertEqual(object.relativePath, "temp.json")
            XCTAssertEqual(object.contentType, "application/json")
            
            let jsonData = try encoder.encode(object)
            guard let dictionary = try JSONSerialization.jsonObject(with: jsonData, options: []) as? [String : Any]
                else {
                    XCTFail("Encoded object is not a dictionary")
                    return
            }
            
            XCTAssertEqual(dictionary["identifier"] as? String, "foo")
            XCTAssertEqual(dictionary["type"] as? String, "file")
            XCTAssertEqual(dictionary["startDate"] as? String, "2017-10-16T22:28:09.000-02:30")
            XCTAssertEqual(dictionary["endDate"] as? String, "2017-10-16T22:30:09.000-02:30")
            XCTAssertEqual(dictionary["relativePath"] as? String, "temp.json")
            XCTAssertEqual(dictionary["contentType"] as? String, "application/json")
            XCTAssertNil(dictionary["url"])
            
        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
        }
    }
    
    func testFileResultObject_Copy() {
        let result = FileResultObject(identifier: "fileResult", url: URL(string: "file://temp/foo.json")!, contentType: "application/json", startUptime: 1234.567)
        var copy = result.deepCopy()
        XCTAssertEqual(result, copy)
        copy.startDate = Date()
        XCTAssertNotEqual(result, copy)
    }
    
    func testErrorResultObject_Copy() {
        let result = ErrorResultObject(identifier: "foo", description: "It broke", domain: "com.fixme.foo", code: 42)
        var copy = result.deepCopy()
        XCTAssertEqual(result, copy)
        copy.startDate = Date()
        XCTAssertNotEqual(result, copy)
    }
    
    func testAnswerResultObject_Copy() {
        let result = AnswerResultObject(identifier: "foo",
                                        answerType: AnswerTypeMeasurement(unit: "cm"),
                                        value: .number(42),
                                        questionText: "What is your favorite color?",
                                        questionData: .boolean(true))
        let copy = result.deepCopy()
        
        XCTAssertFalse(result === copy)
        XCTAssertEqual(result.identifier, copy.identifier)
        XCTAssertEqual(result.startDate, copy.startDate)
        XCTAssertEqual(result.endDate, copy.endDate)
        XCTAssertEqual(result.jsonValue, copy.jsonValue)
        XCTAssertEqual(result.questionText, copy.questionText)
        XCTAssertEqual(result.questionData, copy.questionData)
        XCTAssertEqual("cm", (copy.jsonAnswerType as? AnswerTypeMeasurement)?.unit)
    }
    
    func testSerializers() {
        let factory = AssessmentFactory()
        
        XCTAssertTrue(checkPolymorphicExamples(for: factory.resultSerializer.examples,
                                                using: factory, protocolType: ResultData.self))
        XCTAssertTrue(checkPolymorphicExamples(for: factory.answerTypeSerializer.examples,
                                                using: factory, protocolType: AnswerType.self))

    }
    
    func checkPolymorphicExamples<ProtocolType>(for objects: [ProtocolType], using factory: SerializationFactory, protocolType: ProtocolType.Type) -> Bool {
        var success = true
        objects.forEach {
            guard let original = $0 as? DocumentableObject else {
                XCTFail("Object does not conform to DocumentableObject. \($0)")
                success = false
                return
            }

            do {
                let decoder = factory.createJSONDecoder()
                let examples = try type(of: original).jsonExamples()
                examples.forEach { example in
                    do {
                        // Check that the example can be decoded without errors.
                        let wrapper = example.jsonObject()
                        let encodedObject = try JSONSerialization.data(withJSONObject: wrapper, options: [])
                        let decodingWrapper = try decoder.decode(_DecodablePolymorphicWrapper.self, from: encodedObject)
                        let decodedObject = try factory.decodePolymorphicObject(protocolType, from: decodingWrapper.decoder)
                        
                        // Check that the decoded object is the same Type as the original.
                        let originalType = type(of: original as Any)
                        let decodedType = type(of: decodedObject as Any)
                        let isSameType = (originalType == decodedType)
                        XCTAssertTrue(isSameType, "\(decodedType) is not equal to \(originalType)")
                        success = success && isSameType
                        
                        // Check that the decoded type name is the same as the original type name
                        guard let decodedTypeName = (decodedObject as? PolymorphicRepresentable)?.typeName
                            else {
                                XCTFail("Decoded object does not conform to PolymorphicRepresentable. \(decodedObject)")
                                return
                        }
                        guard let originalTypeName = (original as? PolymorphicRepresentable)?.typeName
                            else {
                                XCTFail("Example object does not conform to PolymorphicRepresentable. \(original)")
                                return
                        }
                        XCTAssertEqual(originalTypeName, decodedTypeName)
                        success = success && (originalTypeName == decodedTypeName)
                        
                    } catch let err {
                        XCTFail("Failed to decode \(example) for \(protocolType). \(err)")
                        success = false
                    }
                }
            }
            catch let err {
                XCTFail("Failed to decode \(original). \(err)")
                success = false
            }
        }
        return success
    }

    fileprivate struct _DecodablePolymorphicWrapper : Decodable {
        let decoder: Decoder
        init(from decoder: Decoder) throws {
            self.decoder = decoder
        }
    }
    
    func testCollectionResultExtensions() {
        
        let collection = CollectionResultObject(identifier: "test")
        let answers = ["a" : 3, "b": 5, "c" : 7]
        answers.forEach {
            let answerResult = AnswerResultObject(identifier: $0.key, value: .integer($0.value))
            collection.insert(answerResult)
        }

        let answerB = AnswerResultObject(identifier: "a", value: .integer(8))
        let previous = collection.insert(answerB)
        XCTAssertNotNil(previous)
        if let previousResult = previous as? AnswerResultObject {
            XCTAssertEqual(previousResult.jsonValue, .integer(3))
        }
        else {
            XCTFail("Failed to return the previous answer")
        }
        
        if let newResult = (collection as AnswerFinder).findAnswer(with: "a") {
            XCTAssertEqual(newResult.jsonValue, .integer(8))
        }
        else {
            XCTFail("Failed to find the new answer")
        }
        
        let removed = collection.remove(with: "b")
        XCTAssertNotNil(removed)
        if let removedResult = removed as? AnswerResultObject {
            XCTAssertEqual(removedResult.jsonValue, .integer(5))
        }
        else {
            XCTFail("Failed to remove the result")
        }
        
        let removedD = collection.remove(with: "d")
        XCTAssertNil(removedD)
    }
    
    func testResultObject_Codable() {
        let json = """
        {
            "identifier": "foo",
            "type": "base",
            "startDate": "2017-10-16T22:28:09.000-02:30",
            "endDate": "2017-10-16T22:30:09.000-02:30"
        }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        do {
            
            let object = try decoder.decode(ResultObject.self, from: json)
            
            XCTAssertEqual(object.identifier, "foo")
            XCTAssertEqual(object.serializableType, .StandardTypes.base.resultType)
            XCTAssertGreaterThan(object.endDate, object.startDate)
            
            let jsonData = try encoder.encode(object)
            guard let dictionary = try JSONSerialization.jsonObject(with: jsonData, options: []) as? [String : Any]
                else {
                    XCTFail("Encoded object is not a dictionary")
                    return
            }
            
            XCTAssertEqual(dictionary["identifier"] as? String, "foo")
            XCTAssertEqual(dictionary["type"] as? String, "base")
            XCTAssertEqual(dictionary["startDate"] as? String, "2017-10-16T22:28:09.000-02:30")
            XCTAssertEqual(dictionary["endDate"] as? String, "2017-10-16T22:30:09.000-02:30")

        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
        }
    }
    
    func testAssessmentResultObject_Codable() {
        let assessmentResult = AssessmentResultObject(identifier: "foo",
                                                      versionString: "3",
                                                      assessmentIdentifier: "foo 2")
        let answerResult1 = AnswerResultObject(identifier: "step1", value: .boolean(true))
        let answerResult2 = AnswerResultObject(identifier: "step2", value: .integer(42))
        assessmentResult.stepHistory = [answerResult1, answerResult2]
        
        do {
            let jsonData = try encoder.encode(assessmentResult)
            guard let dictionary = try JSONSerialization.jsonObject(with: jsonData, options: []) as? [String : Any]
                else {
                    XCTFail("Encoded object is not a dictionary")
                    return
            }
            
            XCTAssertEqual(dictionary["identifier"] as? String, "foo")
            XCTAssertNotNil(dictionary["startDate"])
            XCTAssertNil(dictionary["endDate"])

            if let results = dictionary["stepHistory"] as? [[String : Any]] {
                XCTAssertEqual(results.count, 2)
                if let result1 = results.first {
                    XCTAssertEqual(result1["identifier"] as? String, "step1")
                }
            }
            else {
                XCTFail("Failed to encode the step history.")
            }
            
            XCTAssertEqual(dictionary["assessmentIdentifier"] as? String, "foo 2")
            XCTAssertEqual(dictionary["versionString"] as? String, "3")
            
            let object = try decoder.decode(AssessmentResultObject.self, from: jsonData)
            
            XCTAssertEqual(object.identifier, assessmentResult.identifier)
            XCTAssertEqual(object.startDate.timeIntervalSinceNow, assessmentResult.startDate.timeIntervalSinceNow, accuracy: 1)
            XCTAssertNil(object.endDateTime)
            XCTAssertEqual(object.stepHistory.count, 2)
            
            if let result1 = object.stepHistory.first as? AnswerResultObject {
                XCTAssertEqual(result1.identifier, answerResult1.identifier)
                let expected = AnswerTypeBoolean()
                XCTAssertEqual(expected, answerResult1.jsonAnswerType as? AnswerTypeBoolean)
                XCTAssertEqual(result1.startDate.timeIntervalSinceNow, answerResult1.startDate.timeIntervalSinceNow, accuracy: 1)
                XCTAssertNil(result1.endDateTime)
                XCTAssertEqual(result1.jsonValue, answerResult1.jsonValue)
            } else {
                XCTFail("\(object.stepHistory) did not decode the results as expected")
            }
            
            XCTAssertEqual(object.versionString, "3")
            XCTAssertEqual(object.assessmentIdentifier, "foo 2")
            
        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
        }
    }

    func testBranchNodeResultObject_Codable() {
        let branchResult = BranchNodeResultObject(identifier: "foo")
        let answerResult1 = AnswerResultObject(identifier: "step1", value: .boolean(true))
        let answerResult2 = AnswerResultObject(identifier: "step2", value: .integer(42))
        branchResult.stepHistory = [answerResult1, answerResult2]
        
        do {
            let jsonData = try encoder.encode(branchResult)
            guard let dictionary = try JSONSerialization.jsonObject(with: jsonData, options: []) as? [String : Any]
                else {
                    XCTFail("Encoded object is not a dictionary")
                    return
            }
            
            XCTAssertEqual(dictionary["identifier"] as? String, "foo")
            XCTAssertNotNil(dictionary["startDate"])
            XCTAssertNil(dictionary["endDate"])

            if let results = dictionary["stepHistory"] as? [[String : Any]] {
                XCTAssertEqual(results.count, 2)
                if let result1 = results.first {
                    XCTAssertEqual(result1["identifier"] as? String, "step1")
                }
            }
            else {
                XCTFail("Failed to encode the step history.")
            }
            
            let object = try decoder.decode(BranchNodeResultObject.self, from: jsonData)
            
            XCTAssertEqual(object.identifier, branchResult.identifier)
            XCTAssertEqual(object.startDate.timeIntervalSinceNow, branchResult.startDate.timeIntervalSinceNow, accuracy: 1)
            XCTAssertNil(object.endDateTime)
            XCTAssertEqual(object.stepHistory.count, 2)
            
            if let result1 = object.stepHistory.first as? AnswerResultObject {
                XCTAssertEqual(result1.identifier, answerResult1.identifier)
                let expected = AnswerTypeBoolean()
                XCTAssertEqual(expected, answerResult1.jsonAnswerType as? AnswerTypeBoolean)
                XCTAssertEqual(result1.startDate.timeIntervalSinceNow, answerResult1.startDate.timeIntervalSinceNow, accuracy: 1)
                XCTAssertNil(result1.endDateTime)
                XCTAssertEqual(result1.jsonValue, answerResult1.jsonValue)
            } else {
                XCTFail("\(object.stepHistory) did not decode the results as expected")
            }
            
        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
        }
    }
    
    func testBranchNodeResultExtensions() {
        
        let branchResult = BranchNodeResultObject(identifier: "test")
        let answers = ["a" : 3, "b": 5, "c" : 7]
        answers.forEach {
            let answerResult = AnswerResultObject(identifier: $0.key, value: .integer($0.value))
            branchResult.stepHistory.append(answerResult)
        }
        branchResult.asyncResults = [
            AnswerResultObject(identifier: "async", value: .integer(8))
        ]

        if let result = (branchResult as AnswerFinder).findAnswer(with: "a") {
            XCTAssertEqual(result.jsonValue, .integer(3))
        }
        else {
            XCTFail("Failed to find the new answer")
        }
        
        if let result = (branchResult as AnswerFinder).findAnswer(with: "async") {
            XCTAssertEqual(result.jsonValue, .integer(8))
        }
        else {
            XCTFail("Failed to find the new answer")
        }
        
        branchResult.insert(AnswerResultObject(identifier: "async", value: .integer(9)))
        if let result = (branchResult as AnswerFinder).findAnswer(with: "async") {
            XCTAssertEqual(result.jsonValue, .integer(9))
        }
        else {
            XCTFail("Failed to find the new answer")
        }
    }
    
    func testResultSerializer() {
        // Check that an example of all the standard results are included in the serializer.
        let serializer = ResultDataSerializer()
        let actual = Set(serializer.examples.map { $0.typeName })
        let expected = Set(SerializableResultType.StandardTypes.allCases.map { $0.rawValue })
        XCTAssertEqual(expected.count, actual.count)
        XCTAssertEqual(expected, actual)
    }
    
    func testNilEndDate() {
        
        let results: [SerializableResultData] = [
            AnswerResultObject(identifier: "example", answerType: nil),
            AssessmentResultObject(identifier: "example"),
            ResultObject(identifier: "example"),
            CollectionResultObject(identifier: "example"),
            ErrorResultObject(identifier: "example", description: "description", domain: "domain", code: 1),
            FileResultObject(identifier: "example", url: URL(string: "example.json", relativeTo: URL(string: "file://example/"))!),
            BranchNodeResultObject(identifier: "example"),
        ]
        
        // Check that all standard results are being checked
        let expected = Set(SerializableResultType.StandardTypes.allCases.map { $0.rawValue })
        let actual = Set(results.map { $0.typeName })
        XCTAssertEqual(expected.count, actual.count)
        XCTAssertEqual(expected, actual)
        
        results.forEach { result in
            
            if let multiplatformResult = result as? MultiplatformResultData {
                XCTAssertNil(multiplatformResult.endDateTime)
            }
            else {
                XCTFail("\(result) does not conform to \(MultiplatformResultData.self)")
            }
            
            do {
                let jsonData = try result.jsonEncodedData()
                guard let dictionary = try JSONSerialization.jsonObject(with: jsonData, options: []) as? [String : Any]
                    else {
                        XCTFail("Encoded object is not a dictionary")
                        return
                }
                
                XCTAssertEqual("example", dictionary["identifier"] as? String)
                XCTAssertEqual(result.typeName, dictionary["type"] as? String)
                XCTAssertNotNil(dictionary["startDate"])
                XCTAssertNil(dictionary["endDate"])

                let wrapper = try decoder.decode(_DecodingWrapper<ResultData>.self, from: jsonData)
                let decodedResult = wrapper.value
                if let multiplatformResult = decodedResult as? MultiplatformResultData {
                    XCTAssertEqual("example", multiplatformResult.identifier, "\(result)")
                    XCTAssertEqual(result.typeName, multiplatformResult.typeName, "\(result)")
                    XCTAssertEqual(result.startDate.timeIntervalSinceReferenceDate, multiplatformResult.startDate.timeIntervalSinceReferenceDate, accuracy:1, "\(result)")
                    XCTAssertNil(multiplatformResult.endDateTime, "\(result)")
                }
                else {
                    XCTFail("\(decodedResult) does not conform to \(MultiplatformResultData.self)")
                }
                
            } catch let err {
                XCTFail("Failed to decode/encode object: \(err)")
            }
        }
    }
    
    fileprivate struct _DecodingWrapper<T> : Decodable {
        let value : T
        init(from decoder: Decoder) throws {
            self.value = try decoder.serializationFactory.decodePolymorphicObject(T.self, from: decoder)
        }
    }
}
