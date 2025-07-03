plugins {
    id("com.android.application")
    kotlin("android")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.kashif.kmmnewsapp.android"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "com.kashif.kmmnewsapp.android"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":shared"))
    
    // Material3
    implementation(libs.bundles.material3)
    
    // Accompanist
    implementation(libs.bundles.accompanist)
    
    // Compose
    implementation(libs.compose.ui.util)
    implementation(libs.compose.activity)
    implementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui)

    implementation(libs.koin.android)
}