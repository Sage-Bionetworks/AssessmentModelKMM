//
//  UnderlinedButton.swift
//

import SwiftUI

/// An underlined button
public struct UnderlinedButton : View {
    let action: () -> Void
    let label: Text
    
    public init(label: Text, action: @escaping () -> Void) {
        self.action = action
        self.label = label
    }
    
    public var body: some View {
        Button(action: action) {
            label.underline()
        }
        .font(DesignSystem.fontRules.buttonFont(at: .underlinedButton))
    }
}

