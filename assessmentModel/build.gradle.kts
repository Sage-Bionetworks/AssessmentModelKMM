import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("maven-publish")
    id("org.jetbrains.dokka")
}


android {
    compileSdkVersion(31)
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
    buildFeatures.viewBinding = true
    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0-dev-k1.7.0-53370d83bb1"
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

    val xcf = XCFramework("KotlinModel")
    ios {
        binaries.framework {
            baseName = "KotlinModel"
            xcf.add(this)
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2-native-mt")
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
            implementation("androidx.compose.runtime:runtime:${rootProject.extra["compose_version"]}")
        }
        sourceSets["iosMain"].dependencies {
        }

    }
//
//    val packForXcode by tasks.creating(Sync::class) {
//        group = "build"
//        val mode = System.getenv("CONFIGURATION") ?: project.findProperty("XCODE_CONFIGURATION") as? String ?: "DEBUG"
//        val sdkName = System.getenv("SDK_NAME") ?: project.findProperty("XCODE_SDK_NAME") as? String ?: "iphonesimulator"
//        val targetName = "ios"// + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
//        val framework = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
//        inputs.property("mode", mode)
//        dependsOn(framework.linkTask)
//        val targetDir = File(buildDir, "xcode-frameworks")
//        from({ framework.outputDirectory })
//        into(targetDir)
//    }
//    tasks.getByName("build").dependsOn(packForXcode)
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

