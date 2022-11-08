//
//  CompletionStepView.swift
//
//

import SwiftUI
import AssessmentModel
import SharedMobileUI

public struct CompletionView : View {
    @SwiftUI.Environment(\.verticalPadding) var verticalPadding: CGFloat
    @SwiftUI.Environment(\.horizontalPadding) var horizontalPadding: CGFloat
    @State var imageHeight: CGFloat = 0.0
    
    let title: Text
    let detail: Text
    
    public init(title: Text, detail: Text) {
        self.title = title
        self.detail = detail
    }
    
    public var body: some View {
        ZStack(alignment: .top) {
            VStack(spacing: 0) {
                Color.clear
                    .frame(height: imageHeight * 2.0 / 3.0)
                VStack(spacing: verticalPadding) {
                    title
                        .font(.stepTitle)
                    detail
                        .font(.stepDetail)
                }
                .padding(.horizontal, horizontalPadding)
                .padding(.top, imageHeight / 3.0 + verticalPadding)
                .padding(.bottom, 32)
                .foregroundColor(.textForeground)
                .background(Color.sageWhite
                                .shadow(color: .hex2A2A2A.opacity(0.1), radius: 3, x: 1, y: 1))
                .padding(.horizontal, horizontalPadding)
            }
            
            CompositedImage("completion", bundle: .module, layers: 2, isResizable: true)
                .padding(.horizontal)
                .heightReader(height: $imageHeight)
        }
    }
}

public struct CompletionStepView : View {
    @EnvironmentObject var pagedNavigation: PagedNavigationViewModel
    let step: CompletionStep
    
    public init(_ step: CompletionStep) {
        self.step = step
    }
    
    public var body: some View {
        VStack {
            Spacer()
            CompletionView(title: title(), detail: detail())
            Spacer()
            if pagedNavigation.backEnabled {
                SurveyNavigationView()
            }
            else {
                ForwardButton {
                    pagedNavigation.forwardButtonText ??
                    Text("Exit", bundle: .module)
                }
            }
        }
        .fullscreenBackground(.lightSurveyBackground)
    }
    
    func title() -> Text {
        step.title.map { Text($0) } ??
        Text("Well done!", bundle: .module)
    }
    
    func detail() -> Text {
        step.detail.map { Text($0) } ??
        Text("Thank you for being part of our study.", bundle: .module)
    }
}

struct CompletionView_Previews: PreviewProvider {
    static var previews: some View {
        CompletionStepView(exampleA)
            .environmentObject(PagedNavigationViewModel(pageCount: 5, currentIndex: 0))
            .environmentObject(AssessmentState(AssessmentObject(previewStep: exampleA)))
    }
}

fileprivate let exampleA = CompletionStepObject(identifier: "exampleA")
