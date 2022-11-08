//
//  SageResourceImage.swift
//

import Foundation
import JsonModel

/// This allows customized image compositing that is required on iOS only.
public struct SageResourceImage : SerializableImageInfo {
    private enum CodingKeys : String, OrderedEnumCodingKey {
        case serializableType = "type", _name = "imageName", label
    }
    public private(set) var serializableType: ImageInfoType = .Standard.sageResource.imageInfoType
    
    public var imageName: String {
        self.name.map { "title_\($0.rawValue)" } ?? _name
    }
    private let _name: String
    
    public var label: String?
    
    public init(_ name: Name, label: String? = nil) {
        self._name = name.rawValue
        self.label = label
    }
    
    public enum Name : String, StringEnumSet, DocumentableStringEnum, Identifiable {
        case `default`
        case cognition
        case dayToDay = "day_to_day"
        case demographics
        case energy
        case environment
        case excercise
        case exit
        case finance
        case food
        case health
        case leisure
        case medicine
        case mentalHealth = "mental_health"
        case mood
        case pain
        case qualityOfLife = "quality_of_life"
        case social
        case screening
        case sleep
        
        public var id: String {
            self.rawValue
        }
    }
    
    public var name: Name? {
        .init(rawValue: _name)
    }

    public var imageIdentifier: String {
        _name
    }
    
    public var bundleIdentifier: String? { "AssessmentModelUI" }
    public var factoryBundle: ResourceBundle? {
        get { nil }
        set {}
    }
    public var packageName: String? {
        get { nil }
        set {}
    }
}

extension SageResourceImage : DocumentableStruct {
    public static func codingKeys() -> [CodingKey] {
        return CodingKeys.allCases
    }

    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        guard let key = codingKey as? CodingKeys else { return false }
        switch key {
        case .serializableType, ._name:
            return true
        default:
            return false
        }
    }
    
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not recognized for this class")
        }
        switch key {
        case .serializableType:
            return .init(constValue: ImageInfoType.Standard.sageResource.imageInfoType)
        case ._name:
            return .init(propertyType: .reference(Name.documentableType()), propertyDescription:
                            "The image name for the image to draw.")
        case .label:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "A caption or label to display for the image in a localized string.")
        }
    }
    
    public static func examples() -> [SageResourceImage] {
        let imageA = SageResourceImage(.default)
        return [imageA]
    }
}
