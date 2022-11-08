//
//  KMMSerializationTests.swift
//
//

import XCTest
@testable import AssessmentModel
import JsonModel
import KotlinModel

class KMMSerializationTests: ResultSerializationTestCase {
    
    func testSurveyACodable() {
        let factory = AssessmentFactory()
        let encoder = factory.createJSONEncoder()

        do {
            
            let json = try encoder.encode(surveyA)
            let jsonString = String(data: json, encoding: .utf8)!
            let loader = AssessmentJsonStringLoader(jsonString: jsonString, bundle: Bundle.main)
            let kmmAssessment = try loader.decodeObject() as? KotlinModel.AssessmentObject
            XCTAssertNotNil(kmmAssessment)
            
        } catch {
            XCTFail("Failed to encode or decode the assessment: \(error)")
        }
    }
        
    func testAssessmentCodable() {
        let factory = AssessmentFactory()
        let encoder = factory.createJSONEncoder()

        do {
            
            let json = try encoder.encode(swiftAssessment)
            let jsonString = String(data: json, encoding: .utf8)!
            let loader = AssessmentJsonStringLoader(jsonString: jsonString, bundle: Bundle.main)
            let kmmAssessment = try loader.decodeObject() as? KotlinModel.AssessmentObject
            XCTAssertNotNil(kmmAssessment)
            
            guard let kmmAssessment = kmmAssessment else {
                XCTFail("Failed to decode assessment from the kotlin model.")
                return
            }
            
            XCTAssertEqual(swiftAssessment.identifier, kmmAssessment.identifier)
            XCTAssertEqual(swiftAssessment.comment, kmmAssessment.comment)
            XCTAssertEqual(swiftAssessment.estimatedMinutes, Int(kmmAssessment.estimatedMinutes))
            XCTAssertEqual(swiftAssessment.versionString, kmmAssessment.versionString)
            
            XCTAssertEqual(swiftAssessment.children.count, kmmAssessment.children.count)
            
            if let overview = kmmAssessment.children.first as? KotlinModel.OverviewStepObject {
                checkObjects(swiftNode: swiftOverviewStep, kmmNode: overview)
            } else {
                XCTFail("Failed to decode the overview step as the first step")
            }
            
            if let instruction = kmmAssessment.children[1] as? KotlinModel.InstructionStepObject {
                checkObjects(swiftNode: swiftInstructionStep, kmmNode: instruction)
            } else {
                XCTFail("Failed to decode the instruction step as the second step")
            }
            
            if let permission = kmmAssessment.children[2] as? KotlinModel.PermissionStepObject {
                checkObjects(swiftNode: swiftPermissionStep, kmmNode: permission)
            } else {
                XCTFail("Failed to decode the instruction step as the second step")
            }
            
            if let section = kmmAssessment.children[3] as? KotlinModel.SectionObject {
                checkObjects(swiftNode: swiftQuestionSection, kmmNode: section)
                swiftQuestionSection.children.enumerated().forEach { node in
                    guard let swiftNode = node.element as? AssessmentModel.AbstractQuestionStepObject,
                          section.children.count > node.offset,
                          let kmmNode = section.children[node.offset] as? KotlinModel.QuestionObject
                    else {
                        XCTFail("The kotlin node or swift node does not match expected. at:\(node.offset), for:\(node.element)")
                        return
                    }
                    checkObjects(swiftNode: swiftNode, kmmNode: kmmNode)
                    // TODO: check that the questions match. syoung 03/21/2022
                }
            } else {
                XCTFail("Failed to decode the section as the third node")
            }
            
            if let completion = kmmAssessment.children.last as? KotlinModel.CompletionStepObject {
                checkObjects(swiftNode: swiftCompletionStep, kmmNode: completion)
            } else {
                XCTFail("Failed to decode the completion step as the last step")
            }
            
        } catch {
            XCTFail("Failed to encode or decode the assessment: \(error)")
        }
    }
    
    func checkObjects(swiftNode: AssessmentModel.AbstractContentNodeObject, kmmNode: KotlinModel.NodeObject) {
        XCTAssertEqual(swiftNode.identifier, kmmNode.identifier)
        XCTAssertEqual(swiftNode.title, kmmNode.title)
        XCTAssertEqual(swiftNode.subtitle, kmmNode.subtitle)
        XCTAssertEqual(swiftNode.detail, kmmNode.detail)
        XCTAssertEqual(swiftNode.comment, kmmNode.comment)
        XCTAssertEqual(swiftNode.nextNode?.stringValue, kmmNode.nextNodeIdentifier)
        XCTAssertEqual(swiftNode.shouldHideButtons.map { $0.stringValue }, kmmNode.hideButtons.map { $0.name })
        XCTAssertEqual(swiftNode.buttonMap.count, kmmNode.buttonMap.count)
        kmmNode.buttonMap.forEach { (key, value) in
            guard let swiftBtn = swiftNode.buttonMap[.init(rawValue: key.name)] else {
                XCTFail("\(key.name) not found in the swift button map.")
                return
            }
            XCTAssertEqual(value.buttonTitle, swiftBtn.buttonTitle)
            XCTAssertEqual(value.imageInfo?.imageName, swiftBtn.iconName)
        }
    }
    
    func testResultCodable() {
        let factory = AssessmentFactory()
        let encoder = factory.createJSONEncoder()

        do {
            let json = try encoder.encode(swiftAssessmentResult)
            let jsonString = String(data: json, encoding: .utf8)!
            print(jsonString)
            
            let loader = KotlinModel.ResultDecoder(jsonString: jsonString)
            let kmmAssessmentResult = try loader.decodeObject() as? KotlinModel.AssessmentResultObject
            XCTAssertNotNil(kmmAssessmentResult)
            
            guard let kmmAssessmentResult = kmmAssessmentResult else {
                XCTFail("Failed to decode assessment result from the kotlin model.")
                return
            }
            
            let kmmEncoder = KotlinModel.ResultEncoder(result: kmmAssessmentResult)
            let kmmJsonString = try kmmEncoder.encodeObject()
            let kmmJson = kmmJsonString.data(using: .utf8)!
            try checkAssessmentResult(from: kmmJson)

        } catch {
            XCTFail("Failed to encode or decode the assessment: \(error)")
        }
    }
    
}

