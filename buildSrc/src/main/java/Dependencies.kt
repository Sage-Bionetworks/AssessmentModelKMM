object Libs {

    const val androidGradlePlugin = "com.android.tools.build:gradle:7.0.0-alpha09"

    //TODO: migrate to maven-publish-plugin https://developer.android.com/studio/build/maven-publish-plugin
    const val androidMavenGradlePlugin = "com.github.dcendents:android-maven-gradle-plugin:2.1"

    object Kotlin {
        private const val version = "1.5.0-M1"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
        const val serialization ="org.jetbrains.kotlin:kotlin-serialization:${version}"
    }

    object Dokka {
        const val version = "1.4.0"
    }

    object Jetpack {
        val compose = "1.0.0-beta01"
        val composeLifecycle = "1.0.0-alpha02"
    }
}