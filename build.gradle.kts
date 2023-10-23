buildscript {
    repositories {
        google()
        mavenCentral()
        maven {url = uri("https://plugins.gradle.org/m2/")}
    }
    dependencies {
        classpath(libs.bundles.gradlePlugins)
    }
}

plugins {
    id("org.jetbrains.dokka") version "1.9.0"
    id("maven-publish")
}

tasks.dokkaHtmlMultiModule {
    outputDirectory.set(rootDir.resolve("docs"))
}

allprojects {
    group = "org.sagebionetworks.assessmentmodel"

    version = "1.1.1"

    repositories {
        google()
        mavenCentral()
        jcenter() // still used by org.jetbrains.dokka:javadoc-plugin - liujoshua 05-07-2021
   }
}

// TODO: This block was causing problems with build, need to figure out creating javadoc jar if we want it -nbrown 10/10/2023
//subprojects {
//    afterEvaluate {
//        if (project.plugins.hasPlugin("com.android.library")) {
////            val android = this.extensions.getByName("android") as com.android.build.gradle.LibraryExtension
////            val kotlin =
////                this.extensions.getByType(org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension::class.java)
//
//            tasks.register<Jar>("javadocJar") {
//                val dokkaJavadoc = tasks.getByName<org.jetbrains.dokka.gradle.DokkaTask>("dokkaJavadoc")
//                dependsOn(dokkaJavadoc)
//                //classifier = "javadoc"
//                from(dokkaJavadoc.outputDirectory)
//            }
//        }
//    }
//}
