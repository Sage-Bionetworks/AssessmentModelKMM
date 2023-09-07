//
//  RadioButtonToggleStyle.swift
//  
//

import SwiftUI

public struct RadioButtonToggleStyle : ToggleStyle {
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
                RadioButtonToggle(isOn: configuration.isOn)
                configuration.label
            }
            .padding(.all, padding)
        }
        .buttonStyle(PlainButtonStyle())
    }
}

struct RadioButtonToggle : View {
    private let dotColor: Color = .hex3E3E3E
    private let circleSize: CGFloat = 24
    private let dotSize: CGFloat = 14.4
    let isOn: Bool
    var body: some View {
        ZStack {
            Circle()
                .strokeBorder(dotColor, lineWidth: 2.5)
                .frame(width: circleSize, height: circleSize)
            Circle()
                .fill(dotColor)
                .frame(width: dotSize, height: dotSize)
                .opacity(isOn ? 1 : 0)
        }
    }
}
