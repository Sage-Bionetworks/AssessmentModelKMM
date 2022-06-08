//
//  CompletionStepView.swift
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

public struct CompletionView : View {
    @SwiftUI.Environment(\.verticalPadding) var verticalPadding: CGFloat
    @SwiftUI.Environment(\.horizontalPadding) var horizontalPadding: CGFloat
    @State var imageHeight: CGFloat = 0.0
    
    let title: Text
    let detail: Text
    
    public init(title: Text, detail: Text) {
        self.title = title
        self.detail = detail
    }
    
    public var body: some View {
        ZStack(alignment: .top) {
            VStack(spacing: 0) {
                Color.clear
                    .frame(height: imageHeight * 2.0 / 3.0)
                VStack(spacing: verticalPadding) {
                    title
                        .font(.stepTitle)
                    detail
                        .font(.stepDetail)
                }
                .padding(.horizontal, horizontalPadding)
                .padding(.top, imageHeight / 3.0 + verticalPadding)
                .padding(.bottom, 32)
                .foregroundColor(.textForeground)
                .background(Color.sageWhite
                                .shadow(color: .hex2A2A2A.opacity(0.1), radius: 3, x: 1, y: 1))
                .padding(.horizontal, horizontalPadding)
            }
            
            CompositedImage("completion", bundle: .module, layers: 2, isResizable: true)
                .padding(.horizontal)
                .heightReader(height: $imageHeight)
        }
    }
}

public struct CompletionStepView : View {
    @EnvironmentObject var pagedNavigation: PagedNavigationViewModel
    let step: CompletionStep
    
    public init(_ step: CompletionStep) {
        self.step = step
    }
    
    public var body: some View {
        VStack {
            Spacer()
            CompletionView(title: title(), detail: detail())
            Spacer()
            if pagedNavigation.backEnabled {
                SurveyNavigationView()
            }
            else {
                ForwardButton {
                    pagedNavigation.forwardButtonText ??
                    Text("Exit", bundle: .module)
                }
            }
        }
        .fullscreenBackground(.lightSurveyBackground)
    }
    
    func title() -> Text {
        step.title.map { Text($0) } ??
        Text("Well done!", bundle: .module)
    }
    
    func detail() -> Text {
        step.detail.map { Text($0) } ??
        Text("Thank you for being part of our study.", bundle: .module)
    }
}

struct CompletionView_Previews: PreviewProvider {
    static var previews: some View {
        CompletionStepView(exampleA)
            .environmentObject(PagedNavigationViewModel(pageCount: 5, currentIndex: 0))
            .environmentObject(AssessmentState(AssessmentObject(previewStep: exampleA)))
    }
}

fileprivate let exampleA = CompletionStepObject(identifier: "exampleA")
