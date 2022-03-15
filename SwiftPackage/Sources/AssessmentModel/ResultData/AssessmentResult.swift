//
//  AssessmentResult.swift
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

/// An ``AssessmentResult`` is the top-level ``ResultData`` for an assessment.
public protocol AssessmentResult : BranchNodeResult {

    /// A unique identifier for this run of the assessment. This property is defined as readwrite
    /// to allow the controller for the task to set this on the ``AssessmentResult`` children
    /// included in this run.
    var taskRunUUID: UUID { get set }

    /// The ``versionString`` may be a semantic version, timestamp, or sequential revision integer.
    var versionString: String? { get }
    
    /// An identifier for the assessment model associated with this result. If included, this
    /// is intended to match the identifier used by the services that requested running the
    /// assessment. This could be a schedule identifier or an identifier in a different namespace
    /// than the "task identifier" used by the assessment developers to identify their assessments.
    var assessmentIdentifier: String? { get }
    
    /// An identifier that can be used either by the assessment developers or scientists as needed.
    var schemaIdentifier: String?  { get }
}

/// Abstract implementation to allow extending an assessment result while retaining the serializable type.
open class AbstractAssessmentResultObject : Codable {
    private enum CodingKeys : String, OrderedEnumCodingKey {
        case serializableType = "type", identifier, schemaIdentifier, startDate, endDate, assessmentIdentifier, versionString, taskRunUUID, stepHistory, asyncResults, path
    }
    public private(set) var serializableType: SerializableResultType = "null"
    
    open class func defaultType() -> SerializableResultType {
        fatalError("Default serializable type not defined for abstract.")
    }

    /// The identifier associated with the task, step, or asynchronous action.
    public let identifier: String
    public var startDateTime: Date
    public var endDateTime: Date?
    
    public let versionString: String?
    public var assessmentIdentifier: String?
    public var schemaIdentifier: String?

    public var taskRunUUID: UUID
    public var stepHistory: [ResultData]
    public var asyncResults: [ResultData]?
    public var path: [PathMarker]

    /// Default initializer for this object.
    public init(identifier: String,
                versionString: String? = nil,
                assessmentIdentifier: String? = nil,
                schemaIdentifier: String? = nil,
                startDate: Date = Date(),
                endDate: Date? = nil,
                taskRunUUID: UUID = UUID(),
                stepHistory: [ResultData] = [],
                asyncResults: [ResultData]? = nil,
                path: [PathMarker] = []) {
        self.identifier = identifier
        self.versionString = versionString
        self.assessmentIdentifier = assessmentIdentifier
        self.schemaIdentifier = schemaIdentifier
        self.startDateTime = startDate
        self.endDateTime = endDate
        self.taskRunUUID = taskRunUUID
        self.stepHistory = stepHistory
        self.asyncResults = asyncResults
        self.path = path
        self.serializableType = type(of: self).defaultType()
    }

    /// Initialize from a `Decoder`. This decoding method will use the ``SerializationFactory`` instance associated
    /// with the decoder to decode the `stepHistory` and `asyncResults`.
    ///
    /// - parameter decoder: The decoder to use to decode this instance.
    /// - throws: `DecodingError`
    public required init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.identifier = try container.decode(String.self, forKey: .identifier)
        self.startDateTime = try container.decode(Date.self, forKey: .startDate)
        self.endDateTime = try container.decodeIfPresent(Date.self, forKey: .endDate)
        self.taskRunUUID = try container.decode(UUID.self, forKey: .taskRunUUID)
        self.path = try container.decodeIfPresent([PathMarker].self, forKey: .path) ?? []
        self.versionString = try container.decodeIfPresent(String.self, forKey: .versionString)
        self.assessmentIdentifier = try container.decodeIfPresent(String.self, forKey: .assessmentIdentifier)
        self.schemaIdentifier = try container.decodeIfPresent(String.self, forKey: .schemaIdentifier)

        let resultsContainer = try container.nestedUnkeyedContainer(forKey: .stepHistory)
        self.stepHistory = try decoder.serializationFactory.decodePolymorphicArray(ResultData.self, from: resultsContainer)

        if container.contains(.asyncResults) {
            let asyncResultsContainer = try container.nestedUnkeyedContainer(forKey: .asyncResults)
            self.asyncResults = try decoder.serializationFactory.decodePolymorphicArray(ResultData.self, from: asyncResultsContainer)
        }
        
