//
//  InstructionStepView.swift
//
//

import SwiftUI
import AssessmentModel
import SharedMobileUI

public struct InstructionStepView: View {
    @ObservedObject var nodeState: ContentNodeState
    let alignment: Alignment
    
    public init(_ nodeState: ContentNodeState, alignment: Alignment = .leading) {
        self.nodeState = nodeState
        self.alignment = alignment
    }
    
    public var body: some View {
        VStack {
            StepHeaderView(nodeState)
            ContentNodeView(nodeState.contentNode, alignment: alignment)
            SurveyNavigationView()
        }
    }
}

struct InstructionView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            InstructionStepView(InstructionState(example2, parentId: nil))
                .environmentObject(PagedNavigationViewModel(pageCount: 5, currentIndex: 0))
            .environmentObject(AssessmentState(AssessmentObject(previewStep: example2)))
            InstructionStepView(InstructionState(example1, parentId: nil), alignment: .center)
                .environmentObject(PagedNavigationViewModel(pageCount: 5, currentIndex: 0))
                .environmentObject(AssessmentState(AssessmentObject(previewStep: example1)))
        }
    }
}

fileprivate let example1 = InstructionStepObject(
    identifier: "example",
    title: "Example Survey A",
    detail: "You will be shown a series of example questions. This survey has no additional instructions.",
    imageInfo: SageResourceImage(.default))

fileprivate let example2 = InstructionStepObject(
    identifier: "example",
    title: "Example Survey A",
    detail: "You will be shown a series of example questions. This survey has no additional instructions.",
    imageInfo: FetchableImage(imageName: "survey.1", bundle: Bundle.module, placementHint: "iconAfter"))
