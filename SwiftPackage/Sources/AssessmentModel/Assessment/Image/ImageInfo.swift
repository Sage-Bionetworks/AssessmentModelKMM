//
//  ImageInfo.swift
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

/// `ImageInfo` includes information required to display an image.
public protocol ImageInfo : ResourceInfo {
    
    /// The image name for the image to draw. This can be either the name of the first image in an
    /// animated series or the resource name used to fetch the image.
    var imageName: String { get }
    
    /// A caption or label to display for the image in a localized string.
    var label: String? { get }
    
    /// A unique identifier that can be used to validate that the image shown in a reusable view
    /// is the same image as the one fetched.
    var imageIdentifier: String { get }
    
    /// The preferred placement of the image. Default placement is `iconBefore` if undefined.
    var placementHint: String? { get }
    
    /// The image size. If `.zero` or `nil` then default sizing will be used.
    var imageSize: ImageSize? { get }
}

/// `AnimatedImageInfo` defines a series of images that can be animated.
public protocol AnimatedImageInfo : ImageInfo {
    
    /// The animation duration.
    var animationDuration: TimeInterval { get }
    
    /// This is used to set how many times the animation should be repeated where `0` means infinite.
    var animationRepeatCount: Int? { get }
    
    /// The list of the names of the images to animate through in order.
    var imageNames: [String] { get }
}

public extension AnimatedImageInfo {
    var imageName: String {
        imageIdentifier
    }
}

/// The type of the image theme. This is used to decode a `ImageInfo` using a `AssessmentFactory`. It can also be used
/// to customize the UI.
public struct ImageInfoType : TypeRepresentable, Codable, Equatable, Hashable {
    public let rawValue: String
    
    public init(rawValue: String) {
        self.rawValue = rawValue
    }
    
    /// Defaults to creating a ``FetchableImageInfoObject``.
    public static let fetchable: ImageInfoType = "fetchable"
    
    /// Defaults to creating a ``AnimatedImageInfoObject``.
    public static let animated: ImageInfoType = "animated"
    
    public static func allStandardTypes() -> [ImageInfoType] {
        return [.fetchable, .animated]
    }
}

extension ImageInfoType : ExpressibleByStringLiteral {
    public init(stringLiteral value: String) {
        self.init(rawValue: value)
    }
}

extension ImageInfoType : DocumentableStringLiteral {
    public static func examples() -> [String] {
        return allStandardTypes().map{ $0.rawValue }
    }
}

public final class ImageInfoSerializer : AbstractPolymorphicSerializer, PolymorphicSerializer {
    public var documentDescription: String? {
        """
        `ImageInfo` extends the UI step to include an image."
        """.replacingOccurrences(of: "\n", with: " ").replacingOccurrences(of: "  ", with: "\n")
    }
    
    public var jsonSchema: URL {
        URL(string: "\(AssessmentFactory.defaultFactory.modelName(for: self.interfaceName)).json", relativeTo: kSageJsonSchemaBaseURL)!
    }
    
    override init() {
        examples = [
            FetchableImage.examples().first!,
            AnimatedImage.examples().first!,
        ]
    }
    
    public private(set) var examples: [ImageInfo]
    
    public override class func typeDocumentProperty() -> DocumentProperty {
        .init(propertyType: .reference(ImageInfoType.documentableType()))
    }
    
    public func add(_ example: SerializableImageInfo) {
        if let idx = examples.firstIndex(where: {
            ($0 as! PolymorphicRepresentable).typeName == example.typeName }) {
            examples.remove(at: idx)
        }
        examples.append(example)
    }
}

public protocol SerializableImageInfo : ImageInfo, PolymorphicRepresentable, Encodable {
    var serializableType: ImageInfoType { get }
}

public extension SerializableImageInfo {
    var typeName: String { return serializableType.rawValue }
}

/// `FetchableImage` is a `Codable` concrete implementation of `ImageInfo`.
public struct FetchableImage : ImageInfo, SerializableImageInfo {
    private enum CodingKeys : String, OrderedEnumCodingKey {
        case serializableType = "type", imageName, label, bundleIdentifier, packageName, rawFileExtension = "fileExtension", placementHint = "placementType", imageSize = "size"
    }
    public private(set) var serializableType: ImageInfoType = .fetchable
    
    public let imageName: String
    public let label: String?
    public let rawFileExtension: String?
    public var factoryBundle: ResourceBundle?
    public let bundleIdentifier: String?
    public var packageName: String?
    public let placementHint: String?
    public let imageSize: ImageSize?
    
    public var resourceName: String {
        imageName
    }
    
    public var imageIdentifier: String {
        imageName
    }
    
    public init(imageName: String, bundle: ResourceBundle? = nil, packageName: String? = nil, bundleIdentifier: String? = nil, label: String? = nil, placementHint: String? = nil, imageSize: ImageSize? = nil) {
        let splitFile = imageName.splitFilename()
        self.imageName = splitFile.resourceName
        self.rawFileExtension = splitFile.fileExtension
        self.factoryBundle = bundle
        self.bundleIdentifier = bundleIdentifier
        self.packageName = packageName
        self.label = label
        self.placementHint = placementHint
        self.imageSize = imageSize
    }
}

