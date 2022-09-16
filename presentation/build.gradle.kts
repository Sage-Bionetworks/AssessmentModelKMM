plugins {
    id("com.android.library")
    kotlin("android")
    id("maven-publish")
    id("org.jetbrains.dokka")
    id("kotlin-android")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        //useIR = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures {
        compose = true
    }
    buildFeatures.viewBinding = true
    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7")
    api(project(":assessmentModel"))

    //Workaround for compose previews not working
    // https://stackoverflow.com/questions/71812710/can-no-longer-view-jetpack-compose-previews-failed-to-instantiate-one-or-more-c
    debugImplementation("androidx.customview:customview:1.2.0-alpha01")
    debugImplementation("androidx.customview:customview-poolingcontainer:1.0.0")

    implementation(libs.koin.android)
    implementation(libs.androidx.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.viewmodelKtx)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.uiToolingPreview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.google.accompanist)

    coreLibraryDesugaring(libs.android.desugar)

    testImplementation(libs.junit)
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation(libs.androidx.compose.uiTest)
    debugImplementation(libs.androidx.compose.uiTooling)
}

afterEvaluate {

    tasks.register<Jar>("sourcesJar") {
        from(android.sourceSets["main"].java.srcDirs)
        classifier = "sources"
    }

    publishing {
        publications {
            create<MavenPublication>("presentation") {
                from(components.getByName("release"))
                artifact(tasks.getByName("releaseSourcesJar"))
                artifact(tasks.getByName<Jar>("javadocJar"))
            }
        }
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