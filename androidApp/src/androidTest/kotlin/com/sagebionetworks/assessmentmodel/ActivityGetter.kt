package com.sagebionetworks.assessmentmodel

import android.app.Activity
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage

/**
 * This object helps with retrieving the current activity for the UI tests so that
 * we can test that the results were generated from the automated UI tests
 * https://stackoverflow.com/questions/24517291/get-current-activity-in-espresso-android
*/

object ActivityGetter {
    fun getActivityInstance(): Activity? {
        var activity: Activity? = null
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val resumedActivities: Collection<*> =
                ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
            if (resumedActivities.iterator().hasNext()) {
                activity = resumedActivities.iterator().next() as Activity?
            }
        }
        return activity
    }
}