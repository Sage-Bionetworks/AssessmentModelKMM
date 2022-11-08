//
//  AssessmentModelTests.swift
//

// TODO: syoung 03/21/2022 Decide if these tests are useful to keep or refactor.

//import XCTest
//import AssessmentModel
//
//class AssessmentModelTests: XCTestCase {
//
//    func testAssessmentLoadedFromEmbeddedResource() {
//        do {
//            let loader = AssessmentJsonResourceLoader(resourceName: "sample_assessment", bundle: Bundle.module)
//            let assessment = try loader.decodeObject() as? AssessmentObject
//            XCTAssertNotNil(assessment)
//            checkBundlePointer(assessment)
//        } catch let err {
//            XCTFail("Failed to decode files: \(err)")
//            return
//        }
//    }
//
//    func testAssessmentLoadedFromJsonString() {
//        guard let url = Bundle.module.url(forResource: "sample_assessment", withExtension: "json") else {
//            XCTFail("Failed to find file")
//            return
//        }
//        do {
//            let jsonString = try String(contentsOf: url)
//            let loader = AssessmentJsonStringLoader(jsonString: jsonString, bundle: Bundle.module)
//            let assessment = try loader.decodeObject() as? AssessmentObject
//            XCTAssertNotNil(assessment)
//            checkBundlePointer(assessment)
//        } catch let err {
//            XCTFail("Failed to decode files: \(err)")
//            return
//        }
//    }
//
//    func testAssessmentLoadedFromRegistry() {
//        do {
//            let registry = AssessmentRegistryProviderIOS(modulesResourceName: "embedded_assessment_registry", bundle: Bundle.module)
//            let assessment = try registry.loadRegisteredAssessment(identifier: "sampleId", version: nil) as? AssessmentObject
//            XCTAssertNotNil(assessment)
//
//            let wrappedAssessment = try registry.loadRegisteredAssessment(identifier: "sampleWrapperId", version: nil) as? AssessmentObject
//            XCTAssertNotNil(wrappedAssessment)
//
//            guard let assessment2 = wrappedAssessment?.children.last as? AssessmentObject else {
//                XCTFail("\(String(describing: wrappedAssessment?.children)) are not expected type")
//                return
//            }
//
//            XCTAssertEqual(assessment?.schemaIdentifier, assessment2.schemaIdentifier)
//            XCTAssertEqual(assessment?.children.count, assessment2.children.count)
//
//            checkBundlePointer(assessment)
//            checkBundlePointer(assessment2)
//
//        } catch let err {
//            XCTFail("Failed to decode files: \(err)")
//            return
//        }
//    }
//
//    func checkBundlePointer(_ assessment: AssessmentObject?) {
//        guard let assessment = assessment else { return }
//        checkNode(node: assessment, prefix: "")
//        checkContainer(container: assessment)
//    }
//
//    func checkContainer(container: NodeContainer) {
//        container.children.forEach { node in
//            checkNode(node: node, prefix: "\(container.identifier).")
//            if let choices = (node as? ChoiceQuestion)?.choices {
//                choices.forEach { choice in
//                    checkResourceInfo(choice.icon, "\(container.identifier).\(node.identifier).choices[\(choice.icon?.imageName ?? "")]")
//                }
//            }
//            if let sub = node as? NodeContainer {
//                checkContainer(container: sub)
//            }
//        }
//    }
//
//    func checkNode(node: Node, prefix: String) {
//        checkResourceInfo((node as? ContentNode)?.imageInfo, "\(prefix)\(node.identifier).imageInfo")
//        node.buttonMap.forEach { button in
//            checkResourceInfo(button.value.imageInfo, "\(prefix)\(node.identifier).buttonMap[\(button.key)].imageInfo")
//            checkResourceInfo(button.value as? ResourceInfo,
//                "\(prefix)\(node.identifier).buttonMap[\(button.key)]")
//        }
//    }
//
//    func checkResourceInfo(_ resourceInfo: ResourceInfo?, _ message: String? = nil) {
//        guard let info = resourceInfo else { return }
//        XCTAssertNotNil(info.decoderBundle, message ?? "")
//    }
//
//    func testAnswerResult_Serialization() {
//        let answerType = AnswerType.INTEGER()
//        let result = answerType.createAnswerResult(identifier: "foo", value: 3)
//        let startDateString = DateUtils().bridgeIsoDateTimeString(instant: result.startDateTime)
//        do {
//            let dictionary = try result.jsonObject() as NSDictionary
//            let expectedJson: NSDictionary = [
//                            "identifier" : "foo",
//                            "type" : "answer",
//                            "answerType" : [
//                                "type": "integer"
//                            ],
//                            "value" : 3,
//                            "startDate" : startDateString,
//                            "endDate" : NSNull()
//                        ]
//            XCTAssertEqual(expectedJson, dictionary)
//        } catch let err {
//            XCTFail("Failed to encode: \(err)")
//        }
//    }
//
//    func testJsonObject_Serialization() {
//        let expectedJson: NSDictionary = [
//            "identifier" : "foo",
//            "type" : "answer",
//            "answerType" : [
//                "type": "integer"
//            ],
//            "value" : 3
//        ]
//        do {
//            let jsonData = try JSONSerialization.data(withJSONObject: expectedJson, options: [])
//            guard let jsonString = String(data: jsonData, encoding: .utf8) else {
//                XCTFail("Failed to convert data to UTF8 string")
//                return
//            }
//            let jsonObject = try JsonElementDecoder(jsonString: jsonString).decodeObject()
//            let encodedString = try JsonElementEncoder(jsonElement: jsonObject).encodeObject()
//            guard let outputData = encodedString.data(using: .utf8) else {
//                XCTFail("Failed to convert \(encodedString) to UTF8 data")
//                return
//            }
//            let outputJson = try JSONSerialization.jsonObject(with: outputData, options: [])
//            guard let outputDictionary = outputJson as? NSDictionary else {
//                XCTFail("Failed to encode object as a dictionary")
//                return
//            }
//            XCTAssertEqual(expectedJson, outputDictionary)
//        } catch let err {
//            XCTFail("Failed to encode: \(err)")
//        }
//
//    }
//}
//
//extension Result {
//    func jsonObject() throws -> [String : Any]  {
//        let encoding = try ResultEncoder(result: self).encodeObject()
//        let data = encoding.data(using: .utf8)
//        guard let jsonData = data else {
//            let context = EncodingError.Context(codingPath: [], debugDescription: "Failed to get a UTF8 encoded string")
//            throw EncodingError.invalidValue(self, context)
//        }
//        let json = try JSONSerialization.jsonObject(with: jsonData, options: [])
//        guard let dictionary = json as? [String : Any] else {
//            let context = EncodingError.Context(codingPath: [], debugDescription: "Failed to encode a dictionary for \(json)")
//            throw EncodingError.invalidValue(json, context)
//        }
//        return dictionary
//    }
//}

