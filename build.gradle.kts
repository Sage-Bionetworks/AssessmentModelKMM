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
    id("org.jetbrains.dokka") version "1.6.21"
    id("maven-publish")
}

tasks.dokkaHtmlMultiModule {
    outputDirectory.set(rootDir.resolve("docs"))
}

allprojects {
    group = "org.sagebionetworks.assessmentmodel"

    version = "0.12.0"

    repositories {
        google()
        mavenCentral()
        jcenter() // still used by org.jetbrains.dokka:javadoc-plugin - liujoshua 05-07-2021
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
