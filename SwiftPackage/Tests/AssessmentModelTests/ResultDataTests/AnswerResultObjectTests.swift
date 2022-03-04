//
//  AnswerResultObjectTests.swift
//  
//
//  Copyright © 2017-2021 Sage Bionetworks. All rights reserved.
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

class AnswerResultObjectTests: XCTestCase {
    
    let decoder: JSONDecoder = AssessmentFactory().createJSONDecoder()

    var encoder: JSONEncoder = AssessmentFactory().createJSONEncoder()
    
    override func setUp() {
        super.setUp()
        
        // Use a statically defined timezone.
        ISO8601TimestampFormatter.timeZone = TimeZone(secondsFromGMT: Int(-2.5 * 60 * 60))
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }
    
    func testAnswerResultObject_String_NilValue_Codable() {
        let json = """
        {
            "identifier": "foo",
            "type": "answer",
            "startDate": "2017-10-16T22:28:09.000-02:30",
            "endDate": "2017-10-16T22:30:09.000-02:30",
            "answerType": {"type" : "string"}
        }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        do {
            
            let object = try decoder.decode(AnswerResultObject.self, from: json)
            
            XCTAssertEqual(object.identifier, "foo")
            XCTAssertEqual(object.typeName, "answer")
            XCTAssertGreaterThan(object.endDate, object.startDate)
            XCTAssertTrue(object.jsonAnswerType is AnswerTypeString, "\(String(describing: object.jsonAnswerType))")
            XCTAssertNil(object.jsonValue)
            
            let jsonData = try encoder.encode(object)
            guard let dictionary = try JSONSerialization.jsonObject(with: jsonData, options: []) as? [String : Any]
                else {
                    XCTFail("Encoded object is not a dictionary")
                    return
            }
            
            XCTAssertEqual(dictionary["identifier"] as? String, "foo")
            XCTAssertEqual(dictionary["type"] as? String, "answer")
            XCTAssertEqual(dictionary["startDate"] as? String, "2017-10-16T22:28:09.000-02:30")
            XCTAssertEqual(dictionary["endDate"] as? String, "2017-10-16T22:30:09.000-02:30")

        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
        }
    }
    
    func testAnswerResultObject_String_Codable() {
        let json = """
        {
            "identifier": "foo",
            "type": "answer",
            "startDate": "2017-10-16T22:28:09.000-02:30",
            "endDate": "2017-10-16T22:30:09.000-02:30",
            "answerType": {"type" : "string"},
            "value": "hello"
        }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        do {
            
            let object = try decoder.decode(AnswerResultObject.self, from: json)
            
            XCTAssertEqual(object.identifier, "foo")
            XCTAssertEqual(object.typeName, "answer")
            XCTAssertGreaterThan(object.endDate, object.startDate)
            XCTAssertTrue(object.jsonAnswerType is AnswerTypeString, "\(String(describing: object.jsonAnswerType))")
            XCTAssertEqual(object.jsonValue, .string("hello"))
            
            let jsonData = try encoder.encode(object)
            guard let dictionary = try JSONSerialization.jsonObject(with: jsonData, options: []) as? [String : Any]
                else {
                    XCTFail("Encoded object is not a dictionary")
                    return
            }
            
            XCTAssertEqual(dictionary["identifier"] as? String, "foo")
            XCTAssertEqual(dictionary["type"] as? String, "answer")
            XCTAssertEqual(dictionary["startDate"] as? String, "2017-10-16T22:28:09.000-02:30")
            XCTAssertEqual(dictionary["endDate"] as? String, "2017-10-16T22:30:09.000-02:30")
            XCTAssertEqual(dictionary["value"] as? String, "hello")
            if let answerType = dictionary["answerType"] as? [String:Any] {
                XCTAssertEqual(answerType["type"] as? String, "string")
            }
            else {
                XCTFail("Encoded object does not include the answerType")
            }
            
        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
        }
    }
    
