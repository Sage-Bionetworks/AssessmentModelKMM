buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.4.10")
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
    version = "0.4.1"

    repositories {
        google()
        mavenCentral()
    }
}

subprojects {
    afterEvaluate {
        if (project.plugins.hasPlugin("com.android.library")) {
//            val android = this.extensions.getByName("android") as com.android.build.gradle.LibraryExtension
//            val kotlin =
//                this.extensions.getByType(org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension::class.java)

            tasks.register<Jar>("javadocJar") {
                val dokkaJavadoc = tasks.getByName<org.jetbrains.dokka.gradle.DokkaTask>("dokkaJavadoc")
                dependsOn(dokkaJavadoc)
                classifier = "javadoc"
                from(dokkaJavadoc.outputDirectory)
            }
        }
    }
}