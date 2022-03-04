//
//  ErrorResultObject.swift
//  
//
//  Copyright © 2017-2022 Sage Bionetworks. All rights reserved.
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

/// `ErrorResult` is a result that holds information about an error.
public protocol ErrorResult : ResultData {
    
    /// A description associated with an `NSError`.
    var errorDescription: String { get }
    
    /// A domain associated with an `NSError`.
    var errorDomain: String { get }
    
    /// The error code associated with an `NSError`.
    var errorCode: Int { get }
}

/// `ErrorResultObject` is a result that holds information about an error.
public struct ErrorResultObject : SerializableResultData, ErrorResult, MultiplatformResultData, Equatable {
    private enum CodingKeys : String, OrderedEnumCodingKey {
        case serializableType="type", identifier, startDateTime = "startDate", endDateTime = "endDate", errorDescription, errorDomain, errorCode
    }
    public private(set) var serializableType: SerializableResultType = .StandardTypes.error.resultType
    
    public let identifier: String
    public var startDateTime: Date
    public var endDateTime: Date?
    
    /// A description associated with an `NSError`.
    public let errorDescription: String
    
    /// A domain associated with an `NSError`.
    public let errorDomain: String
    
    /// The error code associated with an `NSError`.
    public let errorCode: Int
    
    /// Initialize using a description, domain, and code.
    /// - parameters:
    ///     - identifier: The identifier for the result.
    ///     - description: The description of the error.
    ///     - domain: The error domain.
    ///     - code: The error code.
    public init(identifier: String, description: String, domain: String, code: Int, startDate: Date = Date(), endDate: Date? = nil) {
        self.identifier = identifier
        self.startDateTime = startDate
        self.endDateTime = endDate
        self.errorDescription = description
        self.errorDomain = domain
        self.errorCode = code
    }
    
    /// Initialize using an error.
    /// - parameters:
    ///     - identifier: The identifier for the result.
    ///     - error: The error for the result.
    public init(identifier: String, error: Error) {
        self.identifier = identifier
        self.startDateTime = Date()
        self.errorDescription = (error as NSError).localizedDescription
        self.errorDomain = (error as NSError).domain
        self.errorCode = (error as NSError).code
    }
    
    public func deepCopy() -> ErrorResultObject { self }
}

extension ErrorResultObject : DocumentableStruct {
    public static func codingKeys() -> [CodingKey] {
        return CodingKeys.allCases
    }
    
    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        true
    }
    
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not recognized for this class")
        }
        switch key {
        case .serializableType:
            return .init(constValue: SerializableResultType.StandardTypes.error.resultType)
        case .identifier:
            return .init(propertyType: .primitive(.string))
        case .startDateTime, .endDateTime:
            return .init(propertyType: .format(.dateTime))
        case .errorDomain:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "The error domain.")
        case .errorDescription:
            return .init(propertyType: .primitive(.string), propertyDescription:
                            "The description of the error.")
        case .errorCode:
            return .init(propertyType: .primitive(.integer), propertyDescription:
                            "The error code.")
        }
    }
    
    public static func examples() -> [ErrorResultObject] {
        return [ErrorResultObject(identifier: "errorResult", description: "example error", domain: "ExampleDomain", code: 1)]
    }
}
