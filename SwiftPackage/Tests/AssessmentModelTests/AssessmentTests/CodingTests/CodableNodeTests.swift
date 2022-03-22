//
//  CodableQuestionTests.swift
//  ResearchTests_iOS
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
    
    // MARK: Branch and Assessment
    
    func testAssessment_Default_Codable() {
        
        let json = """
            {
                 "identifier": "foo",
                 "type": "assessment",
                 "steps": [
                    {
                         "identifier": "foo",
                         "type": "instruction"
                    }
                ]
            }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        XCTAssertEqual(.StandardTypes.assessment.nodeType, AssessmentObject.defaultType())
        checkDefaultSharedKeys(step: AssessmentObject(identifier: "foo", children: []))
        checkResult(step: AssessmentObject(identifier: "foo", children: []), type: AssessmentResultObject.self)
        
        do {
            
            let wrapper = try decoder.decode(NodeWrapper<AssessmentObject>.self, from: json)
            let object = wrapper.node
            
            checkDefaultSharedKeys(step: object)
            XCTAssertEqual("foo", object.identifier)
            XCTAssertEqual(.StandardTypes.assessment.nodeType, object.serializableType)
            
            let actualEncoding = try encoder.encode(object)
            try checkEncodedJson(expected: json, actual: actualEncoding)

        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
            return
        }
    }
    
    func testAssessment_AllFields_Codable() {
        
        let json = """
        {
            "type": "assessment",
            "identifier": "foo",
            "versionString":"0.1.2",
            "estimatedMinutes":3,
            "copyright":"Baroo, Inc.",
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
             "steps": [
                {
                     "identifier": "foo",
                     "type": "instruction"
                }
            ]
        }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        do {
            
            let wrapper = try decoder.decode(NodeWrapper<AssessmentObject>.self, from: json)
            let object = wrapper.node
            
            XCTAssertEqual("foo", object.identifier)
            XCTAssertEqual(.StandardTypes.assessment.nodeType, object.serializableType)
            XCTAssertEqual(1, object.children.count)
            XCTAssertTrue(object.children.first is InstructionStepObject)
            XCTAssertEqual("0.1.2", object.versionString)
            XCTAssertEqual("Baroo, Inc.", object.copyright)
            XCTAssertEqual(3, object.estimatedMinutes)
            
            checkSharedEncodingKeys(step: object)

            let actualEncoding = try encoder.encode(object)
            try checkEncodedJson(expected: json, actual: actualEncoding)

        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
            return
        }
    }
    
    func testSection_Default_Codable() {
        
        let json = """
            {
                 "identifier": "foo",
                 "type": "section",
                 "steps": [
                    {
                         "identifier": "foo",
                         "type": "instruction"
                    }
                ]
            }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        XCTAssertEqual(.StandardTypes.section.nodeType, SectionObject.defaultType())
        checkDefaultSharedKeys(step: SectionObject(identifier: "foo", children: []))
        checkResult(step: SectionObject(identifier: "foo", children: []), type: BranchNodeResultObject.self)
        
        do {
            
            let wrapper = try decoder.decode(NodeWrapper<SectionObject>.self, from: json)
            let object = wrapper.node
            
            checkDefaultSharedKeys(step: object)
            XCTAssertEqual("foo", object.identifier)
            XCTAssertEqual(.StandardTypes.section.nodeType, object.serializableType)
            
            let actualEncoding = try encoder.encode(object)
            try checkEncodedJson(expected: json, actual: actualEncoding)

        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
            return
        }
    }
    
    func testSection_AllFields_Codable() {
        
        let json = """
        {
            "type": "section",
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
             "steps": [
                {
                     "identifier": "foo",
                     "type": "instruction"
                }
            ]
        }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        do {
            
            let wrapper = try decoder.decode(NodeWrapper<SectionObject>.self, from: json)
            let object = wrapper.node
            
            XCTAssertEqual("foo", object.identifier)
            XCTAssertEqual(.StandardTypes.section.nodeType, object.serializableType)
            XCTAssertEqual(1, object.children.count)
            XCTAssertTrue(object.children.first is InstructionStepObject)
            
            checkSharedEncodingKeys(step: object)

            let actualEncoding = try encoder.encode(object)
            try checkEncodedJson(expected: json, actual: actualEncoding)
            
            let copy = object.copy(with: "bar")
            XCTAssertEqual("bar", copy.identifier)
            XCTAssertEqual(.StandardTypes.section.nodeType, copy.serializableType)
            XCTAssertEqual(1, copy.children.count)
            checkSharedEncodingKeys(step: copy)

        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
            return
        }
    }
    
    // MARK: Instructions
    
    func testOverviewStep_Default_Codable() {
        
        let json = """
            {
                 "identifier": "foo",
                 "type": "overview"
            }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        XCTAssertEqual(.StandardTypes.overview.nodeType, OverviewStepObject.defaultType())
        checkDefaultSharedKeys(step: OverviewStepObject(identifier: "foo"))
        checkResult(step: OverviewStepObject(identifier: "foo"), type: ResultObject.self)
        
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
            
            let copy = object.copy(with: "bar")
            XCTAssertEqual("bar", copy.identifier)
            XCTAssertEqual(object.serializableType, copy.serializableType)
            checkSharedEncodingKeys(step: copy)

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
        checkResult(step: InstructionStepObject(identifier: "foo"), type: ResultObject.self)
        
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
            
            let copy = object.copy(with: "bar")
            XCTAssertEqual("bar", copy.identifier)
            XCTAssertEqual(object.serializableType, copy.serializableType)
            XCTAssertTrue(copy.fullInstructionsOnly)
            checkSharedEncodingKeys(step: copy)

        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
            return
        }
    }
    
    func testCompletionStep_Default_Codable() {
        
        let json = """
            {
                 "identifier": "foo",
                 "type": "completion"
            }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        XCTAssertEqual(.StandardTypes.completion.nodeType, CompletionStepObject.defaultType())
        checkDefaultSharedKeys(step: CompletionStepObject(identifier: "foo"))
        checkResult(step: CompletionStepObject(identifier: "foo"), type: ResultObject.self)
        
        do {
            
            let wrapper = try decoder.decode(NodeWrapper<CompletionStepObject>.self, from: json)
            let object = wrapper.node
            
            checkDefaultSharedKeys(step: object)
            XCTAssertEqual("foo", object.identifier)
            XCTAssertEqual(.StandardTypes.completion.nodeType, object.serializableType)
            
            let actualEncoding = try encoder.encode(object)
            try checkEncodedJson(expected: json, actual: actualEncoding)

        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
            return
        }
    }
    
    // MARK: Questions

    func testSimpleQuestion_Default_Codable() {
        
        let json = """
            {
                 "identifier": "foo",
                 "type": "simpleQuestion"
            }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        XCTAssertEqual(.StandardTypes.simpleQuestion.nodeType, SimpleQuestionStepObject.defaultType())
        checkDefaultSharedKeys(step: SimpleQuestionStepObject(identifier: "foo"))
        checkResult(step: SimpleQuestionStepObject(identifier: "foo"), type: AnswerResultObject.self)
        
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
            },
            "surveyRules": [{"skipToIdentifier" : "boomer", "matchingAnswer" : 1964, "ruleOperator": "lt" }]
        }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        do {
            
            let wrapper = try decoder.decode(NodeWrapper<SimpleQuestionStepObject>.self, from: json)
            let object = wrapper.node
            
            let expectedRules = [JsonSurveyRuleObject(skipToIdentifier: "boomer", matchingValue: .integer(1964), ruleOperator: .lessThan)]
            
            XCTAssertEqual("foo", object.identifier)
            XCTAssertEqual(.StandardTypes.simpleQuestion.nodeType, object.serializableType)
            XCTAssertTrue(object.inputItem is YearTextInputItemObject)
            XCTAssertEqual(expectedRules, object.surveyRules)
            checkSharedEncodingKeys(question: object)

            let actualEncoding = try encoder.encode(object)
            try checkEncodedJson(expected: json, actual: actualEncoding)
            
            let copy = object.copy(with: "bar")
            XCTAssertEqual("bar", copy.identifier)
            XCTAssertEqual(object.serializableType, copy.serializableType)
            XCTAssertTrue(copy.inputItem is YearTextInputItemObject)
            XCTAssertEqual(expectedRules, copy.surveyRules)
            checkSharedEncodingKeys(question: copy)

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
        checkDefaultSharedKeys(step: ChoiceQuestionStepObject(identifier: "foo", choices: [.init(text: "bar")]))
        checkResult(step: ChoiceQuestionStepObject(identifier: "foo", choices: [.init(text: "bar")]), type: AnswerResultObject.self)
        
        do {
            
            let wrapper = try decoder.decode(NodeWrapper<ChoiceQuestionStepObject>.self, from: json)
            let object = wrapper.node
            checkDefaultSharedKeys(step: object)
            
            XCTAssertEqual("foo", object.identifier)
            XCTAssertEqual(.StandardTypes.choiceQuestion.nodeType, object.serializableType)
            XCTAssertEqual(.string, object.baseType)
            
            let choices: [JsonChoice] = [
                .init(value: .string("one"), text: "one"),
                .init(value: .string("two"), text: "two"),
                .init(text: "none", selectorType: .exclusive)
            ]
            XCTAssertEqual(choices, object.choices)
            
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
            "surveyRules": [{"skipToIdentifier" : "one", "matchingAnswer" : 1, "ruleOperator": "le" }]
        }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        do {

            let wrapper = try decoder.decode(NodeWrapper<ChoiceQuestionStepObject>.self, from: json)
            let object = wrapper.node
            
            let expectedRules = [JsonSurveyRuleObject(skipToIdentifier: "one", matchingValue: .integer(1), ruleOperator: .lessThanEqual)]
            
            XCTAssertEqual("foo", object.identifier)
            XCTAssertEqual(.StandardTypes.choiceQuestion.nodeType, object.serializableType)
            XCTAssertEqual(.integer, object.baseType)
            XCTAssertEqual(expectedRules, object.surveyRules)
            
            let choices: [JsonChoice] = [
                .init(value: .integer(1), text: "one"),
                .init(value: .integer(2), text: "two"),
                .init(text: "none", selectorType: .exclusive)
            ]
            XCTAssertEqual(choices, object.choices)
            XCTAssertTrue(object.other is IntegerTextInputItemObject)
            
            checkSharedEncodingKeys(question: object)

            let actualEncoding = try encoder.encode(object)
            try checkEncodedJson(expected: json, actual: actualEncoding)
            
            let copy = object.copy(with: "bar")
            XCTAssertEqual("bar", copy.identifier)
            XCTAssertEqual(object.serializableType, copy.serializableType)
            XCTAssertEqual(.integer, copy.baseType)
            XCTAssertEqual(choices, copy.choices)
            XCTAssertTrue(copy.other is IntegerTextInputItemObject)
            XCTAssertEqual(expectedRules, object.surveyRules)
            checkSharedEncodingKeys(question: copy)

        } catch let err {
            XCTFail("Failed to decode/encode object: \(err)")
            return
        }
    }
    
    // MARK: helpers
    
    func checkResult<T : ResultData>(step: AbstractNodeObject, type: T.Type) {
        let result = step.instantiateResult()
        XCTAssertEqual(result.identifier, step.identifier)
        XCTAssertTrue(result is T)
    }
    
    func checkDefaultSharedKeys(step: AbstractContentNodeObject) {
        XCTAssertEqual(type(of: step).defaultType(), step.serializableType)
        XCTAssertNil(step.comment)
        XCTAssertTrue(step.shouldHideButtons.isEmpty)
        XCTAssertTrue(step.buttonMap.isEmpty)
        XCTAssertNil(step.title)
        XCTAssertNil(step.subtitle)
        XCTAssertNil(step.detail)
        XCTAssertNil(step.imageInfo)
    }
    
    func checkSharedEncodingKeys(step: AbstractContentNodeObject) {
        XCTAssertEqual(type(of: step).defaultType(), step.serializableType)
        XCTAssertEqual("comment", step.comment)
        XCTAssertEqual([ButtonType.navigation(.skip)], step.shouldHideButtons)
        let expectedButtonMap: [ButtonType : ButtonActionInfoObject] = [
            .navigation(.goForward) : ButtonActionInfoObject(buttonTitle: "Go, Dogs! Go!")
        ]
        XCTAssertEqual(expectedButtonMap, step.buttonMap as? [ButtonType : ButtonActionInfoObject])
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
