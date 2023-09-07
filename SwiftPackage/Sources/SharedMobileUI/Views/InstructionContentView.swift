//
//  InstructionContentView.swift
//  
//

import SwiftUI

/// A view for displaying content that includes a title, detail, and image.
public struct InstructionContentView: View {
    
    /// The fontRatio determines how big the image is (or whether or not it is hidden) based on the accessiblity size.
    @ScaledMetric private var fontRatio: CGFloat = 1
    
    private let title: LocalizedStringKey
    private let detail: LocalizedStringKey
    private let imageName: String?
    private let bundle: Bundle?
    
    /// Initializer.
    /// - Parameters:
    ///   - title: The localized string key for the title of the view.
    ///   - detail: The localized string key for the detail of the view.
    ///   - imageName: The image name for the image to display in the view.
    ///   - bundle: The bundle for the localized strings and images.
    public init(title: LocalizedStringKey,
                detail: LocalizedStringKey,
                imageName: String? = nil,
                bundle: Bundle? = nil) {
        self.title = title
        self.detail = detail
        self.imageName = imageName
        self.bundle = bundle
    }
    
    public var body: some View {
        ScrollView {
            // Only show the image if the font size is not extra large.
            if let imageName = self.imageName,
               fontRatio < 1.5 {
                Image(imageName, bundle: bundle)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(height: 160.0 / fontRatio, alignment: .center)
                    .padding(.top, 10.0)
            }
            Text(title, bundle: bundle)
                .font(DesignSystem.fontRules.headerFont(at: 1))
                .padding(.top, 44.0)
                .padding(.bottom, 14.0)
                .padding(.horizontal, 60.0)
            Text(detail, bundle: bundle)
                .font(DesignSystem.fontRules.bodyFont(at: 1, isEmphasis: false))
                .padding(.horizontal, 56.0)
                .padding(.bottom, 14.0)
                .lineLimit(nil)
                .fixedSize(horizontal: false, vertical: true)
                .frame(maxHeight: .infinity)
        }
    }
}

extension Text {
    init(_ key: LocalizedStringKey, bundle: Bundle?) {
        self.init(key, tableName: nil, bundle: bundle, comment: nil)
    }
}

struct InstructionContentView_Previews: PreviewProvider {
    static var previews: some View {
        InstructionContentView(title: "Hello, World!", detail: "This is some detail about this view")
    }
}