        self.serializableType = type(of: self).defaultType()
    }

    /// Encode the result to the given encoder.
    /// - parameter encoder: The encoder to use to encode this instance.
    /// - throws: `EncodingError`
    open func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(identifier, forKey: .identifier)
        try container.encode(serializableType, forKey: .serializableType)
        try container.encode(startDateTime, forKey: .startDate)
        try container.encodeIfPresent(endDateTime, forKey: .endDate)
        try container.encode(taskRunUUID, forKey: .taskRunUUID)
        try container.encode(path, forKey: .path)
        try container.encodeIfPresent(self.assessmentIdentifier, forKey: .assessmentIdentifier)
        try container.encodeIfPresent(self.schemaIdentifier, forKey: .schemaIdentifier)
        try container.encodeIfPresent(self.versionString, forKey: .versionString)

        var nestedContainer = container.nestedUnkeyedContainer(forKey: .stepHistory)
        for result in stepHistory {
            let nestedEncoder = nestedContainer.superEncoder()
            try result.encode(to: nestedEncoder)
        }

        if let results = asyncResults {
            var asyncContainer = container.nestedUnkeyedContainer(forKey: .asyncResults)
            for result in results {
                let nestedEncoder = asyncContainer.superEncoder()
                try result.encode(to: nestedEncoder)
            }
        }
    }
    
    open class func codingKeys() -> [CodingKey] {
        CodingKeys.allCases
    }

    open class func isRequired(_ codingKey: CodingKey) -> Bool {
        guard let key = codingKey as? CodingKeys else { return false }
        switch key {
        case .serializableType, .identifier, .startDate, .taskRunUUID, .stepHistory:
            return true
        default:
            return false
        }
    }

    open class func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        guard let key = codingKey as? CodingKeys else {
            throw DocumentableError.invalidCodingKey(codingKey, "\(codingKey) is not recognized for this class")
        }
        switch key {
        case .serializableType:
            return .init(constValue: SerializableResultType.StandardTypes.assessment.resultType)
        case .identifier:
            return .init(propertyType: .primitive(.string))
        case .startDate, .endDate:
            return .init(propertyType: .format(.dateTime))
        case .assessmentIdentifier:
            return .init(propertyType: .primitive(.string), propertyDescription:
            """
            An identifier for the assessment model associated with this result. If included, this
            is intended to match the identifier used by the services that requested running the
            assessment. This could be a schedule identifier or an identifier in a different namespace
            than the "task identifier" used by the assessment developers to identify their assessments.
            """.replacingOccurrences(of: "\n", with: " ").replacingOccurrences(of: "  ", with: "\n"))
        case .schemaIdentifier:
            return .init(propertyType: .primitive(.string), propertyDescription:
            "An identifier that can be used either by the developer or researcher for custom mapping.")
        case .versionString:
            return .init(propertyType: .primitive(.string), propertyDescription:
            "The versioning key used by the developer to version this assessment.")
        case .taskRunUUID:
            return .init(propertyType: .primitive(.string), propertyDescription:
            """
            A unique identifier for this run of the assessment. This property is defined as readwrite
            to allow the controller for the task to set this on the ``AssessmentResult`` children
            included in this run.
            """.replacingOccurrences(of: "\n", with: " ").replacingOccurrences(of: "  ", with: "\n"))
        case .stepHistory:
            return .init(propertyType: .interfaceArray("\(ResultData.self)"), propertyDescription:
            """
            The ``stepHistory`` includes the history of the node results that were traversed as a
            part of running an assessment.
            """.replacingOccurrences(of: "\n", with: " ").replacingOccurrences(of: "  ", with: "\n"))
        case .asyncResults:
            return .init(propertyType: .interfaceArray("\(ResultData.self)"), propertyDescription:
            "A list of all the asynchronous results for this task. The list should include uniquely identified results.")
        case .path:
            return .init(propertyType: .referenceArray(PathMarker.documentableType()), propertyDescription:
                            "The path traversed by this branch.")
        }
    }
}

/// ``AssessmentResultObject`` is a result associated with a task. This object includes a step history,
/// task run UUID,  and asynchronous results.
public final class AssessmentResultObject : AbstractAssessmentResultObject, SerializableResultData, AssessmentResult, MultiplatformResultData {
    
    public override class func defaultType() -> SerializableResultType {
        .StandardTypes.assessment.resultType
    }
    
    public func deepCopy() -> AssessmentResultObject {
        let copy = AssessmentResultObject(identifier: self.identifier,
                                          versionString: self.versionString,
                                          assessmentIdentifier: self.assessmentIdentifier,
                                          schemaIdentifier: self.schemaIdentifier)
        copy.startDateTime = self.startDateTime
        copy.endDateTime = self.endDateTime
        copy.taskRunUUID = self.taskRunUUID
        copy.stepHistory = self.stepHistory.map { $0.deepCopy() }
        copy.asyncResults = self.asyncResults?.map { $0.deepCopy() }
        copy.path = self.path
        return copy
    }
}

extension AssessmentResultObject : DocumentableRootObject {

    public convenience init() {
        self.init(identifier: "example")
    }

    public var jsonSchema: URL {
        URL(string: "\(self.className).json", relativeTo: kSageJsonSchemaBaseURL)!
    }

    public var documentDescription: String? {
        "A top-level result for this assessment."
    }
}

extension AssessmentResultObject : DocumentableStruct {

    public static func examples() -> [AssessmentResultObject] {

        let result = AssessmentResultObject(identifier: "example")

        var introStepResult = ResultObject(identifier: "introduction")
        introStepResult.startDateTime = ISO8601TimestampFormatter.date(from: "2017-10-16T22:28:09.000-07:00")!
        introStepResult.endDateTime = introStepResult.startDateTime.addingTimeInterval(20)
        
        let collectionResult = CollectionResultObject.examples().first!
        collectionResult.startDateTime = introStepResult.endDateTime!
        collectionResult.endDateTime = collectionResult.startDateTime.addingTimeInterval(2 * 60)
        
        var conclusionStepResult = ResultObject(identifier: "conclusion")
        conclusionStepResult.startDateTime = collectionResult.endDateTime!
        conclusionStepResult.endDateTime = conclusionStepResult.startDateTime.addingTimeInterval(20)

        var fileResult = FileResultObject.examples().first!
        fileResult.startDateTime = collectionResult.startDateTime
        fileResult.endDateTime = collectionResult.endDateTime
        
        result.stepHistory = [introStepResult, collectionResult, conclusionStepResult]
        result.asyncResults = [fileResult]

        result.startDateTime = introStepResult.startDateTime
        result.endDateTime = conclusionStepResult.endDateTime
        result.path = [
            .init(identifier: "introduction", direction: .forward),
            .init(identifier: collectionResult.identifier, direction: .forward),
            .init(identifier: "conclusion", direction: .forward)
        ]

        return [result]
    }
}

