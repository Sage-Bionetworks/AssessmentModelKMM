//
//  ExitButton.swift
//
//

import SwiftUI
import SharedMobileUI

/// A button that shows a pause menu
public struct PauseButton: View {
    @Binding var showingPauseActions: Bool
    
    public init(_ showingPauseActions: Binding<Bool>) {
        self._showingPauseActions = showingPauseActions
    }
    
    public var body: some View {
        Button(action: { showingPauseActions = true }) {
            Image("pause", bundle: .module)
        }
    }
}

struct ExitButton: View {
    @EnvironmentObject var assessmentState: AssessmentState
    let canPause: Bool
    
    var body: some View {
        exitButton()
            .accentColor(.sageBlack)
    }
    
    @ViewBuilder
    func exitButton() -> some View {
        if canPause {
            PauseButton($assessmentState.showingPauseActions)
        }
        else {
            Button(action: { assessmentState.status = .continueLater }) {
                Image("close", bundle: .module)
            }
        }
    }
}

struct ExitButton_Previews: PreviewProvider {
    static var previews: some View {
        HStack {
            ExitButton(canPause: true)
            ExitButton(canPause: false)
        }
    }
}
