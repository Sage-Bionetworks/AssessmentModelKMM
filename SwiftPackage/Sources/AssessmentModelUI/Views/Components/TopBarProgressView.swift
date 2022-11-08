//
//  TopBarProgressView.swift
//  
//

import SwiftUI
import SharedMobileUI
import AssessmentModel

public struct TopBarProgressView : View {
    @SwiftUI.Environment(\.surveyTintColor) var surveyTint: Color
    @EnvironmentObject var pagedNavigation: PagedNavigationViewModel
    
    public var body: some View {
        GeometryReader { geometry in
            ZStack(alignment: .leading) {
                Rectangle()
                    .fill(Color.progressBackground)
                Rectangle()
                    .fill(surveyTint)
                    .frame(width: geometry.size.width * pagedNavigation.fraction)
                    .animation(.easeOut, value: pagedNavigation.fraction)
            }
            .frame(maxWidth: .infinity)
            .frame(height: 4)
        }
        .opacity(pagedNavigation.progressHidden ? 0 : 1)
    }
}
