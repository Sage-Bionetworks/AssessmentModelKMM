//
//  CodableQuestionTests.swift
//  ResearchTests_iOS
//
//  Copyright © 2020 Sage Bionetworks. All rights reserved.
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
@testable import AssessmentModel
import JsonModel

class CodableQuestionTests: XCTestCase {
    
    let decoder = AssessmentFactory().createJSONDecoder()
    let encoder = AssessmentFactory().createJSONEncoder()

    override func setUp() {
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }

    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }
    
    func testOverviewStep_Default_Codable() {
        
        let json = """
            {
                 "identifier": "foo",
                 "type": "overview"
            }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        XCTAssertEqual(.StandardTypes.overview.nodeType, OverviewStepObject.defaultType())
        checkDefaultSharedKeys(step: OverviewStepObject(identifier: "foo"))
        
        do {
            
            let wrapper = try decoder.decode(NodeWrapper<OverviewStepObject>.self, from: json)
            let object = wrapper.node
            
            checkDefaultSharedKeys(step: object)
            XCTAssertEqual("foo", object.identifier)
            XCTAssertEqual(.StandardTypes.overview.nodeType, object.serializableType)
            
            let actualEncoding = try encoder.encode(object)
            try checkEncodedJson(expected: json, actual: actualEncoding)

        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
            return
        }
    }
    
    func testOverviewStep_AllFields_Codable() {
        
        let json = """
        {
            "type": "overview",
            "identifier": "foo",
            "comment": "comment",
            "shouldHideActions": ["skip"],
            "actions": {
                "goForward": {
                    "type": "default",
                    "buttonTitle": "Go, Dogs! Go!"
                }
            },
            "title": "Hello World!",
            "subtitle": "Question subtitle",
            "detail": "Some text. This is a test.",
            "image": {
                "type": "animated",
                "imageNames": ["foo1", "foo2", "foo3", "foo4"],
                "animationDuration": 2
            }
        }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        do {
            
            let wrapper = try decoder.decode(NodeWrapper<OverviewStepObject>.self, from: json)
            let object = wrapper.node
            
            XCTAssertEqual("foo", object.identifier)
            XCTAssertEqual(.StandardTypes.overview.nodeType, object.serializableType)
            
            checkSharedEncodingKeys(step: object)

            let actualEncoding = try encoder.encode(object)
            try checkEncodedJson(expected: json, actual: actualEncoding)

        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
            return
        }
    }
    
    func testInstructionStep_Default_Codable() {
        
        let json = """
            {
                 "identifier": "foo",
                 "type": "instruction"
            }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        XCTAssertEqual(.StandardTypes.instruction.nodeType, InstructionStepObject.defaultType())
        checkDefaultSharedKeys(step: InstructionStepObject(identifier: "foo"))
        
        do {
            
            let wrapper = try decoder.decode(NodeWrapper<InstructionStepObject>.self, from: json)
            let object = wrapper.node
            
            checkDefaultSharedKeys(step: object)
            XCTAssertEqual("foo", object.identifier)
            XCTAssertEqual(.StandardTypes.instruction.nodeType, object.serializableType)
            XCTAssertFalse(object.fullInstructionsOnly)
            XCTAssertNil(object.spokenInstructions)
            
            let actualEncoding = try encoder.encode(object)
            try checkEncodedJson(expected: json, actual: actualEncoding)

        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
            return
        }
    }
    
    func testInstructionStep_AllFields_Codable() {
        
        let json = """
        {
            "type": "instruction",
            "identifier": "foo",
            "comment": "comment",
            "shouldHideActions": ["skip"],
            "actions": {
                "goForward": {
                    "type": "default",
                    "buttonTitle": "Go, Dogs! Go!"
                }
            },
            "title": "Hello World!",
            "subtitle": "Question subtitle",
            "detail": "Some text. This is a test.",
            "image": {
                "type": "animated",
                "imageNames": ["foo1", "foo2", "foo3", "foo4"],
                "animationDuration": 2
            },
            "fullInstructionsOnly": true,
            "spokenInstructions": {
                "start": "Begin now",
                "end": "You are done!"
            }
        }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        do {
            
            let wrapper = try decoder.decode(NodeWrapper<InstructionStepObject>.self, from: json)
            let object = wrapper.node
            
            XCTAssertEqual("foo", object.identifier)
            XCTAssertEqual(.StandardTypes.instruction.nodeType, object.serializableType)
            XCTAssertTrue(object.fullInstructionsOnly)
            XCTAssertEqual([.start : "Begin now", .end : "You are done!"], object.spokenInstructions)
            
            checkSharedEncodingKeys(step: object)

            let actualEncoding = try encoder.encode(object)
            try checkEncodedJson(expected: json, actual: actualEncoding)

        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
            return
        }
    }

    func testSimpleQuestion_Default_Codable() {
        
        let json = """
            {
                 "identifier": "foo",
                 "type": "simpleQuestion"
            }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        XCTAssertEqual(.StandardTypes.simpleQuestion.nodeType, SimpleQuestionStepObject.defaultType())
        checkDefaultSharedKeys(step: SimpleQuestionStepObject(identifier: "foo"))
        
        do {
            
            let wrapper = try decoder.decode(NodeWrapper<SimpleQuestionStepObject>.self, from: json)
            let object = wrapper.node
            
            checkDefaultSharedKeys(step: object)
            XCTAssertEqual("foo", object.identifier)
            XCTAssertEqual(.StandardTypes.simpleQuestion.nodeType, object.serializableType)
            XCTAssertTrue(object.inputItem is StringTextInputItemObject)
            
            let actualEncoding = try encoder.encode(object)
            try checkEncodedJson(expected: json, actual: actualEncoding)

        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
            return
        }
    }
    
    func testSimpleQuestion_AllFields_Codable() {
        
        let json = """
        {
            "type": "simpleQuestion",
            "identifier": "foo",
            "comment": "comment",
            "shouldHideActions": ["skip"],
            "actions": {
                "goForward": {
                    "type": "default",
                    "buttonTitle": "Go, Dogs! Go!"
                }
            },
            "title": "Hello World!",
            "subtitle": "Question subtitle",
            "detail": "Some text. This is a test.",
            "image": {
                "type": "animated",
                "imageNames": ["foo1", "foo2", "foo3", "foo4"],
                "animationDuration": 2
            },
            "optional": true,
            "uiHint": "allThatJazz",
            "inputItem": {
                "type": "year"
            }
        }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        do {
            
            let wrapper = try decoder.decode(NodeWrapper<SimpleQuestionStepObject>.self, from: json)
            let object = wrapper.node
            
            XCTAssertEqual("foo", object.identifier)
            XCTAssertEqual(.StandardTypes.simpleQuestion.nodeType, object.serializableType)
            XCTAssertTrue(object.inputItem is YearTextInputItemObject)
            checkSharedEncodingKeys(question: object)

            let actualEncoding = try encoder.encode(object)
            try checkEncodedJson(expected: json, actual: actualEncoding)

        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
            return
        }
    }
    
    func testChoiceQuestion_Default_Codable() {
        
        let json = """
            {
                "identifier": "foo",
                "type": "choiceQuestion",
                "choices": [{
                        "text": "one",
                        "value": "one"
                    },
                    {
                        "text": "two",
                        "value": "two"
                    },
                    {
                        "text": "none",
                        "selectorType": "exclusive"
                    }
                ]
            }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        XCTAssertEqual(.StandardTypes.choiceQuestion.nodeType, ChoiceQuestionStepObject.defaultType())
        checkDefaultSharedKeys(step: ChoiceQuestionStepObject(identifier: "foo", choices: []))
        
        do {
            
            let wrapper = try decoder.decode(NodeWrapper<ChoiceQuestionStepObject>.self, from: json)
            let object = wrapper.node
            checkDefaultSharedKeys(step: object)
            
            XCTAssertEqual("foo", object.identifier)
            XCTAssertEqual(.StandardTypes.choiceQuestion.nodeType, object.serializableType)
            XCTAssertEqual(.string, object.baseType)
            
            let choices: [JsonChoiceObject] = [
                .init(value: .string("one"), text: "one"),
                .init(value: .string("two"), text: "two"),
                .init(text: "none", selectorType: .exclusive)
            ]
            XCTAssertEqual(choices, object.choices as? [JsonChoiceObject])
            
            let actualEncoding = try encoder.encode(object)
            try checkEncodedJson(expected: json, actual: actualEncoding)

        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
            return
        }
    }
    
    func testChoiceQuestion_AllFields_Codable() {
        
        let json = """
        {
            "identifier": "foo",
            "type": "choiceQuestion",
            "baseType": "integer",
            "choices": [{
                    "text": "one",
                    "value": 1
                },
                {
                    "text": "two",
                    "value": 2
                },
                {
                    "text": "none",
                    "selectorType": "exclusive"
                }
            ],
            "other": { "type": "integer" },
            "comment": "comment",
            "shouldHideActions": ["skip"],
            "actions": {
                "goForward": {
                    "type": "default",
                    "buttonTitle": "Go, Dogs! Go!"
                }
            },
            "title": "Hello World!",
            "subtitle": "Question subtitle",
            "detail": "Some text. This is a test.",
            "image": {
                "type": "animated",
                "imageNames": ["foo1", "foo2", "foo3", "foo4"],
                "animationDuration": 2
            },
            "optional": true,
            "uiHint": "allThatJazz",
        }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        do {

            let wrapper = try decoder.decode(NodeWrapper<ChoiceQuestionStepObject>.self, from: json)
            let object = wrapper.node
            
            XCTAssertEqual("foo", object.identifier)
            XCTAssertEqual(.StandardTypes.choiceQuestion.nodeType, object.serializableType)
            XCTAssertEqual(.integer, object.baseType)
            
            let choices: [JsonChoiceObject] = [
                .init(value: .integer(1), text: "one"),
                .init(value: .integer(2), text: "two"),
                .init(text: "none", selectorType: .exclusive)
            ]
            XCTAssertEqual(choices, object.choices as? [JsonChoiceObject])
            XCTAssertTrue(object.other is IntegerTextInputItemObject)
            
            checkSharedEncodingKeys(question: object)

            let actualEncoding = try encoder.encode(object)
            try checkEncodedJson(expected: json, actual: actualEncoding)

        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
            return
        }
    }
    
    func checkDefaultSharedKeys(step: AbstractStepObject) {
        XCTAssertEqual(type(of: step).defaultType(), step.serializableType)
        XCTAssertNil(step.comment)
        XCTAssertTrue(step.shouldHideButtons.isEmpty)
        XCTAssertTrue(step.buttonMap.isEmpty)
        XCTAssertNil(step.title)
        XCTAssertNil(step.subtitle)
        XCTAssertNil(step.detail)
        XCTAssertNil(step.imageInfo)
    }
    
    func checkSharedEncodingKeys(step: AbstractStepObject) {
        XCTAssertEqual(type(of: step).defaultType(), step.serializableType)
        XCTAssertEqual("foo", step.identifier)
        XCTAssertEqual("comment", step.comment)
        XCTAssertEqual([ButtonAction.navigation(.skip)], step.shouldHideButtons)
        let expectedButtonMap: [ButtonAction : ButtonActionInfoObject] = [
            .navigation(.goForward) : ButtonActionInfoObject(buttonTitle: "Go, Dogs! Go!")
        ]
        XCTAssertEqual(expectedButtonMap, step.buttonMap as? [ButtonAction : ButtonActionInfoObject])
        XCTAssertEqual("Hello World!", step.title)
        XCTAssertEqual("Question subtitle", step.subtitle)
        XCTAssertEqual("Some text. This is a test.", step.detail)
        if let image = step.imageInfo as? AnimatedImage {
            XCTAssertEqual(["foo1", "foo2", "foo3", "foo4"], image.imageNames)
            XCTAssertEqual(2.0, image.animationDuration, accuracy: 0.001)
        }
        else {
            XCTFail("Failed to decoded expected image.")
        }
    }
    
    func checkSharedEncodingKeys(question: AbstractQuestionStepObject) {
        checkSharedEncodingKeys(step: question)
        XCTAssertTrue(question.optional)
        XCTAssertEqual(QuestionUIHint(rawValue: "allThatJazz"), question.uiHint)
    }
    
    func checkEncodedJson(expected: Data, actual: Data) throws {
        guard let dictionary = try JSONSerialization.jsonObject(with: actual, options: []) as? [String : Any]
            else {
                XCTFail("Encoded object is not a dictionary")
                return
        }
        guard let expectedDictionary = try JSONSerialization.jsonObject(with: expected, options: []) as? [String : Any]
            else {
                XCTFail("input json not a dictionary")
                return
        }
        
        expectedDictionary.forEach { (pair) in
            let encodedValue = dictionary[pair.key]
            XCTAssertNotNil(encodedValue, "\(pair.key)")
            if let str = pair.value as? String {
                XCTAssertEqual(str, encodedValue as? String, "\(pair.key)")
            }
            else if let num = pair.value as? NSNumber {
                XCTAssertEqual(num, encodedValue as? NSNumber, "\(pair.key)")
            }
            else if let arr = pair.value as? NSArray {
                XCTAssertEqual(arr, encodedValue as? NSArray, "\(pair.key)")
            }
            else if let dict = pair.value as? NSDictionary {
                XCTAssertEqual(dict, encodedValue as? NSDictionary, "\(pair.key)")
            }
            else {
                XCTFail("Failed to match \(pair.key)")
            }
        }
    }
    
    struct NodeWrapper<Value : Node> : Decodable {
        let node : Value
        init(from decoder: Decoder) throws {
            let step = try decoder.serializationFactory.decodePolymorphicObject(Node.self, from: decoder)
            guard let qStep = step as? Value else {
                let context = DecodingError.Context(codingPath: decoder.codingPath, debugDescription: "Failed to decode a QuestionStep")
                throw DecodingError.typeMismatch(Value.self, context)
            }
            self.node = qStep
        }
    }
}
