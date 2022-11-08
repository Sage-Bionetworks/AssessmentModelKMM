//
//  KMMTestAssessment.swift
//  
//

import XCTest
import Foundation
import AssessmentModel
import JsonModel
import ResultModel

class ResultSerializationTestCase: XCTestCase {
    
    func checkAssessmentResult(from kmmJson: Data) throws {
        let factory = AssessmentFactory()
        let decoder = factory.createJSONDecoder()
        let decodedResult = try decoder.decode(AssessmentResultObject.self, from: kmmJson)
        checkResults(swiftResult: swiftAssessmentResult, decodedResult: decodedResult)
    }
    
    func checkResults(swiftResult: ResultData, decodedResult: ResultData) {
        XCTAssertEqual(swiftResult.typeName, decodedResult.typeName, "\(swiftResult.identifier)")
        XCTAssertEqual(swiftResult.identifier, decodedResult.identifier, "\(swiftResult.identifier)")
        XCTAssertEqual(swiftResult.startDate.timeIntervalSinceReferenceDate, decodedResult.startDate.timeIntervalSinceReferenceDate, accuracy: 0.1, "\(swiftResult.identifier)")
        XCTAssertEqual(swiftResult.endDate.timeIntervalSinceReferenceDate, decodedResult.endDate.timeIntervalSinceReferenceDate, accuracy: 0.1, "\(swiftResult.identifier)")
        
        if let answerResult = swiftResult as? AnswerResultObject {
            if let decoded = decodedResult as? AnswerResultObject {
                XCTAssertEqual(answerResult.questionText, decoded.questionText, "\(swiftResult.identifier)")
                XCTAssertEqual(answerResult.questionData, decoded.questionData, "\(swiftResult.identifier)")
                XCTAssertEqual(answerResult.jsonValue, decoded.jsonValue, "\(swiftResult.identifier)")
                XCTAssertEqual(answerResult.jsonAnswerType?.typeName, decoded.jsonAnswerType?.typeName, "\(swiftResult.identifier)")
            }
            else {
                XCTFail("Failed to decode the correct type. expected=\(swiftResult), actual=\(decodedResult)")
            }
        }
        else if let branchResult = swiftResult as? BranchNodeResult {
            if let decoded = decodedResult as? BranchNodeResult {
                branchResult.stepHistory.forEach { childResult in
                    if let decodedChild = decoded.stepHistory.first(where: { $0.identifier == childResult.identifier }) {
                        checkResults(swiftResult: childResult, decodedResult: decodedChild)
                    }
                    else {
                        XCTFail("Failed to decode the matching child. expected=\(childResult)")
                    }
                }
            }
            else {
                XCTFail("Failed to decode the correct type. expected=\(swiftResult), actual=\(decodedResult)")
            }
        }
    }
}

let swiftFetchableImage = FetchableImage(imageName: "instructionFoo",
                                    label: "labelFoo",
                                    placementHint: "topBackground",
                                    imageSize: .init(width: 100, height: 100))
let swiftAnimatedImage = AnimatedImage(imageNames: ["image1","image2"], animationDuration: 2.0)

let swiftGoButton = ButtonActionInfoObject(buttonTitle: "Go, Dogs! Go")
let swiftBackButton = ButtonActionInfoObject(iconName: "back")

let swiftInstructionStep = InstructionStepObject(identifier: "instruction",
                                            title: "title",
                                            subtitle: "subtitle",
                                            detail: "detail",
                                            imageInfo: swiftFetchableImage,
                                            shouldHideButtons: [.navigation(.goBackward)],
                                            buttonMap: [
                                                .navigation(.goForward) : swiftGoButton,
                                                .navigation(.goBackward) : swiftBackButton],
                                            comment: "comment",
                                            nextNode: .node("baroo"),
                                            fullInstructionsOnly: true,
                                            spokenInstructions: [.start : "Start instructions"])

let swiftCompletionStep = CompletionStepObject(identifier: "completion",
                                          title: "title",
                                          subtitle: "subtitle",
                                          detail: "detail",
                                          imageInfo: swiftAnimatedImage,
                                          shouldHideButtons: [.navigation(.pause)],
                                          buttonMap: [.navigation(.goForward) : swiftGoButton],
                                          comment: "comment",
                                          nextNode: .reserved(.exit))

let swiftOverviewStep = OverviewStepObject(identifier: "overview",
                                           title: "title",
                                           subtitle: "subtitle",
                                           detail: "detail",
                                           imageInfo: swiftFetchableImage,
                                           permissions: [.init(permission: .weather, optional: false, restrictedMessage: "restricted", deniedMessage: "denied")],
                                           shouldHideButtons: [.navigation(.pause)],
                                           buttonMap: [.navigation(.goForward) : swiftGoButton],
                                           comment: "comment",
                                           nextNode: .reserved(.nextSection))

let swiftPermissionStep = PermissionStepObject(identifier: "permission",
                                               permissionType: .weather,
                                               optional: false,
                                               restrictedMessage: "Resticted",
                                               deniedMessage: "You are denied.")

let regex = try! NSRegularExpression(pattern: "/[a-z]+/g", options: [])
let swiftStringTextInputItem = StringTextInputItemObject(fieldLabel: "Question 1",
                                                         placeholder: "enter string",
                                                         resultIdentifier: "foo",
                                                         keyboardOptions: .init(keyboardType: .asciiCapable),
                                                         regExValidator: .init(pattern: regex, invalidMessage: "Failed regex"))

let swiftStringQuestion = SimpleQuestionStepObject(identifier: "stringQuestion",
                                                   inputItem: swiftStringTextInputItem)

