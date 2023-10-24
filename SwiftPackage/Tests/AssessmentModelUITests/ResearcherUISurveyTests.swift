// Created 10/24/23
// swift-tools-version:5.0

import XCTest
@testable import AssessmentModel
import JsonModel
import ResultModel

final class ResearcherUISurveyTests: XCTestCase {
    
    let decoder = AssessmentFactory().createJSONDecoder()
    let encoder = AssessmentFactory().createJSONEncoder()

    override func setUpWithError() throws {
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }

    override func tearDownWithError() throws {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }

    func testDHP1127() throws {
        let wrapper = try decoder.decode(NodeWrapper<AssessmentObject>.self, from: survey_mvzvbb_Json)
        let assessment = wrapper.node

        // First check that the decoding worked as expected.
        let expected: Set<ButtonType> = [.navigation(.skip), .navigation(.goBackward)]
        XCTAssertEqual(expected, assessment.shouldHideButtons)
        
        // Next check the view model
        let taskController = TestAssessmentController(assessment)
        let loopCount = taskController.test_stepTo("singleChoiceQ_psrbtf")
        guard loopCount <= assessment.children.count else {
            XCTFail("Possible loop of wacky madness. loopCount=\(loopCount)")
            return
        }
        
        guard let current = taskController.assessmentState.currentStep else {
            XCTFail("Unexpected null `currentStep`")
            return
        }
        XCTAssertNotNil(current)
        
        // Confirm assumption that the step identifier is the expected
        XCTAssertEqual("singleChoiceQ_psrbtf", current.step.identifier)
        
        let canGoBack = taskController.viewModel.canGoBack(step: current.step)
        XCTAssertFalse(canGoBack, "step should not be able to go back")
        
        let skipButtonText = taskController.viewModel.skipButtonText(step: current.step)
        XCTAssertNil(skipButtonText, "skip button should be hidden")
        
        let shouldHideSkip = assessment.shouldHideButton(.navigation(.skip), node: current.step)
        let shouldHideBack = assessment.shouldHideButton(.navigation(.goBackward), node: current.step)
        XCTAssertTrue(shouldHideSkip ?? false, "Skip button should be hidden")
        XCTAssertTrue(shouldHideBack ?? false, "Back button should be hidden")
    }

}

struct NodeWrapper<Value : Node> : Decodable {
    let node : Value
    init(from decoder: Decoder) throws {
        let step = try decoder.serializationFactory.decodePolymorphicObject(Node.self, from: decoder)
        guard let qStep = step as? Value else {
            let context = DecodingError.Context(codingPath: decoder.codingPath, debugDescription: "Failed to decode a QuestionStep")
            throw DecodingError.typeMismatch(Value.self, context)
        }
        self.node = qStep
    }
}

// DHP-1127 skip and back button are not hidden
fileprivate let survey_mvzvbb_Json = """
{
  "type": "assessment",
  "identifier": "mvzvbb",
  "shouldHideActions": [
    "skip",
    "goBackward"
  ],
  "interruptionHandling": {
    "canResume": true,
    "reviewIdentifier": "beginning",
    "canSkip": false,
    "canSaveForLater": false
  },
  "steps": [
    {
      "type": "overview",
      "identifier": "overview",
      "title": "Status",
      "detail": "",
      "image": {
        "imageName": "screening",
        "type": "sageResource"
      }
    },
    {
      "type": "choiceQuestion",
      "identifier": "singleChoiceQ_psrbtf",
      "title": "Were you interupted? ",
      "baseType": "string",
      "singleChoice": true,
      "choices": [
        {
          "value": "Yes",
          "text": "Choice A"
        },
        {
          "value": "No",
          "text": "Choice B"
        }
      ]
    },
    {
      "type": "completion",
      "identifier": "completion",
      "title": "Well done!",
      "detail": "Thank you for being part of our study."
    }
  ],
  "webConfig": {
    "skipOption": "NO_SKIP"
  }
}
""".data(using: .utf8)!

