//
//  CountdownStep.swift
//
//

import Foundation
import JsonModel

public protocol CountdownStep : InstructionStep {
    var duration: TimeInterval { get }
}

open class AbstractCountdownStepObject : AbstractInstructionStepObject, CountdownStep {
    private enum CodingKeys : String, OrderedEnumCodingKey, OpenOrderedCodingKey {
        case duration
        var relativeIndex: Int { 6 }
    }
    
    public let duration: TimeInterval
    
    // MARK: Initializers and serialization
    
    public init(identifier: String, copyFrom object: AbstractCountdownStepObject) {
        self.duration = object.duration
        super.init(identifier: identifier, copyFrom: object)
    }
    
    public init(identifier: String, duration: TimeInterval,
                title: String? = nil, subtitle: String? = nil, detail: String? = nil, imageInfo: ImageInfo? = nil,
                shouldHideButtons: Set<ButtonType>? = nil, buttonMap: [ButtonType : ButtonActionInfo]? = nil, comment: String? = nil, nextNode: NavigationIdentifier? = nil,
                fullInstructionsOnly: Bool? = nil, spokenInstructions: [SpokenInstructionKey : String]? = nil) {
        self.duration = duration
        super.init(identifier: identifier,
                   title: title, subtitle: subtitle, detail: detail, imageInfo: imageInfo,
                   shouldHideButtons: shouldHideButtons, buttonMap: buttonMap, comment: comment, nextNode: nextNode,
                   fullInstructionsOnly: fullInstructionsOnly, spokenInstructions: spokenInstructions
        )
    }
    
    public required init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.duration = try container.decode(TimeInterval.self, forKey: .duration)
        try super.init(from: decoder)
    }

    open override func encode(to encoder: Encoder) throws {
        try super.encode(to: encoder)
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(self.duration, forKey: .duration)
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
        case .duration:
            return .init(propertyType: .primitive(.number), propertyDescription:
                            "The duration of the coundown.")
        }
    }
}

public final class CountdownStepObject : AbstractCountdownStepObject, Encodable, DocumentableStruct, CopyWithIdentifier {
    public override class func defaultType() -> SerializableNodeType {
        .StandardTypes.countdown.nodeType
    }
    
    public static func examples() -> [CountdownStepObject] {
        [.init(identifier: "example", duration: 3.0)]
    }
    
    public func copy(with identifier: String) -> CountdownStepObject {
        .init(identifier: identifier, copyFrom: self)
    }
}
