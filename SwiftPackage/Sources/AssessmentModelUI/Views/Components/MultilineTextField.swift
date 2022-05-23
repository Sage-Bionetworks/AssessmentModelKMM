//
//  MultilineTextField.swift
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
import JsonModel
import SharedMobileUI

struct MultilineTextField: View {
    @SwiftUI.Environment(\.characterLimit) var characterLimit: Int
    @ScaledMetric private var fontRatio: CGFloat = 1
    @EnvironmentObject private var keyboard: KeyboardObserver
    var isSelected: Binding<Bool>?
    @State var isEditingText: Bool = false
    @Binding var text: String?
    @State var fontSize: CGFloat = textFieldFontSize
    @State var textWidth: CGFloat = 0
    @State var fontHeight: CGFloat = 0
    
    private let inputItem: TextInputItem
    private let fieldLabel: String
    private let style: TextFieldStyle
    private let isBold: Bool
    
    init(text bindingText: Binding<String?>,
         isSelected: Binding<Bool>? = nil,
         inputItem: TextInputItem = StringTextInputItemObject(),
         fieldLabel: String? = nil,
         fontStyle: TextFieldStyle = .otherChoice) {
        self._text = bindingText
        self.isSelected = isSelected
        self.inputItem = inputItem
        self.fieldLabel = fieldLabel.map { $0.hasSuffix(": ") || $0.isEmpty ? $0 : "\($0): " } ?? ""
        self.style = fontStyle
        self.isBold = (fontStyle == .otherChoice)
    }
    
    var body: some View {
        ZStack(alignment: .bottomTrailing) {
            // This is used to force a refresh of the font size and width of
            // the view if the user changes the dynamic font size.
            Text("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .opacity(0)
                .lineLimit(1)
                .widthReader(width: $textWidth)
                .heightReader(height: $fontHeight)
            // This is a hint to the user that they've hit the text limit.
            Text("[\(characterLimit)]")
                .opacity((text?.count ?? 0 == characterLimit) && isEditingText && style == .freeText ? 1 : 0)
                .foregroundColor(.hex727272)
                .font(.italicLatoFont(18, relativeTo: nil, weight: .regular))
                .offset(x: 0, y: fontSize/2)
            #if canImport(UIKit)
            // This is the actual UITextView used to support the desired UI/UX
            MultilineTextFieldContainer(
                fieldLabel,
                text: $text,
                isEditingText: $isEditingText,
                isSelected: isSelected,
                inputItem: inputItem,
                characterLimit: characterLimit,
                textHeight: $keyboard.textFieldHeight,
                cursorAtEnd: $keyboard.cursorAtEnd,
                fontSize: $fontSize,
                isBold: isBold)
                .frame(height: keyboard.textFieldHeight)
            #endif
        }
        .onChange(of: isEditingText) { newValue in
            keyboard.keyboardFocused = newValue
        }
        .onChange(of: textWidth) { _ in
            updateStyle()
        }
        .onChange(of: fontHeight) { _ in
            updateStyle()
        }
        .onAppear {
            updateStyle()
        }
    }
    
    func updateStyle() {
        switch style {
        case .otherChoice:
            fontSize = textFieldFontSize
        case .freeText:
            fontSize = max(1, min(1.5, fontRatio)) * 18
        }
    }
}

fileprivate struct CharacterLimitEnvironmentKey: EnvironmentKey {
    fileprivate static let defaultValue: Int = 250
}

extension EnvironmentValues {
    var characterLimit: Int {
        get { self[CharacterLimitEnvironmentKey.self] }
        set { self[CharacterLimitEnvironmentKey.self] = newValue }
    }
}

extension View {
    func characterLimit(_ characterLimit: Int) -> some View {
        environment(\.characterLimit, characterLimit)
    }
}

struct PreviewMultilineTextField: View {
    @State var value: String? = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
    @State var oneLine: String? = "Lorem ipsum dolor"
    @State var isSelected = false
    @StateObject var keyboard: KeyboardObserver = .init()
    
    var body: some View {
        VStack {
            Text("Hello, World")
                .font(.latoFont(18))
            
            MultilineTextField(text: $value, isSelected: $isSelected, fieldLabel: "Other")
                .border(Color.sageBlack, width: 1)
            
            MultilineTextField(text: $oneLine, isSelected: $isSelected, fieldLabel: "Other")
                .border(Color.sageBlack, width: 1)
            
            MultilineTextField(text: $value, fontStyle: .freeText)
                .border(Color.sageBlack, width: 1)
        }
        .characterLimit(80)
        .environmentObject(keyboard)
    }
}

struct PreviewMultilineTextField_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            PreviewMultilineTextField()
            PreviewMultilineTextField()
                .environment(\.sizeCategory, .accessibilityExtraLarge)
        }
    }
}

#if canImport(UIKit)