    func testAnswerResultObject_Bool_Codable() {
        let json = """
        {
            "identifier": "foo",
            "type": "answer",
            "startDate": "2017-10-16T22:28:09.000-02:30",
            "endDate": "2017-10-16T22:30:09.000-02:30",
            "answerType": {"type" : "boolean"},
            "value": true
        }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        do {
            
            let object = try decoder.decode(AnswerResultObject.self, from: json)
            
            XCTAssertEqual(object.identifier, "foo")
            XCTAssertEqual(object.typeName, "answer")
            XCTAssertGreaterThan(object.endDate, object.startDate)
            XCTAssertTrue(object.jsonAnswerType is AnswerTypeBoolean, "\(String(describing: object.jsonAnswerType))")
            XCTAssertEqual(object.jsonValue, .boolean(true))
            
            let jsonData = try encoder.encode(object)
            guard let dictionary = try JSONSerialization.jsonObject(with: jsonData, options: []) as? [String : Any]
                else {
                    XCTFail("Encoded object is not a dictionary")
                    return
            }
            
            XCTAssertEqual(dictionary["identifier"] as? String, "foo")
            XCTAssertEqual(dictionary["type"] as? String, "answer")
            XCTAssertEqual(dictionary["startDate"] as? String, "2017-10-16T22:28:09.000-02:30")
            XCTAssertEqual(dictionary["endDate"] as? String, "2017-10-16T22:30:09.000-02:30")
            XCTAssertEqual(dictionary["value"] as? Bool, true)
            if let answerType = dictionary["answerType"] as? [String:Any] {
                XCTAssertEqual(answerType["type"] as? String, "boolean")
            }
            else {
                XCTFail("Encoded object does not include the answerType")
            }
            
        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
        }
    }
    
    func testAnswerResultObject_Int_Codable() {
        let json = """
        {
            "identifier": "foo",
            "type": "answer",
            "startDate": "2017-10-16T22:28:09.000-02:30",
            "endDate": "2017-10-16T22:30:09.000-02:30",
            "answerType": {"type" : "integer"},
            "value": 12
        }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        do {
            
            let object = try decoder.decode(AnswerResultObject.self, from: json)
            
            XCTAssertEqual(object.identifier, "foo")
            XCTAssertEqual(object.typeName, "answer")
            XCTAssertGreaterThan(object.endDate, object.startDate)
            XCTAssertTrue(object.jsonAnswerType is AnswerTypeInteger, "\(String(describing: object.jsonAnswerType))")
            XCTAssertEqual(object.jsonValue, .integer(12))
            
            let jsonData = try encoder.encode(object)
            guard let dictionary = try JSONSerialization.jsonObject(with: jsonData, options: []) as? [String : Any]
                else {
                    XCTFail("Encoded object is not a dictionary")
                    return
            }
            
            XCTAssertEqual(dictionary["identifier"] as? String, "foo")
            XCTAssertEqual(dictionary["type"] as? String, "answer")
            XCTAssertEqual(dictionary["startDate"] as? String, "2017-10-16T22:28:09.000-02:30")
            XCTAssertEqual(dictionary["endDate"] as? String, "2017-10-16T22:30:09.000-02:30")
            XCTAssertEqual(dictionary["value"] as? Int, 12)
            if let answerType = dictionary["answerType"] as? [String:Any] {
                XCTAssertEqual(answerType["type"] as? String, "integer")
            }
            else {
                XCTFail("Encoded object does not include the answerType")
            }
            
        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
        }
    }
    
