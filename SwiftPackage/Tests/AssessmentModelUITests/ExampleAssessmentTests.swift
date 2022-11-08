//
//  ExampleAssessmentTests.swift
//  
//

import Foundation
@testable import AssessmentModelUI
import AssessmentModel
import JsonModel
import XCTest

class ExampleAssessmentTests: XCTestCase {
    
    let decoder = AssessmentFactory().createJSONDecoder()
    let encoder = AssessmentFactory().createJSONEncoder()

    override func setUp() {
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }

    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }
    
    func testSurveyAEncoding() {
        do {
            encoder.outputFormatting.formUnion([.withoutEscapingSlashes])
            let json = try encoder.encode(surveyA)
            let _ = String(data: json, encoding: .utf8)!
            //print(jsonString) // syoung 03/20/2022 Intentionally commented out but left in for building example JSON
        } catch {
            XCTFail("Failed to encode/decode object. \(error)")
        }
    }
    
    func testSurveyBEncoding() {
        do {
            encoder.outputFormatting.formUnion([.withoutEscapingSlashes])
            let json = try encoder.encode(surveyB)
            let _ = String(data: json, encoding: .utf8)!
            //print(jsonString) // syoung 03/20/2022 Intentionally commented out but left in for building example JSON
        } catch {
            XCTFail("Failed to encode/decode object. \(error)")
        }
    }
}
