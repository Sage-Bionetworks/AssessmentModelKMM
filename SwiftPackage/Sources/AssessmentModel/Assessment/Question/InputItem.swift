//
//  InputItem.swift
//  
//
//  Copyright © 2020-2022 Sage Bionetworks. All rights reserved.
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

import Foundation
import JsonModel

/// An ``InputItem`` describes a "part" of a ``Question`` representing a single answer.
///
/// For example, if a question is "what is your name" then the input items may include "given name" and "family name"
/// where separate text fields are used to allow the participant to enter their first and last name, and the question
/// may also include a list of titles from which to choose.
///
/// In another example, the input item could be a single cell in a list that shows the possible choices for a question.
/// In essence, this is akin to a single cell in a table view though the actual implementation may differ.
public protocol InputItem {

    /// The result identifier is an optional value that can be used to help in building the serializable answer result
    /// from this ``InputItem``. If null, then it is assumed that the ``Question`` that holds this ``InputItem``
    /// has some custom serialization strategy or only contains a single answer and this property can be ignored.
    var resultIdentifier: String? { get }
    
    /// The kind of object to expect for the serialization of the answer associated with this ``InputItem``. Typically,
    /// this will be an ``AnswerType`` that maps to a simple ``JsonType``,  but it is possible for the
    /// ``InputItem`` to translate to an object rather than a primitive.
    ///
    /// For example, the question could be about blood pressure where the participant answers the question with a string
    /// of "120/70" but the state handler is responsible for translating that into a data class with systolic and
    /// diastolic as properties that are themselves numbers.
    var answerType: AnswerType? { get }
}

public protocol ChoiceInputItem : InputItem, ChoiceOption {
}

public protocol TextInputItem : InputItem {

    /// Options for displaying a text field. This is only applicable for certain types of UI hints
    /// and data types. If not applicable, it will be ignored.
    var keyboardOptions: KeyboardOptions { get }
    
    /// A localized string that displays a short text offering a hint to the user of the data to be entered for this field.
    var fieldLabel: String? { get }

    /// A localized string that displays placeholder information for the ``InputItem``.
    ///
    /// You can display placeholder text in a text field or text area to help users understand how to answer the item's
    /// question. If the input field brings up another view to enter the answer, this could also be used at the button
    /// title.
    var placeholder: String? { get }

    /// This can be used to return a class used to format and/or validate the text input.
    func buildTextValidator() -> TextEntryValidator
}

