//
//  SelectionToggleStyle.swift
//  
//

import SwiftUI

public struct SelectionToggleStyle : ToggleStyle {
    #if os(iOS)
    @Environment(\.editMode) private var editMode
    #endif
    
    private let spacing: CGFloat
    private let selectedColor: Color
    private let isSingleSelect: Bool
    
    public init(spacing: CGFloat = 8, selectedColor: Color, isSingleSelect: Bool) {
        self.spacing = spacing
        self.selectedColor = selectedColor
        self.isSingleSelect = isSingleSelect
    }
    
    public func makeBody(configuration: Configuration) -> some View {
        if isSingleSelect {
            buttonView(configuration)
                .buttonStyle(ToggleButtonStyle(isOn: configuration.isOn, selectedColor: selectedColor, clipShape: Capsule()))
        }
        else {
            buttonView(configuration)
                .buttonStyle(ToggleButtonStyle(isOn: configuration.isOn, selectedColor: selectedColor, clipShape: RoundedRectangle(cornerRadius: 5)))
        }
    }
    
    @ViewBuilder
    func buttonView(_ configuration: Configuration) -> some View {
        Button {
            #if os(iOS)
            if editMode?.wrappedValue.isEditing ?? false { return } // Exit early if editing
            #endif
            configuration.isOn.toggle()
        } label: {
            HStack(spacing: spacing) {
                toggleView(isOn: configuration.isOn)
                    .padding(.top, 17)
                    .padding(.bottom, 15)
                    .padding(.leading, 24)
                
                configuration.label
            }
        }
    }
    
    @ViewBuilder
    func toggleView(isOn: Bool) -> some View {
        if isSingleSelect {
            RadioButtonToggle(isOn: isOn)
        }
        else {
            CheckboxToggle(isOn: isOn)
        }
    }
}

fileprivate struct ToggleButtonStyle<S: Shape>: ButtonStyle {
    let isOn: Bool
    let selectedColor: Color
    let clipShape: S
    
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .padding(.trailing, 16)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(isOn == configuration.isPressed ? Color.sageWhite : selectedColor)
            .clipShape(clipShape)
            .shadow(color: .hex2A2A2A.opacity(0.1), radius: 3, x: 1, y: 2)
    }
}
