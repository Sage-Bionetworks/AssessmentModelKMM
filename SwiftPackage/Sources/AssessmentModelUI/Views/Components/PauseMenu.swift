//
//  PauseMenu.swift
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

struct PauseMenu: View {
    @SwiftUI.Environment(\.innerSpacing) var innerSpacing: CGFloat
    @SwiftUI.Environment(\.horizontalPadding) var horizontalPadding: CGFloat
    @EnvironmentObject var assessmentState: AssessmentState
    @ObservedObject var viewModel: AssessmentViewModel
    
    var body: some View {
        VStack(spacing: 0) {
            Text("Paused", bundle: .module)
                .font(.pauseMenuTitle)
                .foregroundColor(.pauseMenuForeground)
                .padding(.top, 26)
                .padding(.bottom, 31)
            
            Divider()
                .frame(height: 2)
                .background(Color.pauseMenuForeground)
            
            Spacer()
            
            VStack(spacing: innerSpacing) {
                if assessmentState.interruptionHandling.canResume {
                    Button(action: viewModel.resume) {
                        Text("Resume", bundle: .module)
                    }
                    .buttonStyle(PrimaryButtonStyle())
                }
                if assessmentState.interruptionHandling.reviewIdentifier != nil {
                    Button(action: viewModel.reviewInstructions) {
                        Text("Review instructions", bundle: .module)
                    }
                    .buttonStyle(SecondaryButtonStyle())
                }
                if assessmentState.interruptionHandling.canSkip {
                    Button(action: viewModel.skipAssessment) {
                        Text("Skip this activity", bundle: .module)
                    }
                    .buttonStyle(SecondaryButtonStyle())
                }
                Button(action: viewModel.exitAssessment) {
                    Text("Continue later", bundle: .module)
                }
                .buttonStyle(SecondaryButtonStyle())
            }
            .padding(.horizontal, horizontalPadding)
            
            Spacer()
        }
        .fullscreenBackground(.pauseMenuBackground)
    }
    
    struct PrimaryButtonStyle : ButtonStyle {
        @ViewBuilder public func makeBody(configuration: Self.Configuration) -> some View {
            configuration.label
                .font(.roundedButton)
                .foregroundColor(.pauseMenuResumeText)
                .frame(minHeight: 48, idealHeight: 48)
                .frame(maxWidth: .infinity)
                .background(Color.pauseMenuForeground)
                .clipShape(Capsule())
        }
    }

    struct SecondaryButtonStyle : ButtonStyle {
        @ViewBuilder public func makeBody(configuration: Self.Configuration) -> some View {
            configuration.label
                .font(.roundedButton)
                .foregroundColor(.pauseMenuForeground)
                .frame(minHeight: 48, idealHeight: 48)
                .frame(maxWidth: .infinity)
                .background(Color.clear)
                .overlay(
                    Capsule()
                        .stroke(Color.pauseMenuForeground, lineWidth: 2)
                )
        }
    }
}

struct PauseMenuPreview : View {
    @StateObject var viewModel: AssessmentViewModel = .init()
    @ObservedObject var assessmentState: AssessmentState
    
    public init(_ assessmentState: AssessmentState) {
        self.assessmentState = assessmentState
    }
    
    public var body: some View {
        PauseMenu(viewModel: viewModel)
            .environmentObject(assessmentState)
            .onAppear {
                viewModel.initialize(assessmentState)
            }
    }
}

struct PauseMenu_Previews: PreviewProvider {
    static var previews: some View {
        PauseMenuPreview(.init(surveyA))
    }
}
