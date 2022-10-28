//
//  AssessmentView.swift
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

import SwiftUI
import SharedMobileUI
import AssessmentModel
import JsonModel
import ResultModel

/// This protocol extends the model and views that are used to display an assessment using views that are not
/// defined within this library.
public protocol AssessmentDisplayView : View {
    init(_ assessmentState: AssessmentState)

    /// Unpack and load the assessment state from the given config data and restored data.
    /// - Parameters:
    ///     - config: The JSON (as Data) that is used to configure this assessment.
    ///     - restoredResult: The partial result (if any) that is restored for this assessment.
    ///     - interruptionHandling: The interruption handling to use for this assessment (if defined).
    /// - Returns: Instantiated assessment state observable object.
    static func instantiateAssessmentState(_ identifier: String, config: Data?, restoredResult: Data?, interruptionHandling: InterruptionHandling?) throws -> AssessmentState
}

extension AssessmentView : AssessmentDisplayView {
    
    public static func instantiateAssessmentState(_ identifier: String, config: Data?, restoredResult: Data?, interruptionHandling: InterruptionHandling?) throws -> AssessmentState {
        guard let data = config else {
            throw DecodingError.dataCorrupted(.init(codingPath: [], debugDescription: "Cannot decode a survey with NULL config."))
        }
        let decoder = AssessmentFactory().createJSONDecoder()
        let assessment = try decoder.decode(AssessmentObject.self, from: data)
        let restoredResult = try restoredResult.map {
            try decoder.decode(AssessmentResultObject.self, from: $0)
        }
        return .init(assessment, restoredResult: restoredResult, interruptionHandling: interruptionHandling)
    }
}

/// Displays an assessment built using the views and model objects defined within this library.
public struct AssessmentView : View {
    @StateObject var viewModel: AssessmentViewModel = .init()
    @ObservedObject var assessmentState: AssessmentState
    
    public init(_ assessmentState: AssessmentState) {
        self.assessmentState = assessmentState
    }
    
    public var body: some View {
        AssessmentWrapperView<StepView>(assessmentState, viewModel: viewModel)
    }
    
    struct StepView : View, StepFactoryView {
        @ObservedObject var state: StepState
        
        init(_ state: StepState) {
            self.state = state
        }
        
        var body: some View {
            if let questionState = state as? QuestionState {
                if questionState.step is ChoiceQuestionStep {
                    ChoiceQuestionStepView(questionState)
                }
                else if let question = questionState.question as? SimpleQuestion {
                    switch question.inputItem {
                    case is IntegerTextInputItem:
                        IntegerQuestionStepView(questionState)
                    case is StringTextInputItem:
                        TextEntryQuestionStepView(questionState)
                    case is DurationTextInputItem:
                        DurationQuestionStepView(questionState)
                    case is TimeTextInputItem:
                        TimeQuestionStepView(questionState)
                    default:
                        debugQuestionStepView(questionState)
                    }
                }
                else {
                    debugQuestionStepView(questionState)
                }
            }
            else if let step = state.step as? OverviewStep {
                TitlePageView(step)
            }
            else if let step = state.step as? CompletionStep {
                CompletionStepView(step)
            }
            else if state.step is CountdownStep {
                CountdownStepView(state)
            }
            else if let nodeState = state as? ContentNodeState {
                InstructionStepView(nodeState)
            }
            else {
                debugStepView()
            }
        }
    
        @ViewBuilder
        private func debugQuestionStepView(_ state: QuestionState) -> some View {
            VStack(spacing: 8) {
                StepHeaderView(state)
                Spacer()
                Text(state.id)
                Spacer()
                SurveyNavigationView()
            }
            .id("DebugQuestionStepView:\(state.id)")   // Give the view a unique id to force refresh
            .environmentObject(state)
            .fullscreenBackground(.hexE5E5E5)
            .onAppear {
                state.hasSelectedAnswer = true
            }
        }
        
        @ViewBuilder
        private func debugStepView() -> some View {
            VStack {
                Spacer()
                Text(state.id)
                Spacer()
                SurveyNavigationView()
            }
        }
    }
}

public protocol StepFactoryView : View {
    init(_ state: StepState)
}

public struct AssessmentWrapperView<StepContent : StepFactoryView> : View {
    @ObservedObject var viewModel: AssessmentViewModel
    @ObservedObject var assessmentState: AssessmentState
    
    public init(_ assessmentState: AssessmentState, viewModel: AssessmentViewModel) {
        self.assessmentState = assessmentState
        self.viewModel = viewModel
    }
    
    public var body: some View {
        ZStack(alignment: .top) {
            if let state = assessmentState.currentStep {
                StepContent(state)
                    .id("Step:\(state.id)") // Setting the id will refresh the view.
            }
            else {
                ProgressView()
            }
            TopBarProgressView()
            PauseMenu(viewModel: viewModel)
                .opacity(assessmentState.showingPauseActions ? 1 : 0)
                .animation(.easeInOut, value: assessmentState.showingPauseActions)
        }
        .environmentObject(assessmentState)
        .environmentObject(viewModel.navigationViewModel)
        .onAppear {
            viewModel.initialize(assessmentState)
        }
    }
}

struct AssessmentView_Previews: PreviewProvider {
    static var previews: some View {
        AssessmentView(.init(surveyA))
    }
}

