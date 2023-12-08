//
//  ImageInfo.swift
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
}

public protocol ImagePlacementInfo : ImageInfo {
    
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

public protocol CompositeImageInfo : ImageInfo {
    var layerCount: Int { get }
}

/// The type of the image theme. This is used to decode an `ImageInfo` using an `AssessmentFactory`. It can also be used
/// to customize the UI.
@available(*, deprecated, message: "Use @SerialName")
public struct ImageInfoType : TypeRepresentable, Codable, Equatable, Hashable {
    public let rawValue: String
    
    public init(rawValue: String) {
        self.rawValue = rawValue
    }
    
    enum Standard : String, CaseIterable {
        case fetchable, animated, sageResource
        
        var imageInfoType : ImageInfoType {
            .init(rawValue: self.rawValue)
        }
    }
    
    public static func allStandardTypes() -> [ImageInfoType] {
        return Standard.allCases.map { $0.imageInfoType }
    }
}

@available(*, deprecated, message: "Use @SerialName")
extension ImageInfoType : ExpressibleByStringLiteral {
    public init(stringLiteral value: String) {
        self.init(rawValue: value)
    }
}

@available(*, deprecated, message: "Use @SerialName")
extension ImageInfoType : DocumentableStringLiteral {
    public static func examples() -> [String] {
        return allStandardTypes().map{ $0.rawValue }
    }
}

public final class ImageInfoSerializer : GenericPolymorphicSerializer<ImageInfo>, DocumentableInterface {
    public var documentDescription: String? {
        """
        `ImageInfo` extends the UI step to include an image."
        """.replacingOccurrences(of: "\n", with: " ").replacingOccurrences(of: "  ", with: "\n")
    }
    
    public var jsonSchema: URL {
        URL(string: "\(AssessmentFactory.defaultFactory.modelName(for: self.interfaceName)).json", relativeTo: kBDHJsonSchemaBaseURL)!
    }
    
    override init() {
        super.init([
            FetchableImage.examples().first!,
            AnimatedImage.examples().first!,
            SageResourceImage.examples().first!,
        ])
    }
}

public protocol SerializableImageInfo : ImageInfo, PolymorphicTyped, PolymorphicSerializableTyped, Codable {
}

public protocol EmbeddedImageInfo : ImagePlacementInfo, DecodableBundleInfo {
}

/// `FetchableImage` is a `Codable` concrete implementation of `ImageInfo`.
@Serializable
@SerialName("fetchable")
public struct FetchableImage : SerializableImageInfo, EmbeddedImageInfo, Codable {

    public let imageName: String
    public let label: String?
    @SerialName("placementType") public let placementHint: String?
    @SerialName("size") public let imageSize: ImageSize?
    
    @Transient public var factoryBundle: ResourceBundle?
    public let bundleIdentifier: String?
    public var packageName: String?
    @SerialName("fileExtension") public let rawFileExtension: String?
    
    public var resourceName: String {
        imageName
    }
    
    public var imageIdentifier: String {
        imageName
    }
    
    public init(imageName: String, bundle: ResourceBundle? = nil, packageName: String? = nil, bundleIdentifier: String? = nil, label: String? = nil, placementHint: String? = nil, imageSize: ImageSize? = nil, rawFileExtension: String? = nil) {
        self.imageName = imageName
        self.factoryBundle = bundle
        self.bundleIdentifier = bundleIdentifier
        self.packageName = packageName
        self.label = label
        self.placementHint = placementHint
        self.imageSize = imageSize
        self.rawFileExtension = rawFileExtension
    }
}

extension FetchableImage : DocumentableStruct {
    public static func codingKeys() -> [CodingKey] {
        return CodingKeys.allCases
    }

    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        guard let key = codingKey as? CodingKeys else { return false }
        switch key {
        case .typeName, .imageName:
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
        case .typeName:
            return .init(constValue: serialTypeName)
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
@Serializable
@SerialName("animated")
public struct AnimatedImage : AnimatedImageInfo, SerializableImageInfo, EmbeddedImageInfo, Codable {

    public let imageNames: [String]
    public let label: String?
    public let animationDuration: TimeInterval
    public let animationRepeatCount: Int?
    public let bundleIdentifier: String?
    @Transient public var factoryBundle: ResourceBundle? = nil
    public var packageName: String?
    @SerialName("fileExtension") public let rawFileExtension: String?
    @SerialName("placementType") public let placementHint: String?
    @SerialName("size") public let imageSize: ImageSize?
    
    public var imageIdentifier: String { _imageIdentifier ?? imageNames.first ?? "null" }
    @SerialName("imageIdentifier") private let _imageIdentifier: String?

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
        case .typeName, .imageNames, .animationDuration:
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
        case .typeName:
            return .init(constValue: serialTypeName)
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

/// `ImageSize` is a codable struct for defining the size of a drawable.
@Serializable
public struct ImageSize : Codable {

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

