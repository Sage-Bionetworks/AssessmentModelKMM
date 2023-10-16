plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android")
    id("org.jetbrains.dokka")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "org.sagebionetworks.assessmentmodel.sampleapp"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    packagingOptions {
        exclude("META-INF/main.kotlin_module")
        pickFirst("META-INF/kotlinx-serialization-runtime.kotlin_module")
    }
    buildFeatures.viewBinding = true

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
        getByName("test").java.srcDirs("src/main/kotlin")
    }

    compileOptions {
        // Flag to enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = true
        // Sets Java compatibility to Java 8
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    testOptions.unitTests.isIncludeAndroidResources = true
    namespace = "org.sagebionetworks.assessmentmodel.sampleapp"
}


dependencies {
    implementation(project(":presentation"))

    implementation(libs.androidx.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.viewmodelKtx)
    //implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation(libs.koin.android)

    coreLibraryDesugaring(libs.android.desugar)

    testImplementation(libs.junit)
}
