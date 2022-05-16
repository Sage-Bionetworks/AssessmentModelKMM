//
//  AssessmentNavigationTests.swift
//
//
//  Copyright © 2022 Sage Bionetworks. All rights reserved.
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


import XCTest
import JsonModel
@testable import AssessmentModel
@testable import AssessmentModelUI

class AssessmentNavigationTests: XCTestCase {
    
    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }
    
    func testNavigation_ForwardTo5X() {
        var steps: [Node] = []
        let beforeSteps: [Node] = TestStep.steps(from: ["introduction", "step1", "step2", "step3"])
        steps.append(contentsOf: beforeSteps)
        steps.append(SectionObject(identifier: "step4", children: TestStep.steps(from: ["stepA", "stepB", "stepC"])))
        steps.append(SectionObject(identifier: "step5", children: TestStep.steps(from: ["stepX", "stepY", "stepZ"])))
        steps.append(SectionObject(identifier: "step6", children: TestStep.steps(from: ["stepA", "stepB", "stepC"])))
        let afterSteps: [Node] = TestStep.steps(from: ["step7", "completion"])
        steps.append(contentsOf: afterSteps)
        
        let taskController = TestAssessmentController(steps)

        // Go to step under test
        let loopCount = taskController.test_stepTo("stepX")
        guard loopCount <= steps.count else {
            XCTFail("Possible loop of wacky madness. loopCount=\(loopCount)")
            return
        }

        // Check expected state
        
        XCTAssertTrue(taskController.viewModel.navigationViewModel.backEnabled)
        
        let topHistory = taskController.assessmentState.assessmentResult.stepHistory
        let topIds = topHistory.map { $0.identifier }
        XCTAssertEqual(topIds, ["introduction", "step1", "step2", "step3", "step4", "step5"])
        
        if let step4Result = topHistory.first(where: {$0.identifier == "step4"}) as? BranchNodeResult {
            let step4Ids = step4Result.stepHistory.map({ $0.identifier })
            XCTAssertEqual(step4Ids, ["stepA", "stepB", "stepC"])
            step4Result.stepHistory.forEach {
                guard let result = $0 as? MultiplatformTimestamp else {
                    XCTFail("Result not of expected type. \($0)")
                    return
                }
                XCTAssertNotNil(result.endDateTime)
                XCTAssertGreaterThan(result.endDate, result.startDate)
            }
        }
        else {
            XCTFail("Missing step4")
        }
        
        topHistory.forEach {
            guard let result = $0 as? MultiplatformTimestamp else {
                XCTFail("Result not of expected type. \($0)")
                return
            }
            if $0.identifier != "step5" {
                XCTAssertNotNil(result.endDateTime)
                XCTAssertGreaterThan(result.endDate, result.startDate)
            }
            else {
                // current node should not have an end timestamp yet
                XCTAssertNil(result.endDateTime)
            }
        }
        
        // Check restored state
        guard let expectedNode = taskController.assessmentState.currentStep?.node
        else {
            XCTFail("Expected the current node to be non-nil")
            return
        }
        let restoredController = TestAssessmentController(steps, restoredResult: taskController.assessmentState.assessmentResult)
        let restoredState = restoredController.assessmentState.currentStep
        XCTAssertEqual(expectedNode.identifier, restoredState?.node.identifier)
        XCTAssertEqual(expectedNode.typeName, restoredState?.node.typeName)
        XCTAssertEqual(.forward, restoredController.viewModel.navigationViewModel.currentDirection)
    }
    
    func testNavigation_BackFrom5X() {
        var steps: [Node] = []
        let beforeSteps: [Node] = TestStep.steps(from: ["introduction", "step1", "step2", "step3"])
        steps.append(contentsOf: beforeSteps)
        steps.append(SectionObject(identifier: "step4", children: TestStep.steps(from: ["stepA", "stepB", "stepC"])))
        steps.append(SectionObject(identifier: "step5", children: TestStep.steps(from: ["stepX", "stepY", "stepZ"])))
        steps.append(SectionObject(identifier: "step6", children: TestStep.steps(from: ["stepA", "stepB", "stepC"])))
        let afterSteps: [Node] = TestStep.steps(from: ["step7", "completion"])
        steps.append(contentsOf: afterSteps)
        
        let taskController = TestAssessmentController(steps)

        // Go to step under test
        let loopCount = taskController.test_stepTo("stepX")
        guard loopCount <= steps.count else {
            XCTFail("Possible loop of wacky madness. loopCount=\(loopCount)")
            return
        }
        
        // Go back from that step
        taskController.viewModel.goBack()
        
        let stepTo = taskController.assessmentState.currentStep

        XCTAssertNotNil(stepTo)
        XCTAssertEqual(stepTo?.node.identifier, "stepC")
        
        XCTAssertEqual(.backward, taskController.viewModel.navigationViewModel.currentDirection)
        XCTAssertTrue(taskController.viewModel.navigationViewModel.backEnabled)
        
        // Check restored state
        guard let expectedNode = taskController.assessmentState.currentStep?.node
        else {
            XCTFail("Expected the current node to be non-nil")
            return
        }
        let restoredController = TestAssessmentController(steps, restoredResult: taskController.assessmentState.assessmentResult)
        let restoredState = restoredController.assessmentState.currentStep
        XCTAssertEqual(expectedNode.identifier, restoredState?.node.identifier)
        XCTAssertEqual(expectedNode.typeName, restoredState?.node.typeName)
        XCTAssertEqual(.forward, restoredController.viewModel.navigationViewModel.currentDirection)
    }

    func testNavigation_BackFrom5Z() {
        var steps: [Node] = []
        let beforeSteps: [Node] = TestStep.steps(from: ["introduction", "step1", "step2", "step3"])
        steps.append(contentsOf: beforeSteps)
        steps.append(SectionObject(identifier: "step4", children: TestStep.steps(from: ["stepA", "stepB", "stepC"])))
        steps.append(SectionObject(identifier: "step5", children: TestStep.steps(from: ["stepX", "stepY", "stepZ"])))
        steps.append(SectionObject(identifier: "step6", children: TestStep.steps(from: ["stepA", "stepB", "stepC"])))
        let afterSteps: [Node] = TestStep.steps(from: ["step7", "completion"])
        steps.append(contentsOf: afterSteps)

        let taskController = TestAssessmentController(steps)

        // Go to step under test
        let loopCount = taskController.test_stepTo("stepZ")
        guard loopCount <= steps.count else {
            XCTFail("Possible loop of wacky madness. loopCount=\(loopCount)")
            return
        }
        
        // Go back from step
        taskController.viewModel.goBack()
        
        let stepTo = taskController.assessmentState.currentStep

        XCTAssertNotNil(stepTo)
        XCTAssertEqual(stepTo?.node.identifier, "stepY")
        
        XCTAssertEqual(.backward, taskController.viewModel.navigationViewModel.currentDirection)
        XCTAssertTrue(taskController.viewModel.navigationViewModel.backEnabled)
        
        // Check restored state
        guard let expectedNode = taskController.assessmentState.currentStep?.node
        else {
            XCTFail("Expected the current node to be non-nil")
            return
        }
        let restoredController = TestAssessmentController(steps, restoredResult: taskController.assessmentState.assessmentResult)
        let restoredState = restoredController.assessmentState.currentStep
        XCTAssertEqual(expectedNode.identifier, restoredState?.node.identifier)
        XCTAssertEqual(expectedNode.typeName, restoredState?.node.typeName)
        XCTAssertEqual(.forward, restoredController.viewModel.navigationViewModel.currentDirection)
    }
}

