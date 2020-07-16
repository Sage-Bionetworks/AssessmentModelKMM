import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id( "com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.github.dcendents.android-maven")
    id ("maven-publish")
}

android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(19)
        multiDexEnabled = true //Required when setting minSdkVersion to 20 or lower
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        coreLibraryDesugaringEnabled = true
        sourceCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
        targetCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.0.5")
    // Specify Kotlin/JVM stdlib dependency.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7")

    testImplementation("junit:junit:4.12")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    androidTestImplementation("junit:junit:4.12")
    androidTestImplementation("org.jetbrains.kotlin:kotlin-test")
    androidTestImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

kotlin {
   android("androidLib") {
       publishLibraryVariants("release", "debug")
   }
   
   val buildForDevice = project.findProperty("device") as? Boolean ?: false
   val iosTarget = if(buildForDevice) iosArm64("ios") else iosX64("ios")
   iosTarget.binaries {
      framework {
          baseName = "AssessmentModel"
         // Disable bitcode embedding for the simulator build.
         if (!buildForDevice) {
            embedBitcode("disable")
         }
          // Include DSYM in the release build
          freeCompilerArgs += "-Xg0"
          // Include Generics in the module header.
          freeCompilerArgs += "-Xobjc-generics"
      }
   }

   sourceSets {
      commonMain {
         dependencies {
            implementation( "org.jetbrains.kotlin:kotlin-stdlib-common")
             api ("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:0.20.0")
         }
      }
      commonTest {
         dependencies {
            implementation( "org.jetbrains.kotlin:kotlin-test-common")
             implementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
         }
      }
       sourceSets["androidLibMain"].dependencies {
           implementation("org.jetbrains.kotlin:kotlin-stdlib")
           api( "org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")
           implementation("androidx.appcompat:appcompat:1.1.0")
       }
       sourceSets["iosMain"].dependencies {
           api( "org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.20.0")
       }

    }
}

tasks.register("copyFramework") {
    val buildType = project.findProperty("kotlin.build.type") as? String ?: "DEBUG"
    dependsOn("link${buildType.toLowerCase().capitalize()}FrameworkIos")

    doLast {
        val srcFile = (kotlin.targets["ios"] as KotlinNativeTarget).binaries.getFramework(buildType).outputFile
        val targetDir = project.property("configuration.build.dir")!!
        copy {
            from(srcFile.parent)
            into(targetDir)
            include( "AssessmentModel.framework/**")
            include("AssessmentModel.framework.dSYM")
        }
    }
}

apply("../config/artifact-deploy.gradle")

//TODO: syoung 03/24/2020 Figure out why getting a warning that this was already added.
//tasks.register("iosTest")  {
//    val  device = project.findProperty("iosDevice") as? String ?: "iPhone 8"
//    dependsOn("linkDebugTestIos")
//    group = JavaBasePlugin.VERIFICATION_GROUP
//    description = "Runs tests for target 'ios' on an iOS simulator"
//
//    doLast {
//        val  binary = (kotlin.targets["ios"] as KotlinNativeTarget).binaries.getTest("DEBUG").outputFile
//        exec {
//            commandLine("xcrun", "simctl", "spawn", "--standalone", device, binary.absolutePath)
//        }
//    }
//}
