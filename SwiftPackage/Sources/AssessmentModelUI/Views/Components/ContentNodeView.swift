//
//  ContentNodeView.swift
//
//

import SwiftUI
import AssessmentModel
import SharedMobileUI

public struct ContentNodeView : View {
    @SwiftUI.Environment(\.verticalPadding) var verticalPadding: CGFloat
    @SwiftUI.Environment(\.horizontalPadding) var horizontalPadding: CGFloat
    
    let contentInfo: ContentNode
    let alignment: Alignment
    
    public init(_ contentInfo: ContentNode, alignment: Alignment = .center) {
        self.contentInfo = contentInfo
        self.alignment = alignment
    }
    
    public var body: some View {
        GeometryReader { scrollViewGeometry in
            ScrollView {  // Main content for the view includes header, content, and navigation footer
                VStack(alignment: alignment.horizontal, spacing: verticalPadding) {
                    Spacer()
                    if let imageInfo = contentInfo.imageInfo, imageInfo.placement == .iconBefore {
                        ContentImage(imageInfo)
                    }
                    Text(contentInfo.title ?? "")
                        .font(.stepTitle)
                        .foregroundColor(.textForeground)
                    if let subtitle = contentInfo.subtitle {
                        Text(subtitle)
                            .font(.stepSubtitle)
                            .foregroundColor(.textForeground)
                    }
                    if let detail = contentInfo.detail {
                        Text(detail)
                            .font(.stepDetail)
                            .foregroundColor(.textForeground)
                    }
                    if let imageInfo = contentInfo.imageInfo, imageInfo.placement == .iconAfter {
                        ContentImage(imageInfo)
                    }
                    Spacer()
                }
                .padding(.horizontal, horizontalPadding)
                .frame(minHeight: scrollViewGeometry.size.height)
            }
        }
    }
}

enum ImagePlacement : String, Codable, CaseIterable {
    case iconBefore, iconAfter
}

extension ImageInfo {
    var placement: ImagePlacement {
        (self as? ImagePlacementInfo)?.placementHint.flatMap { .init(rawValue: $0) } ?? .iconBefore
    }
}

struct ContentNodeView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            ContentNodeView(exampleStep, alignment: .leading)
            ContentNodeView(exampleStep, alignment: .center)
        }
    }
}

fileprivate let exampleStep = OverviewStepObject(
    identifier: "overview",
    title: "Example Survey A",
    detail: "You will be shown a series of example questions. This survey has no additional instructions.",
    imageInfo: SageResourceImage(.default))
