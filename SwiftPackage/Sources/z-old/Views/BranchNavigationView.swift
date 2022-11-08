//
//  BranchNavigationView.swift
//

import SwiftUI
import AssessmentModel
import SharedMobileUI

public struct BranchNavigationView : View {
    @EnvironmentObject public var branchViewModel: BranchViewModel
    
    public var body: some View {
        VStack {
            if let nodeState = branchViewModel.currentNodeState {
                buildContent(nodeState)
            }
            Spacer()
            PagedNavigationBar()
                
        }
        .environmentObject(branchViewModel.navigationViewModel)
    }
    
    @ViewBuilder func buildContent(_ nodeState: NodeState) -> some View {
        if let node = nodeState.node as? ContentNodeStep {
            InstructionStepContentView(node: node)
        }
        else {
            Text("\(nodeState.node.identifier) not handled.")
        }
    }
}

#if PREVIEW

struct BranchNavigationView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            BranchNavigationView()
                .environmentObject(previewPermissionsBranchViewModel(0))
            BranchNavigationView()
                .environment(\.sizeCategory, .extraExtraLarge)
                .previewDevice("iPhone SE (2nd generation)")
                .environmentObject(previewPermissionsBranchViewModel(1))
        }
    }
}

#endif
