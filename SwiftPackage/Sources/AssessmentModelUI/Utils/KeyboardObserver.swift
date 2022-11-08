//
//  KeyboardObserver.swift
//
//

import SwiftUI

class KeyboardObserver : ObservableObject {
    @Published var keyboardFocused: Bool = false
    @Published var keyboardHeight: CGFloat = 236
    @Published var keyboardFocusedId: String = KeyboardObserver.defaultKeyboardFocusedId
    @Published var keyboardState: KeyboardState = .didHide
    @Published var textFieldHeight: CGFloat = textFieldFontSize
    @Published var cursorAtEnd: Bool = true
    
    static let defaultKeyboardFocusedId = "$keyboardFocusedId"
    
    enum KeyboardState : Int {
        case willShow, didShow, willHide, didHide
        var focused: Bool {
            self == .willShow || self == .didShow
        }
    }
    
    init() {
        #if canImport(UIKit)
        NotificationCenter.default.addObserver(forName: UIResponder.keyboardWillShowNotification, object: nil, queue: .main) { _ in
            if !self.keyboardFocused {
                self.keyboardFocused = true
            }
            if self.keyboardState != .willShow {
                self.keyboardState = .willShow
            }
        }
        NotificationCenter.default.addObserver(forName: UIResponder.keyboardDidShowNotification, object: nil, queue: .main) { (notification) in
            if !self.keyboardFocused {
                self.keyboardFocused = true
            }
            if self.keyboardState != .didShow {
                self.keyboardState = .didShow
            }
        }
        NotificationCenter.default.addObserver(forName: UIResponder.keyboardDidChangeFrameNotification, object: nil, queue: .main) { (notification) in
            guard let userInfo = notification.userInfo,
                  let keyboardRect = userInfo[UIResponder.keyboardFrameEndUserInfoKey] as? CGRect
            else {
                return
            }
            self.keyboardHeight = keyboardRect.height
            self.keyboardState = self.keyboardState
        }
        NotificationCenter.default.addObserver(forName: UIResponder.keyboardWillHideNotification, object: nil, queue: .main) { _ in
            if self.keyboardFocused {
                self.keyboardFocused = false
            }
            if self.keyboardState != .willHide {
                self.keyboardState = .willHide
            }
        }
        NotificationCenter.default.addObserver(forName: UIResponder.keyboardDidHideNotification, object: nil, queue: .main) { _ in
            if self.keyboardFocused {
                self.keyboardFocused = false
            }
            if self.keyboardState != .didHide {
                self.keyboardState = .didHide
            }
        }
        #endif
    }
    
    func hideKeyboard() {
        guard keyboardFocused else { return }
        keyboardFocused = false
        #if canImport(UIKit)
        UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
        #endif
    }
}

