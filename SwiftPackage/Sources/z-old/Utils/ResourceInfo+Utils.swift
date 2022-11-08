//
//  ResourceInfo+Utils.swift
//

import Foundation
import AssessmentModel

extension AssessmentModel.ResourceInfo {
    var bundle: Bundle? {
        (self.bundleIdentifier != nil) ? Bundle(identifier: bundleIdentifier!) : (self.decoderBundle as? Bundle)
    }
}
