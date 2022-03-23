

package org.sagebionetworks.assessmentmodel

/**
 * [AsyncActionConfiguration] defines general configuration for an asynchronous action that should be run in
 * the background. Depending upon the parameters and how the action is set up, this could be something that is run
 * continuously or else is paused or reset based on a timeout interval.
 */
interface AsyncActionConfiguration : ResultMapElement {

    /**
     * An identifier marking the step to start the action. If `null`, then the action will be started when the
     * [AsyncActionContainer] is started.
     */
    val startStepIdentifier: String?
}

// TODO: syoung 03/22/2022 delete or move to MobilePassiveData-SDK
//
///**
// * [RecorderConfiguration] is used to configure a recorder--for example, when recording accelerometer data or video.
// */
//interface RecorderConfiguration : AsyncActionConfiguration {
//
//    /**
//     * A localized string (or localization key) describing the reason for using this recorder.
//     */
//    val reason: String?
//
//    /**
//     * Is this recorder required to run the assessment or is it optional? Some active tasks can run and will return
//     * valuable results without the augmented results from a sensor. For other assessments, the recorder is required
//     * in order for the measurement of the assessment to be meaningful.
//     */
//    val optional: Boolean
//
//    /**
//     * Most recorders will require one or more permissions.
//     */
//    val permissions: List<PermissionInfo>?
//
//    /**
//     * An identifier marking the step at which to stop the action. If `null`, then the action will be stopped when the
//     * task is stopped.
//     */
//    val stopStepIdentifier: String?
//
//    /**
//     * Whether or not the recorder must be allowed to run when the app is in the background. Default = `false`.
//     */
//    val requiresBackground: Boolean
//        get() = false
//
//    /**
//     * Should the file used in a previous run of a recording be deleted? If false, a unique file name will be used to
//     * identify the file when a step is repeated? Default = `true`.
//     */
//    val shouldDeletePrevious: Boolean
//        get() = true
//}
//
//interface TableRecorderConfiguration : RecorderConfiguration {
//
//    /**
//     * Set the flag to `true` to encode the samples as a CSV file. Default = `false`.
//     */
//    val usesCSVEncoding : Boolean
//        get() = false
//}
//
///**
// * The [SampleRecord] defines the properties that are included with all logging samples. This allows for some measure
// * of comparing timing of recordings across different sensors.
// */
//interface SampleRecord {
//
//    /**
//     * An identifier marking the current step.
//     *
//     * This is a path marker where the path components are separated by a '/' character. This path includes
//     * the assessment identifier and any sections for the full path to the current step.
//     */
//    val stepPath: String?
//
//    /**
//     * The date timestamp when the measurement was taken (if available). This should be included for the
//     * first entry to mark the start of the recording. Other than to mark step changes, the [timestampDateString]
//     * is optional and should only be included if required by the research study.
//     */
//    val timestampDateString: String?
//
//    /**
//     * A timestamp that is relative to the system uptime.
//     *
//     * This should be included for the first entry to mark the start of the recording. Other than to mark
//     * step changes, the `timestamp` is optional and should only be included if required by the research
//     * study.
//     *
//     * On Apple devices, this is the timestamp used to mark sensors that run in the foreground *only* such as
//     * video processing and motion sensors.
//     *
//     * syoung 04/24/2019 Per request from Sage Bionetworks' research scientists, this timestamp is "zeroed"
//     * to when the recorder is started. It should be calculated by offsetting the
//     * `ProcessInfo.processInfo.systemUptime` from the monotonic clock time to account for gaps in the
//     * sampling due to the application becoming inactive, for example if the participant accepts a phone
//     * call while the recorder is running.
//     */
//    val timestamp: Double?
//}

// TODO: syoung 11/15/2017 This configuration type is stubbed out for future work. This is not currently implemented in
//  the node state handler or node controller.
///**
// * [RequestConfiguration] is used to start an asynchronous service such as a url request or fetching from core data.
// */
//interface RequestConfiguration : AsyncActionConfiguration {
//
//    /**
//     * An identifier marking a step to wait to display until the action is completed. This is only valid for actions
//     * that are single result actions and not continuous recorders.
//     */
//    val waitStepIdentifier: String?
//
//    /**
//     * A time interval in seconds after which the action should be reset. For example, if the action queries a weather service
//     * and the user backgrounds the app for more than the reset time, then the weather service should be queried again.
//     * If `0`, then the action will never reset.
//     */
//    val resetTimeInterval: Double
//
//    /**
//     * A time interval in seconds after which the action should be cancelled. Default = 60
//     */
//    val timeoutTimeInterval: Double
//        get() = 60.0
//}
