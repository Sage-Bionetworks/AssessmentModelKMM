buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.70")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.3.70")
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1")
    }
}

allprojects {
    group = "org.sagebionetworks.assessmentmodel"
    version = "0.1.16"

    repositories {
        jcenter()
        google()
    }
}
