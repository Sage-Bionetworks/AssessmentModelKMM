//
//  InstructionStepView.swift
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
