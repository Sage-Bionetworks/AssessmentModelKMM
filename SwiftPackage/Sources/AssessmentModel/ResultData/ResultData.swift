//
//  ResultData.swift
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

/// `ResultData` is the base protocol for an object that stores data.
///
///  syoung 12/09/2020 `ResultData` is included as a part of the JsonModel module to allow
///  progress and additions to be made to the frameworks used by SageResearch that are independant
///  of the version of https://github.com/Sage-Bionetworks/SageResearch-Apple.git that is
///  referenced by third-party frameworks. Our experience is that third-party developers will
///  pin to a specific version of SageResearch, which breaks the dependency model that we use
///  internally in our applications.
///
///  The work-around to this is to include a light-weight model here since this framework is fairly
///  static and in most cases where the `RSDResult` is referenced, those classes already import
///  JsonModel. This will allow us to divorce *our* code from SageResearch so that we can iterate
///  independently of third-party frameworks.
///
public protocol ResultData : PolymorphicTyped, Encodable, DictionaryRepresentable {
    
    /// The identifier associated with the task, step, or asynchronous action.
    var identifier: String { get }
    
    /// The start date timestamp for the result.
    var startDate: Date { get set }
    
    /// The end date timestamp for the result.
    var endDate: Date { get set }
    
    /// The `deepCopy()` method is intended to allow copying a result to retain the previous result
    /// when revisiting an action. Since a class with get/set variables will use a pointer to the instance
    /// this allows results to either be structs *or* classes and allows collections of results to use
    /// mapping to deep copy their children.
    func deepCopy() -> Self
}

/// Implementation of the interface used by Sage for cross-platform support where the serialized
/// ``endDate`` may be nil.
public protocol MultiplatformResultData : ResultData {
    var startDateTime: Date { get set }
    var endDateTime: Date? { get set }
}
 
extension MultiplatformResultData {
    
    public var startDate: Date {
        get { self.startDateTime }
        set { self.startDateTime = newValue }
    }
    
    public var endDate: Date {
        get { self.endDateTime ?? self.startDateTime }
        set { self.endDateTime = newValue }
    }
}

extension ResultData {
    public func jsonDictionary() throws -> [String : JsonSerializable] {
        try jsonEncodedDictionary()
    }
}

/// `ResultObject` is a concrete implementation of the base result associated with a task, step, or asynchronous action.
public struct ResultObject : SerializableResultData, MultiplatformResultData, Codable {
    private enum CodingKeys : String, OrderedEnumCodingKey {
        case serializableType = "type", identifier, startDateTime = "startDate", endDateTime = "endDate"
    }
    public private(set) var serializableType: SerializableResultType = .StandardTypes.base.resultType

    public let identifier: String
    public var startDateTime: Date
    public var endDateTime: Date?
    
    /// Default initializer for this object.
    ///
    /// - parameters:
    ///     - identifier: The identifier string.
    public init(identifier: String, startDate: Date = Date(), endDate: Date? = nil) {
        self.identifier = identifier
        self.startDateTime = startDate
        self.endDateTime = endDate
    }
    
    public func deepCopy() -> ResultObject {
        self
    }
}

extension ResultObject : DocumentableStruct {
    public static func codingKeys() -> [CodingKey] {
        CodingKeys.allCases
    }
    
    public static func isRequired(_ codingKey: CodingKey) -> Bool {
        (codingKey as? CodingKeys).map { $0 == .endDateTime } ?? true
    }
    
    public static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not recognized for this class")
        }
        switch key {
        case .serializableType:
            return .init(constValue: SerializableResultType.StandardTypes.base.resultType)
        case .identifier:
            return .init(propertyType: .primitive(.string))
        case .startDateTime, .endDateTime:
            return .init(propertyType: .format(.dateTime))
        }
    }
    
    public static func examples() -> [ResultObject] {
        var result = ResultObject(identifier: "step1")
        result.startDateTime = ISO8601TimestampFormatter.date(from: "2017-10-16T22:28:09.000-07:00")!
        result.endDateTime = result.startDateTime.addingTimeInterval(5 * 60)
        return [result]
    }
}

