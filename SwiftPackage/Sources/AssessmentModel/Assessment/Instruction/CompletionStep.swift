//
//  CompletionStep.swift
//  
//

import Foundation
import JsonModel

/// A completion step is intended to be used to mark the *end* of an assessment.
public protocol CompletionStep : Step, ContentNode {
}

open class AbstractCompletionStepObject : AbstractSpokenInstructionStepObject, CompletionStep {
    open override func shouldHideButton(_ buttonType: ButtonType, node: Node) -> Bool? {
        buttonType == .navigation(.goBackward) ? true : super.shouldHideButton(buttonType, node: node)
    }
}

public final class CompletionStepObject : AbstractCompletionStepObject, Encodable, DocumentableStruct, CopyWithIdentifier {
    public override class func defaultType() -> SerializableNodeType {
        .StandardTypes.completion.nodeType
    }
    
    public static func examples() -> [CompletionStepObject] {
        [.init(identifier: "example")]
    }
    
    public func copy(with identifier: String) -> CompletionStepObject {
        .init(identifier: identifier, copyFrom: self)
    }
}
