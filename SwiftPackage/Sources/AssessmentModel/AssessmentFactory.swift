//
//  AssessmentFactory.swift
//  
//

import Foundation
import JsonModel
import ResultModel
import MobilePassiveData

/// A lightweight protocol for copying objects with a new identifier.
public protocol CopyWithIdentifier {
    var identifier: String { get }
    
    /// Copy the step to a new instance with the given identifier, but otherwise, equal.
    /// - parameter identifier: The new identifier.
    func copy(with identifier: String) -> Self
}

open class AssessmentFactory : MobilePassiveDataFactory {
    
    public let buttonActionSerializer = ButtonActionSerializer()
    public let imageInfoSerializer = ImageInfoSerializer()
    public let textInputItemSerializer = TextInputItemSerializer()
    public let nodeSerializer = NodeSerializer()
    public let assessmentSerializer = AssessmentSerializer()
    
    public required init() {
        super.init()
        
        self.registerSerializer(imageInfoSerializer)
        self.registerSerializer(buttonActionSerializer)
        self.registerSerializer(textInputItemSerializer)
        self.registerSerializer(nodeSerializer)
        self.registerSerializer(assessmentSerializer)
        
        self.registerRootObject(AssessmentObject())
        self.registerRootObject(AssessmentResultObject())
    }
    
    open override func mapDecodedObject<T>(_ type: T.Type, object: Any, codingPath: [CodingKey]) throws -> T {
        try super.mapDecodedObject(type, object: (object as? TransformableNode)?.node ?? object, codingPath: codingPath)
    }
}