let swiftIntOptions: IntegerFormatOptions = {
    var options = IntegerFormatOptions()
    options.minimumValue = 0
    options.minimumValue = 10000
    options.usesGroupingSeparator = false
    options.stepInterval = 10
    return options
}()
let swiftIntTextInputItem = IntegerTextInputItemObject(fieldLabel: "Question 2",
                                                  placeholder: "Enter integer",
                                                  resultIdentifier: "foo",
                                                  formatOptions: swiftIntOptions)
let swiftIntQuestion = SimpleQuestionStepObject(identifier: "intQuestion", inputItem: swiftIntTextInputItem)

let swiftDoubleOptions: DoubleFormatOptions = {
    var options = DoubleFormatOptions()
    options.minimumValue = -1.0
    options.maximumValue = 1.0
    options.maximumFractionDigits = 4
    return options
}()
let swiftDoubleTextInputItem = DoubleTextInputItemObject(fieldLabel: "Question 3",
                                                         placeholder: "Enter number between -1 and 1",
                                                         resultIdentifier: "foo",
                                                         formatOptions: swiftDoubleOptions)
let swiftDoubleQuestion = SimpleQuestionStepObject(identifier: "doubleQuestion", inputItem: swiftDoubleTextInputItem)

let swiftYearOptions: YearFormatOptions = {
    var options = YearFormatOptions()
    options.allowFuture = false
    options.minimumYear = 2000
    return options
}()
let swiftYearTextInputItem = YearTextInputItemObject(fieldLabel: "Question 4",
                                                     placeholder: "enter a year",
                                                     resultIdentifier: "foo",
                                                     formatOptions: swiftYearOptions)
let swiftYearQuestion = SimpleQuestionStepObject(identifier: "yearQuestion",
                                                 inputItem: swiftYearTextInputItem,
                                                 surveyRules: [.init(skipToIdentifier: "completion", matchingValue: .integer(2009), ruleOperator: .lessThan)])

let swiftChoiceQuestion = ChoiceQuestionStepObject(identifier: "Question 5",
                                              choices: [
                                                .init(value: .integer(0), text: "zero", detail: "detail", selectorType: .default),
                                                .init(value: .integer(1), text: "one", detail: "detail", selectorType: .default),
                                                .init(value: .integer(2), text: "two", detail: "detail", selectorType: .all),
                                                .init(value: .integer(3), text: "three", detail: "detail", selectorType: .exclusive),
                                              ],
                                              baseType: .integer,
                                              singleChoice: false,
                                              other: IntegerTextInputItemObject(),
                                              title: "title",
                                              subtitle: "subtitle",
                                              detail: "detail",
                                              imageInfo: swiftFetchableImage,
                                              optional: false,
                                              uiHint: .init(rawValue: "checkbox"),
                                              comment: "comment",
                                              nextNode: .node("question2"))

let swiftDurationQuestion = SimpleQuestionStepObject(
    identifier: "Question 6",
    inputItem: DurationTextInputItemObject(displayUnits: [.minute, .second])
)

let swiftTimeQuestion = SimpleQuestionStepObject(
    identifier: "Question 7",
    inputItem: TimeTextInputItemObject(formatOptions: .init(from: .init(hour: 6, minute: 30)!, to: .init(hour: 2, minute: 15)!))
)

let swiftQuestionSection = SectionObject(identifier: "questionSection",
                                         children: [
                                            swiftChoiceQuestion,
                                            swiftDoubleQuestion,
                                            swiftIntQuestion,
                                            swiftStringQuestion,
                                            swiftYearQuestion,
                                            swiftDurationQuestion,
                                            swiftTimeQuestion,
                                         ])

let swiftAssessment = AssessmentObject(identifier: "foo",
                                       children: [swiftOverviewStep, swiftInstructionStep, swiftPermissionStep, swiftQuestionSection, swiftCompletionStep],
                                       version: "0.0.1",
                                       estimatedMinutes: 2,
                                       interruptionHandling: .init(reviewIdentifier: .node(swiftInstructionStep.identifier),
                                                                   canResume: false,
                                                                   canSaveForLater: false,
                                                                   canSkip: false)
)

let swiftAssessmentResult: AssessmentResultObject = {
    let assessmentResult = swiftAssessment.instantiateAssessmentResult() as! AssessmentResultObject
    swiftAssessment.children.forEach { node in
        var result = node.instantiateResult()
        result.endDate = result.startDate.addingTimeInterval(30)
        assessmentResult.stepHistory.append(result)
        if let section = node as? SectionObject,
           let branchResult = result as? BranchNodeResultObject {
            section.children.forEach { child in
                let qResult = child.instantiateResult()
                branchResult.stepHistory.append(qResult)
                if let question = child as? QuestionStep,
                   let answerResult = qResult as? AnswerResultObject {
                    answerResult.endDateTime = answerResult.startDate.addingTimeInterval(30)
                    let jsonValue: JsonElement? = {
                        switch question.answerType.baseType {
                        case .integer:
                            return .integer(2)
                        case .boolean:
                            return .boolean(true)
                        case .number:
                            return .number(2.3)
                        case .string:
                            return .string("baroo")
                        default:
                            return nil
                        }
                    }()
                    if question.answerType is AnswerTypeArray {
                        answerResult.jsonValue = jsonValue.map { .array([$0.jsonObject()]) }
                    }
                    else {
                        answerResult.jsonValue = jsonValue
                    }
                }
                branchResult.endDateTime = qResult.endDate
            }
        }
    }
    return assessmentResult
}()

