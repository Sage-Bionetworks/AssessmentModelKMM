//
//  TitlePageView.swift
//
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
            
            ContentNodeView(contentInfo, alignment: .leading)
            
            ForwardButton {
                pagedNavigation.forwardButtonText ??
                Text("Start", bundle: .module)
            }
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
    imageInfo: SageResourceImage(.default))