extension FetchableImage : DocumentableStruct {
    public static func codingKeys() -> [CodingKey] {
        return CodingKeys.allCases
    }

    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        guard let key = codingKey as? CodingKeys else { return false }
        switch key {
        case .serializableType, .imageName:
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
            return .init(constValue: ImageInfoType.fetchable)
        case .imageName:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "The image name for the image to draw.")
        case .label:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "A caption or label to display for the image in a localized string.")
        case .bundleIdentifier:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "The identifier of the bundle within which the resource is embedded on Apple platforms.")
        case .packageName:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "The package within which the resource is embedded on Android platforms.")
        case .rawFileExtension:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "For a raw resource file, this is the file extension for getting at the resource.")
        case .placementHint:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "A hint to the preferred placement of the image.")
        case .imageSize:
            return .init(propertyType: .reference(ImageSize.documentableType()),
                         propertyDescription: "The preferred size (in pixels) of the image.")
        }
    }
    
    public static func examples() -> [FetchableImage] {
        let imageA = FetchableImage(imageName: "blueDog")
        let imageB = FetchableImage(imageName: "redCat.jpeg",
                                                         bundle: nil,
                                                         packageName: "org.example.sharedresources",
                                                         bundleIdentifier: "org.example.SharedResources")
        return [imageA, imageB]
    }
}

/// `AnimatedImage` is a `Codable` concrete implementation of `AnimatedImageInfo`.
public struct AnimatedImage : AnimatedImageInfo, SerializableImageInfo {
    private enum CodingKeys: String, OrderedEnumCodingKey {
        case serializableType = "type", imageNames, animationDuration, animationRepeatCount, label, bundleIdentifier, packageName, rawFileExtension = "fileExtension", placementHint = "placementType", imageSize = "size", _imageIdentifier = "imageIdentifier"
    }
    public private(set) var serializableType: ImageInfoType = .animated
    
    public let imageNames: [String]
    public let label: String?
    public let animationDuration: TimeInterval
    public let animationRepeatCount: Int?
    public let bundleIdentifier: String?
    public var factoryBundle: ResourceBundle? = nil
    public var packageName: String?
    public let rawFileExtension: String?
    public let placementHint: String?
    public let imageSize: ImageSize?
    
    public var imageIdentifier: String { _imageIdentifier ?? imageNames.first ?? "null" }
    private let _imageIdentifier: String?

    /// Default initializer.
    public init(imageNames: [String], animationDuration: TimeInterval, bundleIdentifier: String? = nil, animationRepeatCount: Int = 0, label: String? = nil, placementHint: String? = nil, imageSize: ImageSize? = nil) {
        self.imageNames = imageNames
        self.bundleIdentifier = bundleIdentifier
        self.animationDuration = animationDuration
        self.animationRepeatCount = animationRepeatCount
        self.rawFileExtension = nil
        self.label = label
        self._imageIdentifier = nil
        self.placementHint = placementHint
        self.imageSize = imageSize
    }
}

extension AnimatedImage : DocumentableStruct {
    public static func codingKeys() -> [CodingKey] {
        return CodingKeys.allCases
    }

    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        guard let key = codingKey as? CodingKeys else { return false }
        switch key {
        case .serializableType, .imageNames, .animationDuration:
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
            return .init(constValue: ImageInfoType.animated)
        case .imageNames:
            return .init(propertyType: .primitiveArray(.string), propertyDescription:
                            "The list of the names of the images to animate through in order.")
        case .animationDuration:
            return .init(propertyType: .primitive(.number), propertyDescription:
                            "The animation duration.")
        case .animationRepeatCount:
            return .init(propertyType: .primitive(.integer), propertyDescription:
                            "This is used to set how many times the animation should be repeated where `0` means infinite.")
        case .label:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "A caption or label to display for the image in a localized string.")
        case .bundleIdentifier:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "The identifier of the bundle within which the resource is embedded on Apple platforms.")
        case .packageName:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "The package within which the resource is embedded on Android platforms.")
        case .rawFileExtension:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "For a raw resource file, this is the file extension for getting at the resource.")
        case .placementHint:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "A hint to the preferred placement of the image.")
        case .imageSize:
            return .init(propertyType: .reference(ImageSize.documentableType()), propertyDescription:
                            "The preferred size (in pixels) of the image.")
        case ._imageIdentifier:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "An identifier for the image.")
        }
    }
    
    public static func examples() -> [AnimatedImage] {
        let imageA = AnimatedImage(imageNames: ["blueDog1", "blueDog2", "blueDog3"], animationDuration: 2)
        let imageB = AnimatedImage(imageNames: ["redCat1", "redCat2", "redCat3"], animationDuration: 2, bundleIdentifier: "org.example.SharedResources")
        return [imageA, imageB]
    }
}

extension String {
    
    /// Convenience utility for spliting a string that represents a file into it's name and extension.
    fileprivate func splitFilename(defaultExtension: String? = nil) -> (resourceName: String, fileExtension: String?) {
        var resource = self
        var ext = defaultExtension
        let split = self.components(separatedBy: ".")
        if split.count == 2 {
            ext = split.last!
            resource = split.first!
        }
        return (resource, ext)
    }
}

/// `ImageSize` is a codable struct for defining the size of a drawable.
public struct ImageSize : Codable {
    private enum CodingKeys : String, CodingKey, CaseIterable {
        case width, height
    }
    public let width: Double
    public let height: Double
    
    public init(width: Double, height: Double) {
        self.width = width
        self.height = height
    }
}

extension ImageSize : DocumentableStruct {

    public static func codingKeys() -> [CodingKey] {
        CodingKeys.allCases
    }
    
    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        true
    }
    
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let _ = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not recognized for this class")
        }
        return .init(propertyType: .primitive(.number))
    }
    
    public static func examples() -> [ImageSize] {
        [ImageSize(width: 10.0, height: 20.0)]
    }
}

