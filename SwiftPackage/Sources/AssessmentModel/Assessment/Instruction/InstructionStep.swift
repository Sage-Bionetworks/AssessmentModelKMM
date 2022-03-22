//
//  InstructionStep.swift
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

/// The instruction step protocol is used to allow the UI/UX for a set of steps to use `is` switch statements
/// to determine the type of view to present to the participant.
public protocol InstructionStep : Step, OptionalNode, ContentNode {
}

open class AbstractInstructionStepObject : AbstractStepObject, InstructionStep {
    private enum CodingKeys : String, OrderedEnumCodingKey, OpenOrderedCodingKey {
        case _fullInstructionsOnly = "fullInstructionsOnly", spokenInstructions
        var relativeIndex: Int { 5 }
    }
    
    /// Should this step be displayed if and only if the flag has been set for displaying the full
    /// instructions?
    public var fullInstructionsOnly: Bool { _fullInstructionsOnly ?? false }
    private let _fullInstructionsOnly: Bool?
    
    // MARK: spoken instruction handling
    
    /// A mapping of the localized text that represents an instructional voice prompt to the time
    /// marker for speaking the instruction.
    public let spokenInstructions: [SpokenInstructionKey : String]?
    
    public enum SpokenInstructionKey : String, OrderedEnumCodingKey {
        case start, end
    }
    
    /// Localized text that represents an instructional voice prompt. Instructional speech can be
    /// returned for `timeInterval == 0` and `timeInterval == Double.infinity` which indicate the
    /// start and end of the step.
    ///
    /// - parameter timeInterval: The time interval at which to speak the instruction.
    /// - returns: The localized instruction to speak or `nil` if there isn't an instruction.
    open override func spokenInstruction(at timeInterval: TimeInterval) -> String? {
        switch timeInterval {
        case 0:
            return self.spokenInstructions?[.start]
        case Double.infinity:
            return self.spokenInstructions?[.end]
        default:
            return nil
        }
    }
    
    // MARK: Initializers and serialization
    
    public init(identifier: String,
                         title: String? = nil, subtitle: String? = nil, detail: String? = nil, imageInfo: ImageInfo? = nil,
                         shouldHideButtons: Set<ButtonType>? = nil, buttonMap: [ButtonType : ButtonActionInfo]? = nil, comment: String? = nil,
                         fullInstructionsOnly: Bool? = nil, spokenInstructions: [SpokenInstructionKey : String]? = nil) {
        self._fullInstructionsOnly = fullInstructionsOnly
        self.spokenInstructions = spokenInstructions
        super.init(identifier: identifier,
                   title: title, subtitle: subtitle, detail: detail, imageInfo: imageInfo,
                   shouldHideButtons: shouldHideButtons, buttonMap: buttonMap, comment: comment)
    }
    
    public required init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self._fullInstructionsOnly = try container.decodeIfPresent(Bool.self, forKey: ._fullInstructionsOnly)
        if let dictionary = try container.decodeIfPresent([String : String].self, forKey: .spokenInstructions) {
            self.spokenInstructions = try dictionary.reduce(into: [SpokenInstructionKey : String](), { (hashMap, pair) in
                guard let specialKey = SpokenInstructionKey(rawValue: pair.key) else {
                    var codingPath = decoder.codingPath
                    codingPath.append(CodingKeys.spokenInstructions)
                    let context = DecodingError.Context(codingPath: codingPath,
                                                        debugDescription: "\(pair.key) cannot be converted to a SpokenInstructionKey")
                    throw DecodingError.typeMismatch(SpokenInstructionKey.self, context)
                }
                hashMap[specialKey] = pair.value
            })
        }
        else {
            self.spokenInstructions = nil
        }
        try super.init(from: decoder)
    }

    open override func encode(to encoder: Encoder) throws {
        try super.encode(to: encoder)
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encodeIfPresent(self._fullInstructionsOnly, forKey: ._fullInstructionsOnly)
        if let dictionary = self.spokenInstructions {
            var nestedContainer = container.nestedContainer(keyedBy: SpokenInstructionKey.self, forKey: .spokenInstructions)
            try dictionary.forEach { (key, value) in
                try nestedContainer.encode(value, forKey: key)
            }
        }
    }

    // Overrides must be defined in the base implementation

    override open class func codingKeys() -> [CodingKey] {
        var keys = super.codingKeys()
        let thisKeys: [CodingKey] = CodingKeys.allCases
        keys.append(contentsOf: thisKeys)
        return keys
    }

    override open class func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            return try super.documentProperty(for: codingKey)
        }
        switch key {
        case .spokenInstructions:
            return .init(propertyType: .primitiveDictionary(.string), propertyDescription:
                            "A mapping of a localized spoken instruction to a key where the key is either 'start' or 'end'.")
        case ._fullInstructionsOnly:
            return .init(defaultValue: .boolean(false), propertyDescription:
                            "Should this instruction step be displayed when displaying full instructions only?")
        }
    }
}

public final class InstructionStepObject : AbstractInstructionStepObject, Encodable, DocumentableStruct, CopyWithIdentifier {
    public override class func defaultType() -> SerializableNodeType {
        .StandardTypes.instruction.nodeType
    }
    
    public static func examples() -> [InstructionStepObject] {
        [.init(identifier: "example")]
    }
    
    public func copy(with identifier: String) -> InstructionStepObject {
        .init(identifier: identifier,
              title: title, subtitle: subtitle, detail: detail, imageInfo: imageInfo,
              shouldHideButtons: shouldHideButtons, buttonMap: buttonMap, comment: comment,
              fullInstructionsOnly: fullInstructionsOnly, spokenInstructions: spokenInstructions)
    }
}

