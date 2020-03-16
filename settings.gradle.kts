pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.android.library") {
                useModule("com.android.tools.build:gradle:3.5.2")
            }
            if (requested.id.id == "com.android.application") {
                useModule("com.android.tools.build:gradle:3.5.2")
            }
            if (requested.id.id == "org.jetbrains.kotlin.multiplatform") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.70")
            }
            if (requested.id.id == "org.jetbrains.kotlin.plugin.serialization") {
                useModule("org.jetbrains.kotlin:kotlin-serialization:1.3.70")
            }
        }
    }

    repositories {
        gradlePluginPortal()
        google()
        jcenter()
    }
}

include(":assessmentModel")
include(":androidApp")
