buildscript {
    val compose_version by extra("1.2.0-rc02")
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.2.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.7.0")
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
    version = "0.7.4"

    repositories {
        google()
        mavenCentral()
        jcenter() // still used by org.jetbrains.dokka:javadoc-plugin - liujoshua 05-07-2021
        maven(url = "https://androidx.dev/storage/compose-compiler/repository/")
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
