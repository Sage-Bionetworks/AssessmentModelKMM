plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android")
    id("org.jetbrains.dokka")
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "org.sagebionetworks.assessmentmodel.sampleapp"
        minSdk = 21
        targetSdk = 31
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
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation(project(":presentation"))

    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("io.insert-koin:koin-android:3.1.1")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    testImplementation("junit:junit:4.12")
}
