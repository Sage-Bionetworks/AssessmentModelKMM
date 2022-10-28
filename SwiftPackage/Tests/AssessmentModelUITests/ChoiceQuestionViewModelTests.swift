//
//  ChoiceQuestionViewModelTests.swift
//
//
//  Copyright © 2022 Sage Bionetworks. All rights reserved.
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

import Foundation
@testable import AssessmentModelUI
@testable import AssessmentModel
import JsonModel
import ResultModel
import XCTest

class ChoiceQuestionViewModelTests: XCTestCase {
    
    override func setUp() {
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }

    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }
    
    func testMultipleChoiceQuestionSelection_InitialAnswerNull() {
        let multipleChoiceQuestion = ChoiceQuestionStepObject(identifier: "multipleChoice",
                                 choices: [
                                    "Blue",
                                    "Green",
                                    "Yellow",
                                    "Red",
                                    .init(text: "All of the above", selectorType: .all),
                                    .init(text: "I don't have any", selectorType: .exclusive),
                                 ],
                                 baseType: .string,
                                 singleChoice: false,
                                 other: StringTextInputItemObject(),
                                 title: "What are your favorite colors?")
        
        let questionState = QuestionState(multipleChoiceQuestion, answerResult: nil)
        let viewModel = ChoiceQuestionViewModel()
        viewModel.initialize(questionState)
        
        // Check initial state
        XCTAssertEqual(6, viewModel.choices.count)
        XCTAssertNotNil(viewModel.otherChoice)
        XCTAssertFalse(questionState.hasSelectedAnswer)
        viewModel.choices.forEach { choice in
            XCTAssertFalse(choice.selected, "Expected selection to be false for \(choice.id)")
        }
        
        // Select a choice
        viewModel.choices[2].selected = true
        XCTAssertEqual("Yellow", viewModel.choices[2].id)
        
        // Check new values
        XCTAssertTrue(questionState.hasSelectedAnswer)
        viewModel.choices.forEach { choice in
            if choice.id == "Yellow" {
                XCTAssertTrue(choice.selected, "Expected selection to be true for \(choice.id)")
            } else {
                XCTAssertFalse(choice.selected, "Expected selection to be false for \(choice.id)")
            }
        }
        XCTAssertEqual(.array(["Yellow"]), questionState.answerResult.jsonValue)
        
        // Select all of the above
        viewModel.choices[4].selected = true
        XCTAssertEqual(.all, viewModel.choices[4].selectorType)
        
        // Check new values
        XCTAssertTrue(questionState.hasSelectedAnswer)
        XCTAssertEqual(.array(["Blue","Green","Yellow","Red"]), questionState.answerResult.jsonValue)
        
        // Select other - this will not automatically update the answer b/c selection happens before change of value.
        viewModel.otherChoice?.selected = true
        viewModel.otherChoice?.value = "Purple"
        viewModel.updateAnswer()
        
        // Check new values
        XCTAssertTrue(questionState.hasSelectedAnswer)
        XCTAssertEqual(.array(["Blue","Green","Yellow","Red","Purple"]), questionState.answerResult.jsonValue)
        
        // Deselect all of the above
        viewModel.choices[4].selected = false

        // Check new values
        XCTAssertTrue(questionState.hasSelectedAnswer)
        XCTAssertEqual(.array(["Purple"]), questionState.answerResult.jsonValue)
        
        // Deselect other
        viewModel.otherChoice?.selected = false
        
        // Check new values
        XCTAssertFalse(questionState.hasSelectedAnswer)
        XCTAssertNil(questionState.answerResult.jsonValue)
        
        // Select answer and other
        viewModel.otherChoice?.selected = true
        viewModel.otherChoice?.value = "Orange"
        viewModel.choices[2].selected = true
        
        // Check new values
        XCTAssertTrue(questionState.hasSelectedAnswer)
        XCTAssertEqual(.array(["Yellow","Orange"]), questionState.answerResult.jsonValue)
        
        // select "none of the above"
        viewModel.choices[5].selected = true
        
        // Check new values
        XCTAssertTrue(questionState.hasSelectedAnswer)
        XCTAssertEqual(.array([]), questionState.answerResult.jsonValue)
        
        // Select "all of the above" again
        viewModel.choices[4].selected = true
        
        // Check new values
        XCTAssertTrue(questionState.hasSelectedAnswer)
        XCTAssertEqual(.array(["Blue","Green","Yellow","Red"]), questionState.answerResult.jsonValue)
        
        // Deselect "yellow" which should also deselect "all"
        viewModel.choices[2].selected = false
        
        XCTAssertEqual(.array(["Blue","Green","Red"]), questionState.answerResult.jsonValue)
        XCTAssertFalse(viewModel.choices[4].selected)
    }
    
    func testMultipleChoiceQuestionSelection_InitialAnswerNone() {
        let multipleChoiceQuestion = ChoiceQuestionStepObject(identifier: "multipleChoice",
                                 choices: [
                                    "Blue",
                                    "Green",
                                    "Yellow",
                                    "Red",
                                    .init(text: "All of the above", selectorType: .all),
                                    .init(text: "I don't have any", selectorType: .exclusive),
                                 ],
                                 baseType: .string,
                                 singleChoice: false,
                                 other: StringTextInputItemObject(),
                                 title: "What are your favorite colors?")
        
        let questionState = QuestionState(multipleChoiceQuestion, answerResult: AnswerResultObject(identifier: "multipleChoice", value: .array([])))
        let viewModel = ChoiceQuestionViewModel()
        viewModel.initialize(questionState)
        
        // Check initial state
        XCTAssertEqual(6, viewModel.choices.count)
        XCTAssertNotNil(viewModel.otherChoice)
        XCTAssertTrue(questionState.hasSelectedAnswer)
        XCTAssertEqual(.array([]), questionState.answerResult.jsonValue)
        viewModel.choices.forEach { choice in
            if choice.id == "I don't have any" {
                XCTAssertTrue(choice.selected, "Expected selection to be true for '\(choice.id)'")
            }
            else {
                XCTAssertFalse(choice.selected, "Expected selection to be false for '\(choice.id)'")
            }
        }
    }
    
    func testMultipleChoiceQuestionSelection_InitialAnswer() {
        let multipleChoiceQuestion = ChoiceQuestionStepObject(identifier: "multipleChoice",
                                 choices: [
                                    "Blue",
                                    "Green",
                                    "Yellow",
                                    "Red",
                                    .init(text: "All of the above", selectorType: .all),
                                    .init(text: "I don't have any", selectorType: .exclusive),
                                 ],
                                 baseType: .string,
                                 singleChoice: false,
                                 other: StringTextInputItemObject(),
                                 title: "What are your favorite colors?")
        
        let initialChoices = ["Blue","Red","Orange"]
        let questionState = QuestionState(multipleChoiceQuestion, answerResult: AnswerResultObject(identifier: "multipleChoice", value: .array(initialChoices)))
        let viewModel = ChoiceQuestionViewModel()
        viewModel.initialize(questionState)
        
        // Check initial state
        XCTAssertEqual(6, viewModel.choices.count)
        XCTAssertNotNil(viewModel.otherChoice)
        XCTAssertTrue(questionState.hasSelectedAnswer)
        XCTAssertEqual(.array(initialChoices), questionState.answerResult.jsonValue)
        viewModel.choices.forEach { choice in
            if initialChoices.contains(choice.id) {
                XCTAssertTrue(choice.selected, "Expected selection to be true for '\(choice.id)'")
            }
            else {
                XCTAssertFalse(choice.selected, "Expected selection to be false for '\(choice.id)'")
            }
        }
        
        if let other = viewModel.otherChoice {
            XCTAssertTrue(other.selected, "Expected selection to be true for '\(other.id)'")
            XCTAssertEqual("Orange", other.value)
        }
        else {
            XCTFail("Expected 'Other' to be non-nil")
        }
    }
    
    func testSingleChoiceQuestionSelection_InitialAnswerNull() {
        let singleChoiceQuestion = ChoiceQuestionStepObject(identifier: "singleChoice",
                                     choices: [
                                        "Blue",
                                        "Green",
                                        "Yellow",
                                        "Red",
                                        "Black and occasionally very, very dark gray",
                                        .init(text: "I don't have any", selectorType: .exclusive),
                                     ],
                                     baseType: .string,
                                     singleChoice: true,
                                     other: StringTextInputItemObject(),
                                     title: "What are your favorite colors?")
        
        let questionState = QuestionState(singleChoiceQuestion, answerResult: nil)
        let viewModel = ChoiceQuestionViewModel()
        viewModel.initialize(questionState)
        
        // Check initial state
        XCTAssertEqual(6, viewModel.choices.count)
        XCTAssertNotNil(viewModel.otherChoice)
        XCTAssertFalse(questionState.hasSelectedAnswer)
        viewModel.choices.forEach { choice in
            XCTAssertFalse(choice.selected, "Expected selection to be false for \(choice.id)")
        }
        
        // Select a choice
        viewModel.choices[2].selected = true
        XCTAssertEqual("Yellow", viewModel.choices[2].id)
        
        // Check new values
        XCTAssertTrue(questionState.hasSelectedAnswer)
        viewModel.choices.forEach { choice in
            if choice.id == "Yellow" {
                XCTAssertTrue(choice.selected, "Expected selection to be true for \(choice.id)")
            } else {
                XCTAssertFalse(choice.selected, "Expected selection to be false for \(choice.id)")
            }
        }
        XCTAssertEqual(.string("Yellow"), questionState.answerResult.jsonValue)
        
        // Select other - this will not automatically update the answer b/c selection happens before change of value.
        viewModel.otherChoice?.selected = true
        viewModel.otherChoice?.value = "Purple"
        viewModel.updateAnswer()
        
        // Check new values
        XCTAssertTrue(questionState.hasSelectedAnswer)
        XCTAssertEqual(.string("Purple"), questionState.answerResult.jsonValue)
        
        // Deselect other
        viewModel.otherChoice?.selected = false
        
        // Check new values
        XCTAssertFalse(questionState.hasSelectedAnswer)
        XCTAssertNil(questionState.answerResult.jsonValue)
        
        // Select answer and other
        viewModel.choices[2].selected = true
        
        // Check new values
        XCTAssertTrue(questionState.hasSelectedAnswer)
        XCTAssertEqual(.string("Yellow"), questionState.answerResult.jsonValue)
        
        // Finally select "none of the above"
        viewModel.choices[5].selected = true
        
        // Check new values
        XCTAssertTrue(questionState.hasSelectedAnswer)
        XCTAssertEqual(.null, questionState.answerResult.jsonValue)
    }
    
    func testSingleChoiceQuestionSelection_InitialAnswerNone() {
        let singleChoiceQuestion = ChoiceQuestionStepObject(identifier: "singleChoice",
                                     choices: [
                                        "Blue",
                                        "Green",
                                        "Yellow",
                                        "Red",
                                        "Black and occasionally very, very dark gray",
                                        .init(text: "I don't have any", selectorType: .exclusive),
                                     ],
                                     baseType: .string,
                                     singleChoice: true,
                                     other: StringTextInputItemObject(),
                                     title: "What are your favorite colors?")
        
        let questionState = QuestionState(singleChoiceQuestion, answerResult: AnswerResultObject(identifier: "singleChoice", value: .null))
        let viewModel = ChoiceQuestionViewModel()
        viewModel.initialize(questionState)
        
        // Check initial state
        XCTAssertEqual(6, viewModel.choices.count)
        XCTAssertNotNil(viewModel.otherChoice)
        XCTAssertTrue(questionState.hasSelectedAnswer)
        XCTAssertEqual(.null, questionState.answerResult.jsonValue)
        viewModel.choices.forEach { choice in
            if choice.id == "I don't have any" {
                XCTAssertTrue(choice.selected, "Expected selection to be true for '\(choice.id)'")
            }
            else {
                XCTAssertFalse(choice.selected, "Expected selection to be false for '\(choice.id)'")
            }
        }
    }
    
    func testSingleChoiceQuestionSelection_InitialAnswerOther() {
        let singleChoiceQuestion = ChoiceQuestionStepObject(identifier: "singleChoice",
                                     choices: [
                                        "Blue",
                                        "Green",
                                        "Yellow",
                                        "Red",
                                        "Black and occasionally very, very dark gray",
                                        .init(text: "I don't have any", selectorType: .exclusive),
                                     ],
                                     baseType: .string,
                                     singleChoice: true,
                                     other: StringTextInputItemObject(),
                                     title: "What are your favorite colors?")
        
        let initialChoice = "Orange"
        let questionState = QuestionState(singleChoiceQuestion, answerResult: AnswerResultObject(identifier: "singleChoice", value: .string(initialChoice)))
        let viewModel = ChoiceQuestionViewModel()
        viewModel.initialize(questionState)
        
        // Check initial state
        XCTAssertEqual(6, viewModel.choices.count)
        XCTAssertNotNil(viewModel.otherChoice)
        XCTAssertTrue(questionState.hasSelectedAnswer)
        XCTAssertEqual(.string(initialChoice), questionState.answerResult.jsonValue)
        viewModel.choices.forEach { choice in
            XCTAssertFalse(choice.selected, "Expected selection to be false for '\(choice.id)'")
        }
        
        if let other = viewModel.otherChoice {
            XCTAssertTrue(other.selected, "Expected selection to be true for '\(other.id)'")
            XCTAssertEqual("Orange", other.value)
        }
        else {
            XCTFail("Expected 'Other' to be non-nil")
        }
    }
    
    func testSingleChoiceQuestionSelection_InitialAnswerGreen() {
        let singleChoiceQuestion = ChoiceQuestionStepObject(identifier: "singleChoice",
                                     choices: [
                                        "Blue",
                                        "Green",
                                        "Yellow",
                                        "Red",
                                        "Black and occasionally very, very dark gray",
                                        .init(text: "I don't have any", selectorType: .exclusive),
                                     ],
                                     baseType: .string,
                                     singleChoice: true,
                                     other: StringTextInputItemObject(),
                                     title: "What are your favorite colors?")
        
        let initialChoice = "Green"
        let questionState = QuestionState(singleChoiceQuestion, answerResult: AnswerResultObject(identifier: "singleChoice", value: .string(initialChoice)))
        let viewModel = ChoiceQuestionViewModel()
        viewModel.initialize(questionState)
        
        // Check initial state
        XCTAssertEqual(6, viewModel.choices.count)
        XCTAssertNotNil(viewModel.otherChoice)
        XCTAssertTrue(questionState.hasSelectedAnswer)
        XCTAssertEqual(.string(initialChoice), questionState.answerResult.jsonValue)
        viewModel.choices.forEach { choice in
            if choice.id == initialChoice {
                XCTAssertTrue(choice.selected, "Expected selection to be true for '\(choice.id)'")
            }
            else {
                XCTAssertFalse(choice.selected, "Expected selection to be false for '\(choice.id)'")
            }
        }
        
        if let other = viewModel.otherChoice {
            XCTAssertFalse(other.selected, "Expected selection to be false for '\(other.id)'")
            XCTAssertEqual("", other.value)
        }
        else {
            XCTFail("Expected 'Other' to be non-nil")
        }
    }
}