    func testAnswerResultObject_Double_Codable() {
        let json = """
        {
            "identifier": "foo",
            "type": "answer",
            "startDate": "2017-10-16T22:28:09.000-02:30",
            "endDate": "2017-10-16T22:30:09.000-02:30",
            "answerType": {"type" : "number"},
            "value": 12.5
        }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        do {
            
            let object = try decoder.decode(AnswerResultObject.self, from: json)
            
            XCTAssertEqual(object.identifier, "foo")
            XCTAssertEqual(object.typeName, "answer")
            XCTAssertGreaterThan(object.endDate, object.startDate)
            XCTAssertTrue(object.jsonAnswerType is AnswerTypeNumber, "\(String(describing: object.jsonAnswerType))")
            XCTAssertEqual(object.jsonValue, .number(12.5))
            
            let jsonData = try encoder.encode(object)
            guard let dictionary = try JSONSerialization.jsonObject(with: jsonData, options: []) as? [String : Any]
                else {
                    XCTFail("Encoded object is not a dictionary")
                    return
            }
            
            XCTAssertEqual(dictionary["identifier"] as? String, "foo")
            XCTAssertEqual(dictionary["type"] as? String, "answer")
            XCTAssertEqual(dictionary["startDate"] as? String, "2017-10-16T22:28:09.000-02:30")
            XCTAssertEqual(dictionary["endDate"] as? String, "2017-10-16T22:30:09.000-02:30")
            XCTAssertEqual(dictionary["value"] as? Double, 12.5)
            if let answerType = dictionary["answerType"] as? [String:Any] {
                XCTAssertEqual(answerType["type"] as? String, "number")
            }
            else {
                XCTFail("Encoded object does not include the answerType")
            }
            
        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
        }
    }
    
    func testAnswerResultObject_Date_Codable() {
        let json = """
        {
            "identifier": "foo",
            "type": "answer",
            "startDate": "2017-10-16T22:28:09.000-02:30",
            "endDate": "2017-10-16T22:30:09.000-02:30",
            "answerType": {"type" : "date-time", "codingFormat" : "yyyy-MM-dd"},
            "value": "2016-02-20"
        }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        do {
            
            let object = try decoder.decode(AnswerResultObject.self, from: json)
            
            XCTAssertEqual(object.identifier, "foo")
            XCTAssertEqual(object.typeName, "answer")
            XCTAssertGreaterThan(object.endDate, object.startDate)
            
            if let answerType = object.jsonAnswerType as? AnswerTypeDateTime {
                XCTAssertEqual(answerType.codingFormat, "yyyy-MM-dd")
            }
            else {
                XCTFail("Failed to decode answerType as a AnswerTypeDateTime")
            }
            XCTAssertEqual(object.jsonValue, .string("2016-02-20"))
            
            let jsonData = try encoder.encode(object)
            guard let dictionary = try JSONSerialization.jsonObject(with: jsonData, options: []) as? [String : Any]
                else {
                    XCTFail("Encoded object is not a dictionary")
                    return
            }
            
            XCTAssertEqual(dictionary["identifier"] as? String, "foo")
            XCTAssertEqual(dictionary["type"] as? String, "answer")
            XCTAssertEqual(dictionary["startDate"] as? String, "2017-10-16T22:28:09.000-02:30")
            XCTAssertEqual(dictionary["endDate"] as? String, "2017-10-16T22:30:09.000-02:30")
            XCTAssertEqual(dictionary["value"] as? String, "2016-02-20")
            if let answerType = dictionary["answerType"] as? [String:Any] {
                XCTAssertEqual(answerType["type"] as? String, "date-time")
                XCTAssertEqual(answerType["codingFormat"] as? String, "yyyy-MM-dd")
            }
            else {
                XCTFail("Encoded object does not include the answerType")
            }
            
        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
        }
    }
    
