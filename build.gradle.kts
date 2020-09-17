buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.0")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.4.0")
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1")
    }
}

plugins {

    id("org.jetbrains.dokka") version "1.4.0"
}

tasks.dokkaHtmlMultiModule {
    outputDirectory.set(rootDir.resolve("docs"))
}

allprojects {
    group = "org.sagebionetworks.assessmentmodel"
    version = "0.3.2"

    repositories {
        jcenter()
        google()
    }
}
