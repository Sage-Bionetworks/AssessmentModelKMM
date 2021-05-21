//
//  PermissionsOnboarding.swift
//
//
//  Copyright © 2021 Sage Bionetworks. All rights reserved.
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

