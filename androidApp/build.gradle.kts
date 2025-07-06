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
        versionCode = 1
        versionName = "1.0"
        targetSdk = 35
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
    val voyagerVersion = "1.1.0-beta02"
    implementation(project(":shared"))


    implementation(libs.bundles.material3)


    implementation(libs.bundles.accompanist)

    implementation("cafe.adriel.voyager:voyager-navigator:${voyagerVersion}")
// Screen Model
    implementation("cafe.adriel.voyager:voyager-screenmodel:${voyagerVersion}")
// BottomSheetNavigator
    implementation("cafe.adriel.voyager:voyager-bottom-sheet-navigator:${voyagerVersion}")
// TabNavigator
    implementation("cafe.adriel.voyager:voyager-tab-navigator:${voyagerVersion}")
// Transitions
    implementation("cafe.adriel.voyager:voyager-transitions:${voyagerVersion}")
// Koin integration
    implementation("cafe.adriel.voyager:voyager-koin:${voyagerVersion}")
    implementation(libs.compose.ui.util)
    implementation(libs.compose.activity)
    implementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui)
    implementation(libs.icons.lucide)

    implementation("io.coil-kt.coil3:coil-compose:3.2.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.2.0")

    implementation(libs.koin.android)
}