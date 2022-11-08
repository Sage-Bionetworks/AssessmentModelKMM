//
//  InstructionStepView.swift
//

import SwiftUI
import AssessmentModel
import SharedMobileUI

struct InstructionStepContentView : View {
    let node: ContentNodeStep
    
    var body: some View {
        InstructionContentView(title: node.titleKey,
                               detail: node.detailKey,
                               imageName: node.imageName,
                               bundle: node.bundle)
    }
}

extension ContentNodeStep {
    fileprivate var titleKey: LocalizedStringKey {
        LocalizedStringKey(title ?? "")
    }
    fileprivate var detailKey: LocalizedStringKey {
        LocalizedStringKey(detail ?? "")
    }
    fileprivate var imageName: String? {
        self.imageInfo?.imageName
    }
    fileprivate var bundle: Bundle? {
        self.imageInfo?.bundle
    }
}

#if PREVIEW

struct InstructionStepContentView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            InstructionStepContentView(node: previewPermissionsNodes[0])
            InstructionStepContentView(node: previewPermissionsNodes[1])
            InstructionStepContentView(node: previewPermissionsNodes[2])
        }
    }
}

#endif
