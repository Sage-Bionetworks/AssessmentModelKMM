//
//  JsonElement+Utils.swift
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

