//
//  TitlePageView.swift
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

public struct TitlePageView : View {
    @EnvironmentObject var pagedNavigation: PagedNavigationViewModel
    let contentInfo: OverviewStep
    
    public init(_ contentInfo: OverviewStep) {
        self.contentInfo = contentInfo
    }
    
    public var body: some View {
        VStack {
            HStack {
                Spacer()
                ExitButton(canPause: false)
            }
            Spacer()
            
            VStack(spacing: 24) {
                if let imageInfo = contentInfo.imageInfo {
                    HStack {
                        ContentImage(imageInfo)
                        Spacer()
                    }
                }
                Text(contentInfo.title ?? "")
                    .font(.stepTitle)
                    .foregroundColor(.textForeground)
                    .frame(maxWidth: .infinity, alignment: .leading)
                if let detail = contentInfo.detail {
                    Text(detail)
                        .font(.stepDetail)
                        .foregroundColor(.textForeground)
                        .frame(maxWidth: .infinity, alignment: .leading)
                }
            }
            .padding(32)
            
            Spacer()
            Button(action: pagedNavigation.goForward) {
                pagedNavigation.forwardButtonText ??
                Text("Start", bundle: .module)
            }
            .buttonStyle(NavigationButtonStyle())
            .padding(.horizontal, 32)
            .padding(.vertical, 22)
        }
    }
}

struct NavigationButtonStyle : ButtonStyle {
    @ViewBuilder public func makeBody(configuration: Self.Configuration) -> some View {
        configuration.label
            .font(.roundedButton)
            .foregroundColor(.sageWhite)
            .frame(minHeight: 48, idealHeight: 48)
            .frame(maxWidth: .infinity)
            .background(Color.sageBlack)
            .clipShape(Capsule())
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

struct TitlePageView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            TitlePageView(overviewStep)
                .environmentObject(PagedNavigationViewModel(pageCount: 5, currentIndex: 0))
                .environmentObject(AssessmentState(AssessmentObject(previewStep: overviewStep)))
        }
    }
}

fileprivate let overviewStep = OverviewStepObject(
    identifier: "overview",
    title: "Example Survey A",
    detail: "You will be shown a series of example questions. This survey has no additional instructions.",
    imageInfo: SageResourceImage(.survey))
