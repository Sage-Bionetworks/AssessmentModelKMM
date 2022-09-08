//
//  ContentImage.swift
//
//
//  Copyright © 2022 Sage Bionetworks. All rights reserved.
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

import SwiftUI
import AssessmentModel

/// The content image is an image wrapper that can draw either a tinted composited image composed of multiple layers
/// defined in the image bundle, a single image resource, or an asyncronously fetched image. The purpose of this struct
/// is to allow arrays and dictionaries to hold the image representation and take advantage of the scrolling performance
/// enhancements provided by SwiftUI while the backing image may *not* be a simple resource.
public struct ContentImage : View, Identifiable {
    public let id: String
    let imageName: String
    let label: Text
    let bundle: Bundle?
    let layerCount: Int
    let url: URL?
    private var animatedImageInfo: AnimatedImageInfo?
    
    /// The constructor to use when creating the image using an image info.
    ///
    /// - Parameters:
    ///   - imageInfo: The image info to use to get the resource.
    ///   - label: The accessibility label.
    public init(_ imageInfo: ImageInfo, label: Text? = nil) {
        let label = label ?? imageInfo.label.map { Text($0) }
        if let iconKey = (imageInfo as? SageResourceImage)?.name {
            self.init(icon: iconKey, label: label)
        }
        else if let animatedImageInfo = imageInfo as? AnimatedImage {
            self.init(animatedImageInfo: animatedImageInfo, label: label)
        }
        else {
            self.init(imageInfo.imageName, bundle: imageInfo.bundle, layers: (imageInfo as? CompositeImageInfo)?.layerCount ?? 1, label: label)
        }
    }
    
    /// General-purpose constructor for use with images defined in a given bundle.
    ///
    /// - Parameters:
    ///   - imageName: The name of the resource.
    ///   - bundle: The bundle for the resource or `main` if nil.
    ///   - layers: The number of layers. If this number is greater than one then the composite images are expected to follow the pattern used by `CompositedImage`.
    ///   - label: The accessibility label.
    public init(_ imageName: String, bundle: Bundle? = nil, layers: Int = 1, label: Text? = nil) {
        self.id = "\(bundle?.bundleIdentifier ?? "main").\(imageName)"
        self.imageName = imageName
        self.bundle = bundle ?? .main
        self.label = label ?? Text(imageName)
        self.layerCount = layers
        self.url = nil
    }
    
    /// The constructor to use with animations.
    ///
    /// - Parameters:
    ///   - animatedImageInfo: The animated image info to use to get the resource.
    ///   - label: The accessibility label.
    public init(animatedImageInfo: AnimatedImage, bundle: Bundle? = nil, label: Text? = nil, layers: Int = 1) {
        self.init(animatedImageInfo.imageName, bundle: animatedImageInfo.bundle, label: label)
        self.animatedImageInfo = animatedImageInfo
    }
    
    /// The constructor to use when creating the view using a default icon key.
    ///
    /// - Parameters:
    ///   - iconKey: The key name for the icon to use that is defined within the `AssessmentModel` framework.
    ///   - label: The accessibility label.
    public init(icon iconKey: SageResourceImage.Name, label: Text? = nil, isList: Bool = false) {
        self.id = "SageResourceImage.\(iconKey.rawValue)"
        self.imageName = iconKey.imageName(isList: isList)
        self.label = label ?? iconKey.label
        self.bundle = .module
        self.layerCount = iconKey.layerCount(isList: isList)
        self.url = nil
    }
    
    /// The constructor to use if fetching an image using a URL.
    ///
    /// - Parameters:
    ///   - url: The URL for the image to fetch asynchronously.
    ///   - placeholder: The key name for the icon to use as a placeholder.
    ///   - label: The accessibility label.
    @available(iOS 15.0, macOS 12.0, tvOS 15.0, watchOS 8.0, *)
    public init(url: URL, placeholder iconKey: SageResourceImage.Name = .default, label: Text? = nil, isList: Bool = false) {
        self.id = url.absoluteString
        self.imageName = iconKey.imageName(isList: isList)
        self.label = label ?? iconKey.label
        self.bundle = .module
        self.layerCount = iconKey.layerCount(isList: isList)
        self.url = url
    }
    
    public var body: some View {
        if #available(iOS 15.0, macOS 12.0, tvOS 15.0, watchOS 8.0, *),
           let url = url {
            AsyncImage(url: url) { phase in
                if let image = phase.image {
                    image
                }
                else {
                    content()
                }
            }
        }
        else {
            content()
        }
    }
    
    @ViewBuilder func content() -> some View {
        if layerCount > 1 {
            CompositedImage(imageName, bundle: bundle, layers: layerCount)
                .accessibility(label: label)
        }
        else if let animationInfo = animatedImageInfo  {
            AnimationView(animatedImageInfo: animationInfo)
                .accessibility(label: label)
        }
        else {
            Image(imageName, bundle: bundle, label: label)
        }
    }
}

let animatedImageExample = AnimatedImage.examples()

struct ContentImagePreview : View {
    var body: some View {
        ScrollView {
            ContentImage("survey.1", bundle: .module)
            ContentImage(FetchableImage(imageName: "survey.1", bundle: Bundle.module))
            ContentImage(animatedImageInfo: animatedImageExample[1])
            ForEach(SageResourceImage.Name.allCases) { name in
                ContentImage(icon: name)
            }
        }
    }
}

struct ContentImage_Previews: PreviewProvider {
    static var previews: some View {
        ContentImagePreview()
    }
}

extension SageResourceImage.Name {
    public var label: Text {
        Text("Survey", bundle: .module)
    }
    
    public func imageName(isList: Bool = false) -> String {
        isList ? "list_\(rawValue)" : "title_\(rawValue)"
    }
    
    public func layerCount(isList: Bool = false) -> Int {
        if isList {
            return 1
        }
        else {
            switch self {
            case .exit:
                return 3
            default:
                return 2
            }
        }
    }
}

