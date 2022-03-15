//
//  AnswerResultTypeJSONTests.swift
//
//  Copyright © 2019-2022 Sage Bionetworks. All rights reserved.
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

class AnswerTypeTests: XCTestCase {
    
    let decoder: JSONDecoder = AssessmentFactory().createJSONDecoder()

    let encoder: JSONEncoder = AssessmentFactory().createJSONEncoder()
    
    override func setUp() {
        super.setUp()
        
        // Use a statically defined timezone.
        ISO8601TimestampFormatter.timeZone = TimeZone(secondsFromGMT: Int(-2.5 * 60 * 60))
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }

    func testAnswerTypeString_Codable() {
        do {
            let expectedObject = "hello"
            let expectedJson: JsonElement = .string("hello")

            let AnswerType = AnswerTypeString()
            let objectValue = try AnswerType.decodeAnswer(from: expectedJson)
            let jsonValue = try AnswerType.encodeAnswer(from: expectedObject)
            
            XCTAssertEqual(objectValue as? String, expectedObject)
            XCTAssertEqual(jsonValue, expectedJson)
            
        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
        }
    }

    func testAnswerTypeBoolean_Codable() {
         do {
            let expectedObject = true
            let expectedJson: JsonElement = .boolean(true)
            
            let AnswerType = AnswerTypeBoolean()
            let objectValue = try AnswerType.decodeAnswer(from: expectedJson)
            let jsonValue = try AnswerType.encodeAnswer(from: expectedObject)
            
            XCTAssertEqual(objectValue as? Bool, expectedObject)
            XCTAssertEqual(jsonValue, expectedJson)
            
        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
        }
    }

    func testAnswerTypeInteger_Codable() {
        do {
            let expectedObject = 12
            let expectedJson: JsonElement = .integer(12)
            
            let AnswerType = AnswerTypeInteger()
            let objectValue = try AnswerType.decodeAnswer(from: expectedJson)
            let jsonValue = try AnswerType.encodeAnswer(from: expectedObject)
            
            XCTAssertEqual(objectValue as? Int, expectedObject)
            XCTAssertEqual(jsonValue, expectedJson)
            
        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
        }
    }

    func testAnswerTypeNumber_Codable() {
        do {
            let expectedObject = 12.5
            let expectedJson: JsonElement = .number(12.5)
            
            let AnswerType = AnswerTypeNumber()
            let objectValue = try AnswerType.decodeAnswer(from: expectedJson)
            let jsonValue = try AnswerType.encodeAnswer(from: expectedObject)
            
            XCTAssertEqual(objectValue as? Double, expectedObject)
            XCTAssertEqual(jsonValue, expectedJson)
            
        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
        }
    }

    func testAnswerTypeDateTime_Codable() {

        do {
            let expectedJson: JsonElement = .string("2016-02-20")
            
            let AnswerType = AnswerTypeDateTime(codingFormat: "yyyy-MM-dd")

            let objectValue = try AnswerType.decodeAnswer(from: expectedJson)
            if let date = objectValue as? Date {
                let calendar = Calendar(identifier: .iso8601)
                let calendarComponents: Set<Calendar.Component> = [.year, .month, .day]
                let comp = calendar.dateComponents(calendarComponents, from: date)
                XCTAssertEqual(comp.year, 2016)
                XCTAssertEqual(comp.month, 2)
                XCTAssertEqual(comp.day, 20)
                
                let jsonValue = try AnswerType.encodeAnswer(from: date)
                XCTAssertEqual(expectedJson, jsonValue)
            }
            else {
                XCTFail("Failed to decode String to a Date: \(String(describing: objectValue))")
            }
            
        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
        }
    }

    func testAnswerTypeArray_String_Codable() {
        do {
            let expectedObject = ["alpha", "beta", "gamma"]
            let expectedJson: JsonElement = .array(["alpha", "beta", "gamma"])
            
            let AnswerType = AnswerTypeArray(baseType: .string)
            let objectValue = try AnswerType.decodeAnswer(from: expectedJson)
            let jsonValue = try AnswerType.encodeAnswer(from: expectedObject)
            
            XCTAssertEqual(objectValue as? [String], expectedObject)
            XCTAssertEqual(jsonValue, expectedJson)
            
        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
        }
    }

    func testAnswerTypeArray_Integer_Codable() {
        do {
            let expectedObject = [65, 47, 99]
            let expectedJson: JsonElement = .array([65, 47, 99])
            
            let AnswerType = AnswerTypeArray(baseType: .integer)
            let objectValue = try AnswerType.decodeAnswer(from: expectedJson)
            let jsonValue = try AnswerType.encodeAnswer(from: expectedObject)
            
            XCTAssertEqual(objectValue as? [Int], expectedObject)
            XCTAssertEqual(jsonValue, expectedJson)
            
        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
        }
    }

    func testAnswerTypeArray_Double_Codable() {
        do {
            let expectedObject = [65.3, 47.2, 99.8]
            let expectedJson: JsonElement = .array([65.3, 47.2, 99.8])
            
            let AnswerType = AnswerTypeArray(baseType: .number)
            let objectValue = try AnswerType.decodeAnswer(from: expectedJson)
            let jsonValue = try AnswerType.encodeAnswer(from: expectedObject)
            
            XCTAssertEqual(objectValue as? [Double], expectedObject)
            XCTAssertEqual(jsonValue, expectedJson)
            
        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
        }
    }
    
    func testAnswerTypeSerializer() {
        // Check that an example of all the standard results are included in the serializer.
        let serializer = AnswerTypeSerializer()
        let actual = Set(serializer.examples.map { $0.typeName })
        var expected = Set(["measurement", "date-time"]).union(JsonType.allCases.map { $0.rawValue })
        expected.remove("null")
        XCTAssertEqual(expected.count, actual.count)
        XCTAssertEqual(expected, actual)
        
        let standardTypes = Set(AnswerTypeType.allStandardTypes().map { $0.rawValue })
        XCTAssertEqual(expected.count, standardTypes.count)
        XCTAssertEqual(expected, standardTypes)

        let answerTypes: [SerializableAnswerType] = serializer.examples as! [SerializableAnswerType]

        answerTypes.forEach { object in

            do {
                let jsonData = try object.jsonEncodedData()
                guard let dictionary = try JSONSerialization.jsonObject(with: jsonData, options: []) as? [String : Any]
                    else {
                        XCTFail("Encoded object is not a dictionary")
                        return
                }

                XCTAssertEqual(object.typeName, dictionary["type"] as? String)

                let wrapper = try decoder.decode(_DecodingWrapper<AnswerType>.self, from: jsonData)
                let decodedObject = wrapper.value
                
                XCTAssertEqual(object.typeName, decodedObject.typeName)
                XCTAssertEqual("\(type(of: object))", "\(type(of: decodedObject))")

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
