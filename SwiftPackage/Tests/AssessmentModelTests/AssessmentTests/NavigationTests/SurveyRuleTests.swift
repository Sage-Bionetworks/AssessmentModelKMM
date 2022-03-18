//
//  SurveyRuleTests.swift
//
//  Copyright © 2017-2022 Sage Bionetworks. All rights reserved.
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

import XCTest
import JsonModel
@testable import AssessmentModel

class SurveyRuleTests: XCTestCase {
    
    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }

    // Integer

    func testSurveyRule_Integer_Equal() {

        let inputItem = IntegerTextInputItemObject()
        let surveyRules = [
            createRule("lessThan", .integer(1), .lessThan),
            createRule("equal", .integer(2), .equal),
            createRule("greaterThan", .integer(3), .greaterThan)
        ]
        
        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .integer(2))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertEqual(navigatingIdentifier, "equal")
    }

    func testSurveyRule_Integer_Equal_Default() {

        let rule = createRule("equal", .integer(3))

        let inputItem = IntegerTextInputItemObject()
        let surveyRules = [ rule ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .integer(3))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertEqual(navigatingIdentifier, "equal")
    }

    func testSurveyRule_Integer_Skip_Default() {

        let rule = createRule("skip")

        let inputItem = IntegerTextInputItemObject()
        let surveyRules = [ rule ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .null)

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertEqual(navigatingIdentifier, "skip")
    }

    func testSurveyRule_Integer_LessThan() {

        let inputItem = IntegerTextInputItemObject()
        let surveyRules = [
            createRule("lessThan", .integer(1), .lessThan),
            createRule("equal", .integer(2), .equal),
            createRule("greaterThan", .integer(3), .greaterThan)
        ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .integer(0))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertEqual(navigatingIdentifier, "lessThan")
    }

    func testSurveyRule_Integer_GreaterThan() {

        let inputItem = IntegerTextInputItemObject()
        let surveyRules = [
            createRule("lessThan", .integer(1), .lessThan),
            createRule("equal", .integer(2), .equal),
            createRule("greaterThan", .integer(3), .greaterThan)
        ]
        
        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .integer(4))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertEqual(navigatingIdentifier, "greaterThan")
    }

    func testSurveyRule_Integer_NotGreaterThan() {

        let inputItem = IntegerTextInputItemObject()
        let surveyRules = [
            createRule("lessThan", .integer(1), .lessThan),
            createRule("equal", .integer(2), .equal),
            createRule("greaterThan", .integer(3), .greaterThan)
        ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .integer(3))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertNil(navigatingIdentifier)
    }

    func testSurveyRule_Integer_NotLessThan() {

        let inputItem = IntegerTextInputItemObject()
        let surveyRules = [
            createRule("lessThan", .integer(1), .lessThan),
            createRule("equal", .integer(2), .equal),
            createRule("greaterThan", .integer(3), .greaterThan)
        ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .integer(1))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertNil(navigatingIdentifier)
    }

    func testSurveyRule_Integer_LessThanOrEqual() {

        let inputItem = IntegerTextInputItemObject()
        let surveyRules = [
            createRule("lessThanEqual", .integer(1), .lessThanEqual),
            createRule("equal", .integer(2), .equal),
            createRule("greaterThanEqual", .integer(3), .greaterThanEqual)
        ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .integer(1))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertEqual(navigatingIdentifier, "lessThanEqual")
    }

    func testSurveyRule_Integer_NotEqual_True() {

        let inputItem = IntegerTextInputItemObject()
        let surveyRules = [
            createRule("notEqual", .integer(2), .notEqual),
        ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .integer(3))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertEqual(navigatingIdentifier, "notEqual")
    }

    func testSurveyRule_Integer_NotEqual_False() {

        let inputItem = IntegerTextInputItemObject()
        let surveyRules = [
            createRule("notEqual", .integer(2), .notEqual),
        ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .integer(2))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertNil(navigatingIdentifier)
    }

    // Decimal

    func testSurveyRule_Decimal_Equal() {

        let inputItem = DoubleTextInputItemObject()
        let surveyRules = [
            createRule("lessThan", .number(1.0), .lessThan),
            createRule("equal", .number(2.0), .equal),
            createRule("greaterThan", .number(3.0), .greaterThan)
        ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .number(2.0000000000001))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertEqual(navigatingIdentifier, "equal")
    }
    
    func testSurveyRule_Decimal_Equal_MatchInt() {

        let inputItem = DoubleTextInputItemObject()
        let surveyRules = [
            createRule("lessThan", .integer(1), .lessThan),
            createRule("equal", .integer(2), .equal),
            createRule("greaterThan", .integer(3), .greaterThan)
        ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .number(2.0000000000001))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertEqual(navigatingIdentifier, "equal")
    }
    
    func testSurveyRule_Decimal_Equal_WithSignificantDigits() {

        var formatOptions = DoubleFormatOptions()
        formatOptions.maximumFractionDigits = 3
        let inputItem = DoubleTextInputItemObject(formatOptions: formatOptions)
        let surveyRules = [
            createRule("lessThan", .integer(1), .lessThan),
            createRule("equal", .integer(2), .equal),
            createRule("greaterThan", .integer(3), .greaterThan)
        ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .number(2.0001))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertEqual(navigatingIdentifier, "equal")
    }
    
    func testSurveyRule_Decimal_Equal_WithSignificantDigits_False() {

        var formatOptions = DoubleFormatOptions()
        formatOptions.maximumFractionDigits = 3
        let inputItem = DoubleTextInputItemObject(formatOptions: formatOptions)
        let surveyRules = [
            createRule("lessThan", .integer(1), .lessThan),
            createRule("equal", .integer(2), .equal),
            createRule("greaterThan", .integer(3), .greaterThan)
        ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .number(2.001))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertNil(navigatingIdentifier)
    }
    
    func testSurveyRule_Decimal_Equal_AnswerInt() {

        let inputItem = DoubleTextInputItemObject()
        let surveyRules = [
            createRule("lessThan", .number(1.0), .lessThan),
            createRule("equal", .number(2.0), .equal),
            createRule("greaterThan", .number(3.0), .greaterThan)
        ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .integer(2))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertEqual(navigatingIdentifier, "equal")
    }

    func testSurveyRule_Decimal_LessThan() {

        let inputItem = DoubleTextInputItemObject()
        let surveyRules = [
            createRule("lessThan", .number(1.0), .lessThan),
            createRule("equal", .number(2.0), .equal),
            createRule("greaterThan", .number(3.0), .greaterThan)
        ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .number(0.0))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertEqual(navigatingIdentifier, "lessThan")
    }

    func testSurveyRule_Decimal_GreaterThan() {

        let inputItem = DoubleTextInputItemObject()
        let surveyRules = [
             createRule("lessThan", .number(1.0), .lessThan),
             createRule("equal", .number(2.0), .equal),
             createRule("greaterThan", .number(3.0), .greaterThan)
        ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .number(4.0))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertEqual(navigatingIdentifier, "greaterThan")
    }

    func testSurveyRule_Decimal_NotGreaterThan() {

        let inputItem = DoubleTextInputItemObject()
        let surveyRules = [
             createRule("lessThan", .number(1.0), .lessThan),
             createRule("equal", .number(2.0), .equal),
             createRule("greaterThan", .number(3.0), .greaterThan)
        ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .number(3.0))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertNil(navigatingIdentifier)
    }

    func testSurveyRule_Decimal_NotLessThan() {

        let inputItem = DoubleTextInputItemObject()
        let surveyRules = [
             createRule("lessThan", .number(1.0), .lessThan),
             createRule("equal", .number(2.0), .equal),
             createRule("greaterThan", .number(3.0), .greaterThan)
        ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .number(1.0))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertNil(navigatingIdentifier)
    }

    func testSurveyRule_Decimal_LessThanOrEqual() {

        let inputItem = DoubleTextInputItemObject()
        let surveyRules = [
             createRule("lessThanEqual", .number(1.0), .lessThanEqual),
             createRule("equal", .number(2.0), .equal),
             createRule("greaterThanEqual", .number(3.0), .greaterThanEqual)
        ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .number(1.0))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertEqual(navigatingIdentifier, "lessThanEqual")
    }

    func testSurveyRule_Decimal_NotEqual_True() {

        let inputItem = DoubleTextInputItemObject()
        let surveyRules = [
             createRule("notEqual", .number(2.0), .notEqual),
        ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .number(3.0))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertEqual(navigatingIdentifier, "notEqual")
    }

    func testSurveyRule_Decimal_NotEqual_False() {

        let inputItem = DoubleTextInputItemObject()
        let surveyRules = [
             createRule("notEqual", .number(2.0), .notEqual),
        ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .number(2.0))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertNil(navigatingIdentifier)
    }

    // String

    func testSurveyRule_String_Equal() {

        let inputItem = StringTextInputItemObject()
        let surveyRules = [
             createRule("lessThan", .string("beta"), .lessThan),
             createRule("equal", .string("charlie"), .equal),
             createRule("greaterThan", .string("delta"), .greaterThan)
        ]
        
        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .string("charlie"))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertEqual("equal", navigatingIdentifier)
    }

    func testSurveyRule_String_LessThan() {

        let inputItem = StringTextInputItemObject()
        let surveyRules = [
             createRule("lessThan", .string("beta"), .lessThan),
             createRule("equal", .string("charlie"), .equal),
             createRule("greaterThan", .string("delta"), .greaterThan)
        ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .string("alpha"))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertEqual("lessThan", navigatingIdentifier)
    }

    func testSurveyRule_String_GreaterThan() {

        let inputItem = StringTextInputItemObject()
        let surveyRules = [
             createRule("lessThan", .string("beta"), .lessThan),
             createRule("equal", .string("charlie"), .equal),
             createRule("greaterThan", .string("delta"), .greaterThan)
        ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .string("gamma"))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertEqual("greaterThan", navigatingIdentifier)
    }

    func testSurveyRule_String_NotGreaterThan() {

        let inputItem = StringTextInputItemObject()
        let surveyRules = [
             createRule("lessThan", .string("beta"), .lessThan),
             createRule("equal", .string("charlie"), .equal),
             createRule("greaterThan", .string("delta"), .greaterThan)
        ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .string("delta"))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertNil(navigatingIdentifier)
    }

    func testSurveyRule_String_NotLessThan() {

        let inputItem = StringTextInputItemObject()
        let surveyRules = [
             createRule("lessThan", .string("beta"), .lessThan),
             createRule("equal", .string("charlie"), .equal),
             createRule("greaterThan", .string("delta"), .greaterThan)
        ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .string("beta"))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertNil(navigatingIdentifier)
    }

    func testSurveyRule_String_LessThanOrEqual() {

        let inputItem = StringTextInputItemObject()
        let surveyRules = [
             createRule("lessThanEqual", .string("beta"), .lessThanEqual),
             createRule("equal", .string("charlie"), .equal),
             createRule("greaterThanEqual", .string("delta"), .greaterThanEqual)
        ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .string("beta"))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertEqual("lessThanEqual", navigatingIdentifier)
    }

    func testSurveyRule_String_NotEqual_True() {

        let inputItem = StringTextInputItemObject()
        let surveyRules = [
             createRule("notEqual", .string("charlie"), .notEqual),
        ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .string("hoover"))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertEqual(navigatingIdentifier, "notEqual")
    }

    func testSurveyRule_String_NotEqual_False() {

        let inputItem = StringTextInputItemObject()
        let surveyRules = [
             createRule("notEqual", .string("charlie"), .notEqual),
        ]

        let step = SimpleQuestionStepObject(identifier: "foo", inputItem: inputItem, surveyRules: surveyRules)

        let (assessmentResult, _) = createBranchResult(for: step, with: .string("charlie"))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertNil(navigatingIdentifier)
    }

    // Boolean

    func testSurveyRule_Boolean_Equal_True() {

        let step = ChoiceQuestionStepObject(identifier: "foo",
                                            choices: [
                                                JsonChoice(value: .boolean(true), text: "Yes"),
                                                JsonChoice(value: .boolean(false), text: "No")],
                                            surveyRules: [
                                                createRule("equal", .boolean(true), .equal),
                                            ])

        let (assessmentResult, _) = createBranchResult(for: step, with: .boolean(true))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertEqual(navigatingIdentifier, "equal")
    }

    func testSurveyRule_Boolean_Equal_False() {

        let step = ChoiceQuestionStepObject(identifier: "foo",
                                            choices: [
                                                JsonChoice(value: .boolean(true), text: "Yes"),
                                                JsonChoice(value: .boolean(false), text: "No")],
                                            surveyRules: [
                                                createRule("equal", .boolean(true), .equal),
                                            ])

        let (assessmentResult, _) = createBranchResult(for: step, with: .boolean(false))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertNil(navigatingIdentifier)
    }

    func testSurveyRule_Boolean_NotEqual_True() {

        let step = ChoiceQuestionStepObject(identifier: "foo",
                                            choices: [
                                                JsonChoice(value: .boolean(true), text: "Yes"),
                                                JsonChoice(value: .boolean(false), text: "No")],
                                            surveyRules: [
                                                createRule("notEqual", .boolean(true), .notEqual)
                                            ])

        let (assessmentResult, _) = createBranchResult(for: step, with: .boolean(false))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertEqual(navigatingIdentifier, "notEqual")
    }

    func testSurveyRule_Boolean_NotEqual_False() {

        let step = ChoiceQuestionStepObject(identifier: "foo",
                                            choices: [
                                                JsonChoice(value: .boolean(true), text: "Yes"),
                                                JsonChoice(value: .boolean(false), text: "No")],
                                            surveyRules: [
                                                createRule("notEqual", .boolean(true), .notEqual)
                                            ])

        let (assessmentResult, _) = createBranchResult(for: step, with: .boolean(true))

        let peekingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: true)
        XCTAssertNil(peekingIdentifier)

        let navigatingIdentifier = step.nextNodeIdentifier(branchResult: assessmentResult, isPeeking: false)
        XCTAssertNil(navigatingIdentifier)
    }

    
    // Helper methods
    
    func createRule(_ skipIdentifier: NavigationIdentifier, _ matchingValue: JsonElement? = nil, _ ruleOperator: SurveyRuleOperator? = nil) -> JsonSurveyRuleObject {
        return JsonSurveyRuleObject(skipToIdentifier: skipIdentifier, matchingValue: matchingValue, ruleOperator: ruleOperator)
    }
    
    func createBranchResult(for step: QuestionStep, with jsonValue: JsonElement?) -> (AssessmentResultObject, AnswerResultObject) {
        let assessmentResult = AssessmentResultObject(identifier: "boobaloo")
        assessmentResult.appendStepHistory(with: ResultObject(identifier: "instruction1"))
        assessmentResult.appendStepHistory(with: ResultObject(identifier: "instruction2"))

        let answerResult = step.instantiateResult() as! AnswerResultObject
        assessmentResult.appendStepHistory(with: answerResult)
        answerResult.jsonValue = jsonValue

        return (assessmentResult, answerResult)
    }
}
