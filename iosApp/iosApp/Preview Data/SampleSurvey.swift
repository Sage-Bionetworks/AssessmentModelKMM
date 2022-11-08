//
//  SampleSurvey.swift
//
//

import Foundation
import AssessmentModel

func previewSurveyBranchViewModel(_ initialIndex: Int) -> BranchViewModel {
    let nodeState = PreviewBranchNodeState(previewSurveyAssessment, initialIndex: initialIndex)
    return BranchViewModel(nodeState)
}

func previewSurveyQuestionState(_ index: Int) -> QuestionState {
    previewSurveyBranchViewModel(index).currentNodeState! as! QuestionState
}

let previewSurveyAssessment = AssessmentObject(identifier: "Sample Survey",
                                               children: previewSurveyNodes,
                                               progressMarkers: [])

let previewSurveyNodes: [ContentNode] = [
    SimpleQuestionObject(identifier: "enterText",
                         inputItem: StringTextInputItemObject(),
                         skipCheckbox: nil,
                         title: "Title: Enter Text",
                         subtitle: "Subtitle: Enter some text into the input field below.",
                         detail: "Detail: This can be anything you want.")
]
