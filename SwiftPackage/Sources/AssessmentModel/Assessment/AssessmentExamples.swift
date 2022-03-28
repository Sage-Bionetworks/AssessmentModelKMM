//
//  AssessmentExamples.swift
//  
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
import JsonModel



let surveyA = AssessmentObject(identifier: "surveyA",
                               children: surveyAChildren,
                               version: "1.0.0",
                               estimatedMinutes: 3,
                               copyright: "Copyright © 2022 Sage Bionetworks. All rights reserved.",
                               title: "Example Survey A",
                               detail: """
                                This is intended as an example of a survey with a list of questions. There are no
                                sections and there are no additional instructions. In this survey, pause navigation
                                is hidden for all nodes. For all questions, the skip button should say 'Skip me'.
                                Default behavior is that buttons that make logical sense to be displayed are shown
                                unless they are explicitly hidden.
                                """.replacingOccurrences(of: "\n", with: " "),
                               shouldHideButtons: [.navigation(.pause)],
                               buttonMap: [.navigation(.skip): ButtonActionInfoObject(buttonTitle: "Skip me")])
fileprivate let surveyAChildren: [Node] = [
    OverviewStepObject(identifier: "overview", title: "Example Survey A", detail: "You will be shown a series of example questions. This survey has no additional instructions."),
    
    ChoiceQuestionStepObject(identifier: "choiceQ1",
                             choices: [
                                .init(value: .integer(1), text: "Enter some text"),
                                .init(value: .integer(2), text: "Birth year"),
                                .init(value: .integer(3), text: "Likert Scale"),
                                .init(value: .integer(3), text: "Decimal Scale"),
                             ],
                             baseType: .integer,
                             singleChoice: true,
                             title: "Choose which question to answer",
                             surveyRules: [
                                .init(skipToIdentifier: "followupQ"),
                                .init(skipToIdentifier: "simpleQ1", matchingValue: .integer(1)),
                                .init(skipToIdentifier: "simpleQ2", matchingValue: .integer(2)),
                                .init(skipToIdentifier: "simpleQ3", matchingValue: .integer(3)),
                                .init(skipToIdentifier: "simpleQ4", matchingValue: .integer(4)),
                             ],
                             comment: "Go to the question selected by the participant. If they skip the question then go directly to follow-up."),
    
    SimpleQuestionStepObject(identifier: "simpleQ1",
                             inputItem: StringTextInputItemObject(placeholder: "I like cake"),
                             title: "Enter some text",
                             nextNode: "followupQ"),
    SimpleQuestionStepObject(identifier: "simpleQ2",
                             inputItem: YearTextInputItemObject(placeholder: "1948", formatOptions: .birthYear),
                             title: "Enter a birth year",
                             nextNode: "followupQ"),
    SimpleQuestionStepObject(identifier: "simpleQ3",
                             inputItem: IntegerTextInputItemObject(formatOptions: .init(minimumValue: 1, maximumValue: 5, minimumLabel: "Not at all", maximumLabel: "Very much")),
                             title: "How much do you like apples on a scale of 1 to 5?",
                             uiHint: .NumberField.likert.uiHint,
                             nextNode: "followupQ"),
    SimpleQuestionStepObject(identifier: "simpleQ4",
                             inputItem: DoubleTextInputItemObject(formatOptions: .init(minimumValue: 0, maximumValue: 1, minimumLabel: "Not at all", maximumLabel: "Very much")),
                             title: "How much do you like apples as a number between 0 and 1?",
                             uiHint: .NumberField.slider.uiHint,
                             nextNode: "followupQ"),
    
    ChoiceQuestionStepObject(identifier: "followupQ", choices: .booleanChoices(), title: "Are you happy with your choice?",
                             surveyRules: [ .init(skipToIdentifier: "choiceQ1", matchingValue: .boolean(false)) ],
                             comment: "If the participant selects 'No' then go to 'choiceQ1'"),
    
    ChoiceQuestionStepObject(identifier: "multipleChoice",
                             choices: [
                                "blue",
                                "red",
                                "green",
                                "yellow",
                                .init(text: "All of the above", selectorType: .all),
                                .init(text: "I don't have any", selectorType: .exclusive),
                             ],
                             baseType: .string,
                             singleChoice: false,
                             other: StringTextInputItemObject(),
                             title: "What are your favorite colors?",
                             detail: "Choose all that apply"),
    
    ChoiceQuestionStepObject(identifier: "favoriteFood",
                             choices: [
                                "pizza",
                                "sushi",
                                "ice cream",
                             ],
                             baseType: .string,
                             singleChoice: true,
                             other: StringTextInputItemObject(),
                             title: "What are you having for dinner?",
                             surveyRules: [
                                .init(skipToIdentifier: "completion", matchingValue: .string("pizza"), ruleOperator: .notEqual)
                             ]),
    
    InstructionStepObject(identifier: "pizza", title: "Mmmmm, pizza..."),
    
    CompletionStepObject(identifier: "completion", title: "You're done!")
]