fileprivate final class MultilineTextFieldContainer: UIViewRepresentable {
    private let characterLimit: Int
    private let fieldLabel: String
    private let inputItem: TextInputItem
    private let value: Binding<String?>
    private let isEditingText: Binding<Bool>
    private let isSelected: Binding<Bool>?
    private let textHeight: Binding<CGFloat>
    private let cursorAtEnd: Binding<Bool>
    private let isBold: Bool
    private let fontSize: Binding<CGFloat>
    
    private var previousFontSize: CGFloat

    init(_ fieldLabel: String, text: Binding<String?>, isEditingText: Binding<Bool>, isSelected: Binding<Bool>?, inputItem: TextInputItem, characterLimit: Int, textHeight: Binding<CGFloat>, cursorAtEnd: Binding<Bool>, fontSize: Binding<CGFloat>, isBold: Bool) {
        self.fieldLabel = fieldLabel
        self.inputItem = inputItem
        self.value = text
        self.isEditingText = isEditingText
        self.isSelected = isSelected
        self.characterLimit = characterLimit
        self.textHeight = textHeight
        self.cursorAtEnd = cursorAtEnd
        self.fontSize = fontSize
        self.isBold = isBold
        self.previousFontSize = fontSize.wrappedValue
    }

    func makeCoordinator() -> MultilineTextFieldContainer.Coordinator {
        Coordinator(self)
    }

    func makeUIView(context: UIViewRepresentableContext<MultilineTextFieldContainer>) -> UITextView {
        
        let backingView = UITextView(frame: .zero)
        backingView.text = backingViewText()
        backingView.delegate = context.coordinator
        backingView.backgroundColor = .clear
        backingView.adjustsFontForContentSizeCategory = true
        backingView.textColor = .textForeground
        backingView.returnKeyType = .done
        backingView.isScrollEnabled = false
        backingView.setContentCompressionResistancePriority(.defaultLow, for: .horizontal)
        backingView.font = .latoFont(fontSize.wrappedValue, relativeTo: nil, weight: isBold ? .bold : .regular)

        return backingView
    }

    func updateUIView(_ uiView: UITextView, context: UIViewRepresentableContext<MultilineTextFieldContainer>) {
        uiView.text = backingViewText()

        DispatchQueue.main.async {
            
            // Recalculate the frame height.
            self.updateTextViewSize(uiView)
            
            // Look to see if the selection state has changed and the text view should become the first responder.
            guard !self.isEditingText.wrappedValue,
                  self.value.wrappedValue?.isEmpty ?? true,
                  let isSelected = self.isSelected?.wrappedValue, isSelected
            else {
                return
            }
            uiView.becomeFirstResponder()
        }
    }
    
    func updateTextViewSize(_ textView: UITextView) {
        if previousFontSize != fontSize.wrappedValue {
            previousFontSize = fontSize.wrappedValue
            textView.font = .latoFont(fontSize.wrappedValue, relativeTo: nil, weight: isBold ? .bold : .regular)
        }
        
        let newHeight = textView.sizeThatFits(textView.frame.size).height
        if newHeight != textHeight.wrappedValue {
            textHeight.wrappedValue = newHeight
        }
    }
    
    func backingViewText() -> String {
        "\(fieldLabel)\(value.wrappedValue ?? "")"
    }
    
    func updateText(newValue: String?, trim: Bool) {
        let parsedValue = newValue.map {
            if fieldLabel.isEmpty {
                return $0
            }
            else if let range = $0.range(of: fieldLabel), range.lowerBound == $0.startIndex {
                return String($0[range.upperBound...])
            }
            else {
                return ""
            }
        } ?? ""
        value.wrappedValue = trim ? parsedValue.trimmingCharacters(in: .whitespacesAndNewlines) : parsedValue
    }

    class Coordinator: NSObject, UITextViewDelegate {
        var parent: MultilineTextFieldContainer

        init(_ textFieldContainer: MultilineTextFieldContainer) {
            self.parent = textFieldContainer
        }
        
        @objc func textViewDidBeginEditing(_ textView: UITextView) {
            parent.isEditingText.wrappedValue = true
            textView.moveCursorIfNeeded(parent.fieldLabel)
            if let selected = parent.isSelected?.wrappedValue, !selected {
                parent.isSelected?.wrappedValue = true
            }
        }

        @objc func textViewDidEndEditing(_ textView: UITextView) {
            parent.isEditingText.wrappedValue = false
            parent.updateText(newValue: textView.text, trim: true)
            if parent.value.wrappedValue?.isEmpty ?? true {
                parent.isSelected?.wrappedValue = false
            }
        }

        @objc func textView(_ textView: UITextView, shouldChangeTextIn range: NSRange, replacementText text: String) -> Bool {
            if text.last?.isNewline ?? false {
                textView.endEditing(true)
                return false
            }
            else if !parent.fieldLabel.isEmpty && range.lowerBound < parent.fieldLabel.endRange {
                return false
            }
            else {
                updateCursorAtEnd(textView)
                return textView.text.count - range.length + text.count <= parent.characterLimit
            }
        }

        @objc func textViewDidChangeSelection(_ textView: UITextView) {
            textView.moveCursorIfNeeded(parent.fieldLabel)
            updateCursorAtEnd(textView)
        }
        
        func updateCursorAtEnd(_ textView: UITextView) {
            parent.cursorAtEnd.wrappedValue = textView.selectedTextRange.map {
                $0.end == textView.endOfDocument
            } ?? false
        }

        @objc func textViewDidChange(_ textView: UITextView) {
            parent.updateText(newValue: textView.text, trim: false)
        }
    }
}

extension String {
    fileprivate var endRange: Int {
        endIndex.utf16Offset(in: self)
    }
}

extension UITextView {
    
    fileprivate func moveCursorIfNeeded(_ fieldLabel: String) {
        guard !fieldLabel.isEmpty,
              let cursor = cursorIndex,
              cursor < fieldLabel.endIndex,
              let newPosition = position(from: beginningOfDocument, offset: fieldLabel.endRange)
        else {
            return
        }
        self.selectedTextRange = textRange(from: newPosition, to: newPosition)
    }
    
    var cursorIndex: String.Index? {
        guard let range = selectedTextRange else { return nil }
        let location = offset(from: beginningOfDocument, to: range.start)
        return Range(.init(location: location, length: 0), in: text)?.lowerBound
    }
}

#endif
