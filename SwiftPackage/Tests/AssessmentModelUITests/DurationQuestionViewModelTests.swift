//
//  DurationQuestionViewModelTests.swift
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
import XCTest

class DurationQuestionViewModelTests: XCTestCase {
    
    override func setUp() {
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }

    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }
    
    func testDurationQuestion_MinSec_Initial() {
        let example = SimpleQuestionStepObject(
            identifier: "example",
            inputItem: DurationTextInputItemObject(displayUnits: [.minute, .second]),
            title: "How long does it take to beam up to the Starship Enterprise?"
        )
        
        let questionState = QuestionState(example, answerResult: nil)
        let questionViewModel = DurationQuestionViewModel()
        questionViewModel.onAppear(questionState)
        
        XCTAssertEqual(2, questionViewModel.inputFields.count)
        guard let minuteField = questionViewModel.inputFields.first,
              let secondField = questionViewModel.inputFields.last
        else {
            XCTFail("Failed to initialize the input fields")
            return
        }

        XCTAssertEqual(0, minuteField.minValue)
        XCTAssertEqual(60, minuteField.maxValue)
        XCTAssertNil(minuteField.value)
        XCTAssertEqual(DurationUnit.minute.rawValue, minuteField.id)
        
        XCTAssertEqual(0, secondField.minValue)
        XCTAssertEqual(60, secondField.maxValue)
        XCTAssertNil(secondField.value)
        XCTAssertEqual(DurationUnit.second.rawValue, secondField.id)
        
        minuteField.value = 2
        XCTAssertEqual(.number(120), questionState.answerResult.jsonValue)
        XCTAssertTrue(questionState.hasSelectedAnswer)
        
        secondField.value = 1
        XCTAssertEqual(.number(121), questionState.answerResult.jsonValue)
        XCTAssertTrue(questionState.hasSelectedAnswer)
        
        minuteField.value = nil
        XCTAssertEqual(.number(1), questionState.answerResult.jsonValue)
        XCTAssertTrue(questionState.hasSelectedAnswer)
        
        secondField.value = nil
        XCTAssertEqual(.number(0), questionState.answerResult.jsonValue)
        XCTAssertTrue(questionState.hasSelectedAnswer)
    }
    
    func testDurationQuestion_MinSec_WithValue() {
        let example = SimpleQuestionStepObject(
            identifier: "example",
            inputItem: DurationTextInputItemObject(displayUnits: [.minute, .second]),
            title: "How long does it take to beam up to the Starship Enterprise?"
        )
        
        let questionState = QuestionState(example, answerResult: nil)
        questionState.answer = .number(72)
        questionState.hasSelectedAnswer = true
        let questionViewModel = DurationQuestionViewModel()
        questionViewModel.onAppear(questionState)
        
        XCTAssertEqual(2, questionViewModel.inputFields.count)
        guard let minuteField = questionViewModel.inputFields.first,
              let secondField = questionViewModel.inputFields.last
        else {
            XCTFail("Failed to initialize the input fields")
            return
        }
        
        XCTAssertEqual(0, minuteField.minValue)
        XCTAssertEqual(60, minuteField.maxValue)
        XCTAssertEqual(1, minuteField.value)
        XCTAssertEqual(DurationUnit.minute.rawValue, minuteField.id)
        
        XCTAssertEqual(0, secondField.minValue)
        XCTAssertEqual(60, secondField.maxValue)
        XCTAssertEqual(12, secondField.value)
        XCTAssertEqual(DurationUnit.second.rawValue, secondField.id)
    }
    
    
    func testDurationQuestion_Initial() {
        let example = SimpleQuestionStepObject(
            identifier: "example",
            inputItem: DurationTextInputItemObject(displayUnits: [.hour, .minute]),
            title: "How long does it take to fly to Spain?"
        )
        
        let questionState = QuestionState(example, answerResult: nil)
        let questionViewModel = DurationQuestionViewModel()
        questionViewModel.onAppear(questionState)
        
        XCTAssertEqual(2, questionViewModel.inputFields.count)
        guard let hourField = questionViewModel.inputFields.first,
              let minuteField = questionViewModel.inputFields.last
        else {
            XCTFail("Failed to initialize the input fields")
            return
        }
        
        XCTAssertEqual(0, hourField.minValue)
        XCTAssertEqual(24, hourField.maxValue)
        XCTAssertNil(hourField.value)
        XCTAssertEqual(DurationUnit.hour.rawValue, hourField.id)

        XCTAssertEqual(0, minuteField.minValue)
        XCTAssertEqual(60, minuteField.maxValue)
        XCTAssertNil(minuteField.value)
        XCTAssertEqual(DurationUnit.minute.rawValue, minuteField.id)
        
        minuteField.value = 2
        XCTAssertEqual(.number(120), questionState.answerResult.jsonValue)
        XCTAssertTrue(questionState.hasSelectedAnswer)
        
        hourField.value = 1
        XCTAssertEqual(.number(3720), questionState.answerResult.jsonValue)
        XCTAssertTrue(questionState.hasSelectedAnswer)
        
        minuteField.value = nil
        XCTAssertEqual(.number(3600), questionState.answerResult.jsonValue)
        XCTAssertTrue(questionState.hasSelectedAnswer)
        
        hourField.value = nil
        XCTAssertEqual(.number(0), questionState.answerResult.jsonValue)
        XCTAssertTrue(questionState.hasSelectedAnswer)
    }
    
    func testDurationQuestion_WithValue() {
        let example = SimpleQuestionStepObject(
            identifier: "example",
            inputItem: DurationTextInputItemObject(displayUnits: [.hour, .minute]),
            title: "How long does it take to fly to Spain?"
        )
        
        let questionState = QuestionState(example, answerResult: nil)
        questionState.answer = .number(14520)
        questionState.hasSelectedAnswer = true
        let questionViewModel = DurationQuestionViewModel()
        questionViewModel.onAppear(questionState)
        
        XCTAssertEqual(2, questionViewModel.inputFields.count)
        guard let hourField = questionViewModel.inputFields.first,
              let minuteField = questionViewModel.inputFields.last
        else {
            XCTFail("Failed to initialize the input fields")
            return
        }
        
        XCTAssertEqual(0, hourField.minValue)
        XCTAssertEqual(24, hourField.maxValue)
        XCTAssertEqual(4, hourField.value)
        XCTAssertEqual(DurationUnit.hour.rawValue, hourField.id)
        
        XCTAssertEqual(0, minuteField.minValue)
        XCTAssertEqual(60, minuteField.maxValue)
        XCTAssertEqual(2, minuteField.value)
        XCTAssertEqual(DurationUnit.minute.rawValue, minuteField.id)
    }
}