public var previewExamples: [AssessmentObject] = [
    surveyA,
    surveyB,
    surveyC,
]

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
                                """.replacingOccurrences(of: "\n", with: " "))
fileprivate let surveyAChildren: [Node] = [
    OverviewStepObject(identifier: "overview",
                       title: "Example Survey A",
                       detail: "You will be shown a series of example questions. This survey has no additional instructions.", imageInfo: SageResourceImage(.default)),
    
    ChoiceQuestionStepObject(identifier: "choiceQ1",
                             choices: [
                                .init(value: .integer(1), text: "Enter some text"),
                                .init(value: .integer(2), text: "Birth year"),
                                .init(value: .integer(3), text: "Likert Scale"),
                                .init(value: .integer(4), text: "Sliding Scale"),
                                .init(value: .integer(5), text: "Duration"),
                                .init(value: .integer(6), text: "Time"),
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
                                .init(skipToIdentifier: "simpleQ5", matchingValue: .integer(5)),
                                .init(skipToIdentifier: "simpleQ6", matchingValue: .integer(6)),
                             ],
                             comment: "Go to the question selected by the participant. If they skip the question then go directly to follow-up."),
    
    textEntryExample,
    birthYearExample,
    likertExample,
    slidingScaleExample,
    durationExample,
    timeExample,
    
    happyChoiceQuestion,
    favoriteFoodChoiceQuestion,
    InstructionStepObject(identifier: "pizza", title: "Mmmmm, pizza..."),
    favoriteColorsQuestion,
    
    CompletionStepObject(identifier: "completion", title: "You're done!")
]

let surveyB = AssessmentObject(identifier: "surveyB",
                               children: surveyBChildren,
                               title: "Example Survey B")
fileprivate let surveyBChildren: [Node] = [
    OverviewStepObject(identifier: "overview", title: "Example Survey B", detail: "This survey has questions presented in sections."),
    SectionObject(identifier: "colors", children: sectionB1Children),
    SectionObject(identifier: "foods", children: sectionB2Children),
    CompletionStepObject(identifier: "completion", title: "You're done!")
]

fileprivate let sectionB1Children: [Node] = [
    ChoiceQuestionStepObject(identifier: "choice1",
                             choices: [
                                "Blue",
                                "Green",
                                "Yellow",
                                "Red",
                             ],
                             baseType: .string,
                             title: "Pick a color"),
    ChoiceQuestionStepObject(identifier: "choice2",
                             choices: [
                                "Blue",
                                "Green",
                                "Yellow",
                                "Red",
                             ],
                             baseType: .string,
                             title: "Pick a different color")
]

fileprivate let sectionB2Children: [Node] = [
    ChoiceQuestionStepObject(identifier: "choice1",
                             choices: [
                                "Pizza",
                                "Hamburger",
                                "Ice Cream",
                                "Tofu Tacos",
                             ],
                             baseType: .string,
                             title: "Pick a food"),
    ChoiceQuestionStepObject(identifier: "choice2",
                             choices: [
                                "Pizza",
                                "Hamburger",
                                "Ice Cream",
                                "Tofu Tacos",
                             ],
                             baseType: .string,
                             title: "Pick a different food")
]


let surveyCJson = """
{
  "identifier": "daily",
  "type": "assessment",
  "interruptionHandling": {
    "reviewIdentifier": null
  },
  "steps": [
    {
      "type": "simpleQuestion",
      "identifier": "energy",
      "title": "Please rate your energy level right now.",
      "detail": "0 being very low, 5 being your average, and 10 being very high",
      "uiHint": "slider",
      "inputItem": {
        "type": "integer",
        "formatOptions": {
          "maximumLabel": "Very high",
          "maximumValue": 10,
          "minimumLabel": "Very low",
          "minimumValue": 0
        }
      }
    },
    {
      "type": "simpleQuestion",
      "identifier": "mood",
      "title": "Please rate your mood right now.",
      "detail": "0 being very low, 5 being your average, and 10 being very high",
      "uiHint": "slider",
      "inputItem": {
        "type": "integer",
        "formatOptions": {
          "maximumLabel": "Very high",
          "maximumValue": 10,
          "minimumLabel": "Very low",
          "minimumValue": 0
        }
      }
    },
    {
      "type": "simpleQuestion",
      "identifier": "thoughts",
      "title": "How fast are your thoughts right now?",
      "detail": "0 being very slow, 5 being your average, and 10 being very fast",
      "uiHint": "slider",
      "inputItem": {
        "type": "integer",
        "formatOptions": {
          "maximumLabel": "Very fast",
          "maximumValue": 10,
          "minimumLabel": "Very slow",
          "minimumValue": 0
        }
      }
    },
    {
      "type": "simpleQuestion",
      "identifier": "impulsiveness",
      "title": "How impulsive are you feeling right now?",
      "detail": "0 being not at all, 5 being your average, and 10 being very much",
      "uiHint": "slider",
      "inputItem": {
        "type": "integer",
        "formatOptions": {
          "maximumLabel": "Very much",
          "maximumValue": 10,
          "minimumLabel": "Not at all",
          "minimumValue": 0
        }
      }
    },
    {
      "type": "simpleQuestion",
      "identifier": "attention",
      "title": "How well are you able to focus right now?",
      "detail": "0 being not at all, 5 being your average, and 10 being very well",
      "uiHint": "slider",
      "inputItem": {
        "type": "integer",
        "formatOptions": {
          "maximumLabel": "Very well",
          "maximumValue": 10,
          "minimumLabel": "Not at all",
          "minimumValue": 0
        }
      },
      "actions": {
        "goForward": {
          "buttonTitle": "Submit",
          "type": "default"
        }
      }
    }
  ]
}
""".data(using: .utf8)!

let surveyC = try! AssessmentFactory().createJSONDecoder().decode(AssessmentObject.self, from: surveyCJson)
