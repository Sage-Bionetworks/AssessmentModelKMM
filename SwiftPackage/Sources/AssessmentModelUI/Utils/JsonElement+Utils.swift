//
//  JsonElement+Utils.swift
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
import JsonModel
import AssessmentModel

public extension AssessmentModel.Kotlinx_serialization_jsonJsonElement {
    
    func jsonElement() -> JsonModel.JsonElement {
        let encoder = AssessmentModel.JsonElementEncoder(jsonElement: self)
        do {
            let string = try encoder.encodeObject()
            guard let data = string.data(using: .utf8) else {
                return .null
            }
            let decoder = JSONDecoder()
            return try decoder.decode(JsonModel.JsonElement.self, from: data)
        } catch let err {
            debugPrint("Failed to convert to json element: \(err)")
            return .null
        }
    }
    
    static func new(from jsonElement: JsonModel.JsonElement) -> AssessmentModel.Kotlinx_serialization_jsonJsonElement? {
        do {
            let encodedData = try jsonElement.jsonEncodedData()
            guard let encodedString = String(data: encodedData, encoding: .utf8) else {
                return nil
            }
            let decoder = AssessmentModel.JsonElementDecoder(jsonString: encodedString)
            return try decoder.decodeObject()
        } catch let err {
            debugPrint("Failed to convert to json element: \(err)")
            return nil
        }
    }
}

