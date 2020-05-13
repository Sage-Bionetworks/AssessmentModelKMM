buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.6.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.70")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.3.70")
        // Required plugins added to classpath to facilitate pushing to Jcenter/Bintray
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1")
    }
}

allprojects {
    group = "org.sagebionetworks.assessmentmodel"
    version = "0.1"

    repositories {
        jcenter()
        google()
    }
}
