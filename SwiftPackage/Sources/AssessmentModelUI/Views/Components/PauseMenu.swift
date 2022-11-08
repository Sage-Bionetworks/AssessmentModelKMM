//
//  PauseMenu.swift
//
//

import SwiftUI
import SharedMobileUI
import AssessmentModel

public struct PauseMenu: View {
    @SwiftUI.Environment(\.innerSpacing) var innerSpacing: CGFloat
    @SwiftUI.Environment(\.horizontalPadding) var horizontalPadding: CGFloat
    @EnvironmentObject var assessmentState: AssessmentState
    @ObservedObject var viewModel: AssessmentViewModel
    
    public init(viewModel: AssessmentViewModel) {
        self.viewModel = viewModel
    }
    
    public var body: some View {
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