    func testAnswerResultObject_StringArray_Codable() {
        let json = """
        {
            "identifier": "foo",
            "type": "answer",
            "startDate": "2017-10-16T22:28:09.000-02:30",
            "endDate": "2017-10-16T22:30:09.000-02:30",
            "answerType": {"baseType" : "integer", "type" : "array"},
            "value": [1, 3, 5]
        }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        do {
            
            let object = try decoder.decode(AnswerResultObject.self, from: json)
            
            XCTAssertEqual(object.identifier, "foo")
            XCTAssertEqual(object.typeName, "answer")
            XCTAssertGreaterThan(object.endDate, object.startDate)
            
            if let answerType = object.jsonAnswerType as? AnswerTypeArray {
                XCTAssertEqual(answerType.baseType, .integer)
            }
            else {
                XCTFail("Failed to decode \(String(describing: object.jsonAnswerType)) as a AnswerTypeArray")
            }
            XCTAssertEqual(object.jsonValue, .array([1, 3, 5]))
            
            let jsonData = try encoder.encode(object)
            guard let dictionary = try JSONSerialization.jsonObject(with: jsonData, options: []) as? [String : Any]
                else {
                    XCTFail("Encoded object is not a dictionary")
                    return
            }
            
            XCTAssertEqual(dictionary["identifier"] as? String, "foo")
            XCTAssertEqual(dictionary["type"] as? String, "answer")
            XCTAssertEqual(dictionary["startDate"] as? String, "2017-10-16T22:28:09.000-02:30")
            XCTAssertEqual(dictionary["endDate"] as? String, "2017-10-16T22:30:09.000-02:30")
            if let values = dictionary["value"] as? [Int] {
                XCTAssertEqual(values, [1, 3, 5])
            }
            else {
                XCTFail("Failed to encode the values. \(dictionary)")
            }
            if let answerType = dictionary["answerType"] as? [String:Any] {
                XCTAssertEqual(answerType["type"] as? String, "array")
                XCTAssertEqual(answerType["baseType"] as? String, "integer")
            }
            else {
                XCTFail("Encoded object does not include the answerType")
            }
            
        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
        }
    }

    func testAnswerResultObject_IntegerArray_StringSeparator_Codable() {
        let json = """
        {
            "identifier": "foo",
            "type": "answer",
            "startDate": "2017-10-16T22:28:09.000-02:30",
            "endDate": "2017-10-16T22:30:09.000-02:30",
            "answerType": {"baseType" : "integer", "type" : "array", "sequenceSeparator" : "-"},
            "value": "206-555-1212"
        }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        do {
            
            let object = try decoder.decode(AnswerResultObject.self, from: json)
            
            XCTAssertEqual(object.identifier, "foo")
            XCTAssertEqual(object.typeName, "answer")
            XCTAssertGreaterThan(object.endDate, object.startDate)
            
            if let answerType = object.jsonAnswerType as? AnswerTypeArray {
                XCTAssertEqual(answerType.baseType, .integer)
                XCTAssertEqual(answerType.sequenceSeparator, "-")
            }
            else {
                XCTFail("Failed to decode answerType as a AnswerTypeDateTime")
            }
            XCTAssertEqual(object.jsonValue, .array([206, 555, 1212]))
            
            let jsonData = try encoder.encode(object)
            guard let dictionary = try JSONSerialization.jsonObject(with: jsonData, options: []) as? [String : Any]
                else {
                    XCTFail("Encoded object is not a dictionary")
                    return
            }
            
            XCTAssertEqual(dictionary["identifier"] as? String, "foo")
            XCTAssertEqual(dictionary["type"] as? String, "answer")
            XCTAssertEqual(dictionary["startDate"] as? String, "2017-10-16T22:28:09.000-02:30")
            XCTAssertEqual(dictionary["endDate"] as? String, "2017-10-16T22:30:09.000-02:30")
            XCTAssertEqual(dictionary["value"] as? String, "206-555-1212")
            if let answerType = dictionary["answerType"] as? [String:Any] {
                XCTAssertEqual(answerType["baseType"] as? String, "integer")
                XCTAssertEqual(answerType["type"] as? String, "array")
                XCTAssertEqual(answerType["sequenceSeparator"] as? String, "-")
            }
            else {
                XCTFail("Encoded object does not include the answerType")
            }
            
        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
        }
    }

    func testAnswerResultObject_Codable() {
        let json = """
        {
            "identifier": "foo",
            "type": "answer",
            "startDate": "2017-10-16T22:28:09.000-02:30",
            "endDate": "2017-10-16T22:30:09.000-02:30",
            "answerType": {"type" : "object"},
            "value": { "breakfast": "08:20", "lunch": "12:40", "dinner": "19:10" }
        }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        do {
            
            let object = try decoder.decode(AnswerResultObject.self, from: json)
            
            XCTAssertEqual(object.identifier, "foo")
            XCTAssertEqual(object.typeName, "answer")
            XCTAssertGreaterThan(object.endDate, object.startDate)
            
            XCTAssertTrue(object.jsonAnswerType is AnswerTypeObject, "\(String(describing: object.jsonAnswerType))")
            XCTAssertEqual(object.jsonValue, .object(["breakfast": "08:20", "lunch": "12:40", "dinner": "19:10"]))
            
            let jsonData = try encoder.encode(object)
            guard let dictionary = try JSONSerialization.jsonObject(with: jsonData, options: []) as? [String : Any]
                else {
                    XCTFail("Encoded object is not a dictionary")
                    return
            }
            
            XCTAssertEqual(dictionary["identifier"] as? String, "foo")
            XCTAssertEqual(dictionary["type"] as? String, "answer")
            XCTAssertEqual(dictionary["startDate"] as? String, "2017-10-16T22:28:09.000-02:30")
            XCTAssertEqual(dictionary["endDate"] as? String, "2017-10-16T22:30:09.000-02:30")
            if let values = dictionary["value"] as? [String : String] {
                XCTAssertEqual(values["breakfast"], "08:20")
                XCTAssertEqual(values["lunch"], "12:40")
                XCTAssertEqual(values["dinner"], "19:10")
            }
            else {
                XCTFail("Failed to encode the values. \(dictionary)")
            }
            if let answerType = dictionary["answerType"] as? [String:Any] {
                XCTAssertEqual(answerType["type"] as? String, "object")
            }
            else {
                XCTFail("Encoded object does not include the answerType")
            }
            
        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
        }
    }
    
}
