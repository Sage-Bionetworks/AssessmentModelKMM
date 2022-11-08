//
//  PermissionsOnboarding.swift
//
//

import Foundation
import AssessmentModel

func previewPermissionsBranchViewModel(_ initialIndex: Int) -> BranchViewModel {
    let nodeState = PreviewBranchNodeState(previewPermissionsAssessment, initialIndex: initialIndex)
    return BranchViewModel(nodeState)
}

let previewPermissionsAssessment = AssessmentObject(identifier: "Permissions Onboarding",
                                                    children: previewPermissionsNodes)

let previewPermissionsNodes: [ContentNodeStep] = [
    PermissionStepObject(identifier: "notifications",
                         permissionType: PermissionType.StandardNotifications(),
                         title: "Notifications",
                         detail: """
                            We will send you a daily reminder or notification on your phone to complete your activities.\n\n
                            You can choose to make it an alert, a sound, or an icon badge.\n\n
                            You can say no and still be in the study.
                            """,
                         imageInfo: FetchableImage("permissions_notifications")),
    InstructionStepObject(identifier: "intro",
                          title: "Environmental Factors",
                          detail: """
                            The environment around you, such as weather and air pollution in your area or how close you are
                            to a grocery store or park, can affect your health and well-being.\n\n
                            On the next screens we will ask for your permission to collect a variety of data. This is
                            optional, you can say no and still be in the study.
                            """,
                          imageInfo: FetchableImage("permissions_intro")),
    PermissionStepObject(identifier: "weather",
                         permissionType: PermissionType.StandardLocationWhenInUse(),
                         title: "Weather and Air Quality",
                         detail: """
                            We'd like to know the weather and air quality around you.\n\n
                            We will only collect this data when you are using the app.
                            """,
                         imageInfo: FetchableImage("permissions_weather")),
    PermissionStepObject(identifier: "microphone",
                         permissionType: PermissionType.StandardMicrophone(),
                         title: "Microphone",
                         detail: """
                            Noise can be distracting. We'd like to use the phone microphone to record the noise level
                            around you.\n\n
                            We only measure noise when you are doing the activities. We do not keep the recordings.
                            """,
                         imageInfo: FetchableImage("permissions_weather")),
    PermissionStepObject(identifier: "motion",
                         permissionType: PermissionType.StandardMotion(),
                         title: "Motion & Fitness Activity",
                         detail: """
                            We’d like to measure your movements while you use the app.\n\n
                            This will give us an idea of your physical activity that may distract you while you
                            are using the app.
                            """,
                         imageInfo: FetchableImage("permissions_motion")),
]

