//
//  AsyncActionConfiguration.swift
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

/// `AsyncActionConfiguration` defines general configuration for an asynchronous background action
/// that should be run off of the main thread. Depending upon the parameters and how the action is set
/// up, this could be something that is run continuously or else is paused or reset based on a
/// timeout interval.
///
/// The configuration is intended to be a serializable object and does not directly call services, record
/// data, or anything else.
///
/// - seealso: `AsyncActionController`.
///
public protocol AsyncActionConfiguration : PolymorphicTyped {
    
    /// A short string that uniquely identifies the asynchronous action within the task.
    var identifier : String { get }
    
    /// An identifier marking the step at which to start the action. If `nil`, then the action will
    /// be started when the branch is started.
    var startStepIdentifier: String? { get }
    
    /// List of the permissions required for this action.
    var permissionTypes: [PermissionType] { get }
}

public final class AsyncActionConfigurationSerializer : IdentifiableInterfaceSerializer, PolymorphicSerializer {
    public var documentDescription: String? {
        """
        `AsyncActionConfiguration` defines general configuration for an asynchronous action
        that should be run in the background. Depending upon the parameters and how the action is set
        up, this could be something that is run continuously or else is paused or reset based on a
        timeout interval.
        """.replacingOccurrences(of: "\n", with: " ").replacingOccurrences(of: "  ", with: "\n")
    }
    
    public var jsonSchema: URL {
        URL(string: "\(self.interfaceName).json", relativeTo: kSageJsonSchemaBaseURL)!
    }

    override init() {
        self.examples = [
            ExampleAsyncActionConfiguration()
        ]
    }

    public private(set) var examples: [AsyncActionConfiguration]

    public func add<T: AsyncActionConfiguration>(_ example: T) where T : Decodable {
        if examples.first is ExampleAsyncActionConfiguration {
            examples.remove(at: 0)
        }
        examples.removeAll(where: { $0.typeName == example.typeName })
        examples.append(example)
    }
}

// Internal example used to keep unit tests from blowing up because no documentable async actions are in this library.
struct ExampleAsyncActionConfiguration : AsyncActionConfiguration, Codable, DocumentableStruct {
    private enum CodingKeys : String, OrderedEnumCodingKey {
        case typeName = "type"
    }
    let typeName: String = "example"

    var identifier: String { typeName }
    var startStepIdentifier: String? { nil }
    var permissionTypes: [PermissionType] { [.init(typeName) ]}
    
    static func codingKeys() -> [CodingKey] {
        CodingKeys.allCases
    }
    
    static func isRequired(_ codingKey: CodingKey) -> Bool {
        true
    }
    
    static func documentProperty(for codingKey: CodingKey) throws -> DocumentProperty {
        .init(defaultValue: .string("example"), propertyDescription: "")
    }
    
    static func examples() -> [ExampleAsyncActionConfiguration] {
        [.init()]
    }
}
