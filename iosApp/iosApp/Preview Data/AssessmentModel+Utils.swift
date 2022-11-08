//
//  AssessmentModel+Utils.swift
//

import Foundation
import AssessmentModel

extension AssessmentObject {
    convenience init(identifier: String, children: [Node], progressMarkers: [String]? = nil, backgroundActions: [AsyncActionConfiguration] = []) {
        self.init(identifier: identifier, children: children, versionString: nil, schemaIdentifier: nil, estimatedMinutes: 0, backgroundActions: backgroundActions)
        self.progressMarkers = progressMarkers ?? children.map { $0.identifier }
    }
}

extension FetchableImage {
    convenience init(_ imageName: String) {
        self.init(imageName: imageName, label: nil, imagePlacement: nil, size: nil, decoderBundle: Bundle.module, bundleIdentifier: nil, packageName: nil, rawFileExtension: nil, versionString: nil)
    }
}

extension InstructionStepObject {
    convenience init(identifier: String, title: String, detail: String, imageInfo: ImageInfo) {
        self.init(identifier:title, imageInfo:imageInfo, fullInstructionsOnly: false)
        self.title = title
        self.detail = detail
    }
}

extension PermissionStepObject {
    convenience init(identifier: String,  permissionType: PermissionType, title: String, detail: String, imageInfo: ImageInfo) {
        self.init(identifier: identifier,
                  permissionType: permissionType,
                  imageInfo: imageInfo,
                  optional: true,
                  requiresBackground: false,
                  reason: nil,
                  restrictedMessage: nil,
                  deniedMessage: nil)
        self.title = title
        self.detail = detail
    }
}

extension SimpleQuestionObject {
    convenience init(identifier: String, inputItem: InputItem, skipCheckbox: SkipCheckboxInputItem? = nil, title: String? = nil, subtitle: String? = nil, detail: String? = nil, optional: Bool = true) {
        self.init(identifier: identifier,
                  inputItem: inputItem,
                  skipCheckbox: skipCheckbox)
        self.title = title
        self.subtitle = subtitle
        self.detail = detail
        self.optional = optional
    }
}

extension StringTextInputItemObject {
    convenience init(_ textOptions: KeyboardOptionsObject = KeyboardOptionsObject()) {
        self.init(resultIdentifier: nil, textOptions: textOptions, regExValidator: nil)
    }
}

extension KeyboardOptionsObject {
    convenience init(autocapitalizationType: AutoCapitalizationType = AutoCapitalizationType.none,
                     autocorrectionType: AutoCorrectionType = AutoCorrectionType.default_,
                     spellCheckingType: SpellCheckingType = SpellCheckingType.default_,
                     keyboardType: KeyboardType = KeyboardType.default_) {
        self.init(isSecureTextEntry: false, autocapitalizationType: autocapitalizationType, autocorrectionType: autocorrectionType, spellCheckingType: spellCheckingType, keyboardType: keyboardType)
    }
}

