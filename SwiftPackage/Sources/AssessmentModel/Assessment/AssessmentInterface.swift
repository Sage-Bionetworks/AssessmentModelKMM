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
import JsonModel

/// A result map element is an element in the ``Assessment`` model that defines the expectations for a
/// ``ResultData``  associated with this element. It can define a user-facing step, a section (which may
/// or may not map to a view), a background web service, a sensor recorder, or any other piece of data
/// collected by the overall ``Assessment``.
public protocol ResultMapElement : PolymorphicTyped {

    /// The identifier for the node.
    var identifier: String { get }

    /// The ``comment`` is *not* intended to be user-facing and is a field that allows the assessment designer to add
    /// explanatory text describing the purpose of the assessment, section, step, or background action.
    var comment: String?  { get }

    /// Create an appropriate instance of a *new* ``ResultData`` for this map element.
    func instantiateResult() -> ResultData
}

/// A ``Node`` is any object defined within the structure of an ``Assessment`` that is used to organize and describe UI/UX
/// for an ``Assessment``.
public protocol Node : ResultMapElement {

    /// List of button actions that should be hidden for this node even if the node subtype typically supports displaying
    /// the button on screen. This property can be defined at any level and will default to whichever is the lowest level
    /// for which this mapping is defined.
    var shouldHideButtons: Set<ButtonAction> { get }

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
        !shouldHideButtons.contains(.navigation(.goBackward))
    }
}

/// Content info is general information about a ``Node``, ``Question``, or other UI/UX element where the syntax
/// for title, subtitle, and detail should be consistent.
public protocol ContentInfo {
    
    /// The primary text to display for this content in a localized string. The UI should display this using a larger font.
    var title: String? { get }

    /// A subtitle to display for this content in a localized string.
    var subtitle: String? { get }

    /// Detail text to display for this content in a localized string.
    var detail: String? { get }
}

/// a ``ContentNode`` is a node that includes information, depending upon the UI design and available real-estate.
public protocol ContentNode : Node, ContentInfo {
    
    /// The primary text to display for the node in a localized string. The UI should display this using a larger font.
    var title: String? { get }

    /// A subtitle to display for the node in a localized string.
    var subtitle: String? { get }

    /// Detail text to display for the node in a localized string.
    var detail: String? { get }
    
    /// An image or animation to display with this node.
    var imageInfo: ImageInfo? { get }
}

/// A user-interface step in an ``Assessment``.
///
/// This is the base interface for the steps that can compose an assessment for presentation using a controller
/// appropriate to the device and application. Each ``Step`` object represents one logical piece of data entry,
/// information, or activity in a larger assessment.
///
/// A step can be a question, an active test, or a simple instruction. It is typically paired with a step controller that
/// controls the actions of the ``Step``.
public protocol Step : Node {

    /// Localized text that represents an instructional voice prompt. Instructional speech begins when the
    /// step passes the time indicated by the given time.  If `timeInterval` is greater than or equal to
    /// `duration` or is equal to `Double.infinity`, then the spoken instruction returned should be for
    /// when the step is finished.
    ///
    /// - parameter timeInterval: The time interval at which to speak the instruction.
    /// - returns: The localized instruction to speak or `nil` if there isn't an instruction.
    func spokenInstruction(at timeInterval: TimeInterval) -> String?
}

/// This protocol is used to allow an assessment designer to show a more detailed set of instructions only
/// to users who are not already familiar with the assessment rather than showing a full set of instructions
/// every time. The state handling for the assessment can use the ``fullInstructionsOnly`` flag to
/// determine if a user who has done this assessment before should be allowed to skip the full instructions.
/// The implementation details for storing state about the participant and handling this flag are left to the
/// assessment developers.
public protocol OptionalNode : Node {

    /// Should this step only be displayed when showing the full instruction sequence?
    var fullInstructionsOnly: Bool { get }
}

/// The instruction step protocol is used to allow the UI/UX for a set of steps to use `is` switch statements
/// to determine the type of view to present to the participant.
public protocol InstructionStep : Step, OptionalNode, ContentNode {
}

