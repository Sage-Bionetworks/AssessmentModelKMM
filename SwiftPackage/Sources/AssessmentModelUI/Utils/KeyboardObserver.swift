//
//  KeyboardObserver.swift
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

