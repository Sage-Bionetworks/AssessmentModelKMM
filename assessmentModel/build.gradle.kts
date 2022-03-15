import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("maven-publish")
    id("org.jetbrains.dokka")
}


android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(21)
        multiDexEnabled = true //Required when setting minSdkVersion to 20 or lower
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
        targetCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
    }
}
dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")
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
       publishAllLibraryVariants()
    }

    val iOSTargetName  = System.getenv("SDK_NAME") ?: project.findProperty("XCODE_SDK_NAME") as? String ?: "iphonesimulator"
    val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
            if (iOSTargetName.startsWith("iphoneos"))
                ::iosArm64
            else if (iOSTargetName.startsWith("macos"))
                ::macosX64
            else
                ::iosX64
    iOSTarget("ios") {
        binaries {
            framework {
                baseName = "AssessmentModel"
                // Include DSYM in the release build
                freeCompilerArgs += "-Xg0"
                // Include Generics in the module header.
                freeCompilerArgs += "-Xobjc-generics"
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
             api ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
             api("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")
            }
        }
        commonTest {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-common")
                implementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
            }
        }
        sourceSets["androidLibMain"].dependencies {
            implementation("androidx.appcompat:appcompat:1.3.0")
        }
        sourceSets["iosMain"].dependencies {
        }

    }

    val packForXcode by tasks.creating(Sync::class) {
        group = "build"
        val mode = System.getenv("CONFIGURATION") ?: project.findProperty("XCODE_CONFIGURATION") as? String ?: "DEBUG"
        val sdkName = System.getenv("SDK_NAME") ?: project.findProperty("XCODE_SDK_NAME") as? String ?: "iphonesimulator"
        val targetName = "ios"// + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
        val framework = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
        inputs.property("mode", mode)
        dependsOn(framework.linkTask)
        val targetDir = File(buildDir, "xcode-frameworks")
        from({ framework.outputDirectory })
        into(targetDir)
    }
    tasks.getByName("build").dependsOn(packForXcode)
}

publishing {
    repositories {
        maven {
            url = uri("https://sagebionetworks.jfrog.io/artifactory/mobile-sdks/")
            credentials {
                username = System.getenv("artifactoryUser")
                password = System.getenv("artifactoryPwd")
            }
        }
    }
}

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
