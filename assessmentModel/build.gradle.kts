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
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
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
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}
dependencies {
    testImplementation(project(mapOf("path" to ":assessmentResults")))
    coreLibraryDesugaring(libs.android.desugar)

    testImplementation(libs.junit)
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    androidTestImplementation(libs.junit)
    androidTestImplementation("org.jetbrains.kotlin:kotlin-test")
    androidTestImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

val iosFrameworkName = "KotlinModel"

kotlin {
    android("androidLib") {
       publishAllLibraryVariants()
    }

    val xcframework = XCFramework(iosFrameworkName)
    // ios() includes x86 sim & arm64 device
    ios {
        binaries.framework {
            baseName = iosFrameworkName
            embedBitcode(org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode.DISABLE)
            xcframework.add(this)
        }
    }
    // iosSimulatorArm64() adds Apple Silicon simulator support
    iosSimulatorArm64 {
        binaries.framework {
            baseName = iosFrameworkName
            embedBitcode(org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode.DISABLE)
            xcframework.add(this)
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(project(":assessmentResults"))
                api(libs.kotlinx.serialization)
                api(libs.kotlinx.dateTime)
                implementation(libs.coroutines.core)
            }
        }
        commonTest {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-common")
                implementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
            }
        }
        sourceSets["androidLibMain"].dependencies {
            implementation(libs.androidx.appcompat)
            implementation(platform("androidx.compose:compose-bom:${libs.versions.androidxComposeBom.get()}"))
            implementation(libs.androidx.compose.runtime)
        }

        // Set up dependencies between the source sets for Mac Silicon
        val iosMain by getting
        val iosSimulatorArm64Main by sourceSets.getting
        iosSimulatorArm64Main.dependsOn(iosMain)
    }
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

