//
//  AssessmentInterface.swift
//
//
//  Copyright © 2017-2022 Sage Bionetworks. All rights reserved.
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

/// A result map element is an element in the ``Assessment`` model that defines the expectations for a
/// ``ResultData``  associated with this element. It can define a user-facing step, a section (which may
/// or may not map to a view), a background web service, a sensor recorder, or any other piece of data
/// collected by the overall ``Assessment``.
public protocol ResultMapElement {

    /// The identifier for the node.
    var identifier: String { get }

    /// The ``comment`` is *not* intended to be user-facing and is a field that allows the assessment designer to add
    /// explanatory text describing the purpose of the assessment, section, step, or background action.
    var comment: String?  { get }

    /// Create an appropriate instance of a *new* ``ResultData`` for this map element.
    func createResult() -> ResultData
}

/// A ``Node`` is any object defined within the structure of an ``Assessment`` that is used to organize and describe UI/UX
/// for an ``Assessment``.
public protocol Node : ResultMapElement {

    /// List of button actions that should be hidden for this node even if the node subtype typically supports displaying
    /// the button on screen. This property can be defined at any level and will default to whichever is the lowest level
    /// for which this mapping is defined.
    var hideButtons: [ButtonAction] { get }

    /// A mapping of a ``ButtonAction`` to a ``ButtonActionInfo``.
    ///
    /// For example, this mapping can be used to  to customize the title of the ``ButtonAction.navigation(.goForward)``
    /// button. It can also define the title, icon, etc. on a custom button as long as the application knows how to
    /// interpret the custom action.
    ///
    /// Finally, a mapping can be used to explicitly mark a button as "should display" even if the overall assessment or
    /// section includes the button action in the list of hidden buttons. For example, an assessment may define the
    /// skip button as hidden but a lower level step within that assessment's hierarchy can return a mapping for the
    /// skip button. The lower level mapping should be respected and the button should be displayed for that step only.
    var buttonMap: [ButtonAction : ButtonActionInfo] { get }
    
    /// Does this ``Node`` support backward navigation?
    func canGoBack() -> Bool
}

public extension Node {
    // Default implementation is that a node can go back unless the button is explicitly hidden.
    func canGoBack() -> Bool {
        !hideButtons.contains(.navigation(.goBackward))
    }
}

/// A ``ContentNode`` contains additional content that may, under certain circumstances and where screen real estate
/// allows, be displayed to the participant to help them understand the intended purpose of the part of the assessment
/// described by this ``Node``.
public protocol ContentNode : Node {
    
    /// The primary text to display for the node in a localized string. The UI should display this using a larger font.
    var title: String? { get }

    /// A subtitle to display for the node in a localized string.
    var subtitle: String? { get }

    /// Detail text to display for the node in a localized string.
    var detail: String? { get }

    /// An image or animation to display with this node.
    var imageInfo: ImageInfo?  { get }
}
