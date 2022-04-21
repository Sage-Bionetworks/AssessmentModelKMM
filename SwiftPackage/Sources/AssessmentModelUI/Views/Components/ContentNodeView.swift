//
//  ContentNodeView.swift
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
import SharedMobileUI

public struct ContentNodeView : View {
    let contentInfo: ContentNode
    let alignment: Alignment
    
    public init(_ contentInfo: ContentNode, alignment: Alignment = .center) {
        self.contentInfo = contentInfo
        self.alignment = alignment
    }
    
    public var body: some View {
        GeometryReader { scrollViewGeometry in
            ScrollView {  // Main content for the view includes header, content, and navigation footer
                VStack(spacing: 24) {
                    Spacer()
                    if let imageInfo = contentInfo.imageInfo {
                        HStack {
                            ContentImage(imageInfo)
                            Spacer()
                        }
                    }
                    Text(contentInfo.title ?? "")
                        .font(.stepTitle)
                        .foregroundColor(.textForeground)
                        .frame(maxWidth: .infinity, alignment: alignment)
                    if let detail = contentInfo.detail {
                        Text(detail)
                            .font(.stepDetail)
                            .foregroundColor(.textForeground)
                            .frame(maxWidth: .infinity, alignment: alignment)
                    }
                    Spacer()
                }
                .padding(.horizontal, 32)
                .frame(minHeight: scrollViewGeometry.size.height)
            }
        }
    }
}

public struct ContentImage : View {
    let imageInfo: ImageInfo
    public init(_ imageInfo: ImageInfo) {
        self.imageInfo = imageInfo
    }
    
    public var body: some View {
        if let sageImage = (imageInfo as? SageResourceImage)?.name {
            switch sageImage {
            case .survey:
                CompositedImage("survey", bundle: .module, layers: 4)
            }
        }
        else {
            Image(imageInfo.imageName, bundle: imageInfo.bundle)
        }
    }
}

struct ContentNodeView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            ContentNodeView(exampleStep, alignment: .leading)
        }
    }
}

fileprivate let exampleStep = OverviewStepObject(
    identifier: "overview",
    title: "Example Survey A",
    detail: "You will be shown a series of example questions. This survey has no additional instructions.",
    imageInfo: SageResourceImage(.survey))
