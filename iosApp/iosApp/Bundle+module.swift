//
//  Bundle+module.swift
//  Research
//

import Foundation

// Swift Packages have an internal static property defined on the Bundle to access
// bundle resources. This code file is *not* included in the swift packages and can
// allow building either a dynamic framework *or* a Swift Package using the same code
// files. syoung 11/05/2020

class BundleResource {
}

extension Bundle {
    static let module = Bundle(for: BundleResource.self)
}
