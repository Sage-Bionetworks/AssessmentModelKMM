//
//  CheckboxToggleStyle.swift
//
//

import SwiftUI

public struct CheckboxToggleStyle : ToggleStyle {
    private let spacing: CGFloat
    private let padding: CGFloat?
    
    public init(spacing: CGFloat = 8, padding: CGFloat? = nil) {
        self.spacing = spacing
        self.padding = padding
    }
    
    public func makeBody(configuration: Configuration) -> some View {
        Button {
            configuration.isOn.toggle()
        } label: {
            HStack(spacing: spacing) {
                CheckboxToggle(isOn: configuration.isOn)
                configuration.label
            }
            .padding(.all, padding)
        }
        .buttonStyle(PlainButtonStyle())
    }
}

struct CheckboxToggle : View {
    private let boxColor: Color = .hex3E3E3E
    private let boxSize: CGFloat = 22
    let isOn: Bool
    
    var body: some View {
        Image(systemName: isOn ? "checkmark.square" : "square")
            .foregroundColor(boxColor)
            .frame(width: boxSize, height: boxSize)
    }
}