class TestStep : AbstractInstructionStepObject {
    public override class func defaultType() -> SerializableNodeType {
        "testStep"
    }
    
    static func steps(from identifiers: [String]) -> [TestStep] {
        identifiers.map { .init(identifier: $0) }
    }
}

class TestAssessmentStepViewVender : AssessmentStepViewVender {
    override func isSupported(step: Step) -> Bool {
        true
    }
}

class TestAssessmentController {
    let assessment: AssessmentObject
    let assessmentState: AssessmentState
    let viewModel: AssessmentViewModel
    
    init(_ children: [Node], restoredResult: AssessmentResult? = nil) {
        let assessment = AssessmentObject(identifier: "test", children: children)
        let assessmentState = AssessmentState(assessment, restoredResult: restoredResult)
        let viewModel = AssessmentViewModel()
        viewModel.initialize(assessmentState, viewVender: TestAssessmentStepViewVender())
        
        self.assessment = assessment
        self.assessmentState = assessmentState
        self.viewModel = viewModel
    }
    
    func test_stepTo(_ identifier: String, maxCount: Int = 100) -> Int {
        var loopCount = 0
        while loopCount <= maxCount, assessmentState.currentStep?.node.identifier != identifier {
            viewModel.goForward()
            loopCount += 1
        }
        return loopCount
    }
}

