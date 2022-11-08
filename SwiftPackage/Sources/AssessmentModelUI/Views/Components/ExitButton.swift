//
//  ExitButton.swift
//
//

import SwiftUI
import SharedMobileUI

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
            Button(action: { assessmentState.showingPauseActions = true }) {
                Image("pause", bundle: .module)
            }
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
