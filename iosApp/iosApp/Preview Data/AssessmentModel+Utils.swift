//
//  AssessmentModel+Utils.swift
//
//  Copyright © 2021 Sage Bionetworks. All rights reserved.
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

