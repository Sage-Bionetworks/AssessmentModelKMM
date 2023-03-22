//
//  PauseMenu.swift
//
//

import SwiftUI
import SharedMobileUI
import AssessmentModel

/// Handler for the pause menu set up and actions.
public protocol PauseMenuHandler {
    
    /// Resume running the assessment.
    func resume()
    
    /// Review the instructions.
    func reviewInstructions()
    
    /// Skip performing the assessment.
    func skipAssessment()
    
    /// Exit the assessment.
    func exitAssessment()
}

extension AssessmentViewModel : PauseMenuHandler {
}

/// The pause menu shows the participant the pause menu and allows a handler
/// to respond to the actions of the pause menu.
public struct PauseMenu<Handler : PauseMenuHandler>: View {
    @SwiftUI.Environment(\.innerSpacing) var innerSpacing: CGFloat
    @SwiftUI.Environment(\.horizontalPadding) var horizontalPadding: CGFloat
    let viewModel: Handler
    let interruptionHandling: InterruptionHandling
    
    public init(viewModel: Handler, interruptionHandling: InterruptionHandling) {
        self.viewModel = viewModel
        self.interruptionHandling = interruptionHandling
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
                if interruptionHandling.canResume {
                    Button(action: viewModel.resume) {
                        Text("Resume", bundle: .module)
                    }
                    .buttonStyle(PrimaryButtonStyle())
                }
                if interruptionHandling.reviewIdentifier != nil {
                    Button(action: viewModel.reviewInstructions) {
                        Text("Review instructions", bundle: .module)
                    }
                    .buttonStyle(SecondaryButtonStyle())
                }
                if interruptionHandling.canSkip {
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

struct PauseMenu_Previews: PreviewProvider {
    static var previews: some View {
        PauseMenu(viewModel: AssessmentViewModel(), interruptionHandling: InterruptionHandlingObject())
    }
}
