//
//  SurveyNavigationView.swift
//
//

import SwiftUI
import SharedMobileUI

public struct SurveyNavigationView: View {
    @SwiftUI.Environment(\.verticalPadding) var verticalPadding: CGFloat
    @SwiftUI.Environment(\.horizontalPadding) var horizontalPadding: CGFloat
    @EnvironmentObject private var viewModel: PagedNavigationViewModel
    
    public init() {
    }
    
    public var body: some View {
        PagedNavigationBar(showsDots: false)
            .padding(.horizontal, horizontalPadding)
            .padding(.vertical, verticalPadding)
    }
}

struct SurveyNavigationView_Previews: PreviewProvider {
    static var previews: some View {
        SurveyNavigationView()
            .environmentObject(PagedNavigationViewModel(pageCount: 5, currentIndex: 2))
    }
}
