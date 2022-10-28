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
import ResultModel
import MobilePassiveData

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
public protocol Node : ResultMapElement, ButtonActionHandler {
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

/// A ``ContentNode`` is a node that includes information, depending upon the UI design and available real-estate.
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
    /// step passes the time indicated by the given time interval.  If `timeInterval` is greater than or equal to
    /// `duration` or is equal to `Double.infinity`, then the spoken instruction returned should be for
    /// when the step is finished.
    ///
    /// - parameter timeInterval: The time interval at which to speak the instruction.
    /// - returns: The localized instruction to speak or `nil` if there isn't an instruction.
    func spokenInstruction(at timeInterval: TimeInterval) -> String?
}

public protocol ContentStep : Step, ContentNode {
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

/// Information about an assessment.
public protocol AssessmentInfo {
    
    /// For assessments from Bridge this is the unique identifier for a particular revision of an assessment.
    var guid: String? { get }
    
    /// The identifier used by a given library to uniquely identify the assessment within that library.
    var identifier: String { get }

    /// The version of this assessment. This may be a semantic version, timestamp, or sequential revision integer.
    var versionString: String? { get }

    /// The estimated number of minutes that the assessment will take. If `0`, then it is assumed that this value is not
    /// defined. Where provided, it can be used by an application to indicate to the participant approximately how
    /// long an assessment is expected to take to complete.
    var estimatedMinutes: Int { get }
    
    /// Copyright information about this assessment.
    var copyright: String? { get }
}

public protocol BranchNode : Node {

    /// The ``Navigator`` for this section or assessment.
    func instantiateNavigator(state: NavigationState) throws -> Navigator

    /// The instantiated result should be a branch node result.
    func instantiateBranchNodeResult() -> BranchNodeResult
}

public protocol Assessment : BranchNode, ContentNode, AssessmentInfo {
    
    /// The interruption handling rules for this assessment.
    var interruptionHandling: InterruptionHandling { get }
    
    /// The instantiated result should be an assessment result.
    func instantiateAssessmentResult() -> AssessmentResult
}

public extension Assessment {
    func instantiateBranchNodeResult() -> BranchNodeResult {
        instantiateAssessmentResult()
    }
}

/// An ``AsyncActionContainer`` is a node that contains the model description for asynchronous background actions that
/// should be started when this ``Node`` in the ``Assessment`` is presented to the user.
public protocol AsyncActionContainer : Node {

    /// A list of the ``AsyncActionConfiguration`` elements used to describe the configuration for background actions
    /// (such as a sensor recorder or web service) that should should be started when this ``Node`` in the ``Assessment``
    /// is presented to the user.
    var asyncActions: [AsyncActionConfiguration] { get }
}

