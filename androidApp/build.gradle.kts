plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 33
    defaultConfig {
        applicationId = "com.kashif.kmmnewsapp.android"
        minSdk = 23
        targetSdk = 33
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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
    }
}

dependencies {
    implementation(project(":shared"))
    with(Compose){
        implementation(util){

        }
        implementation(composeActivity) {
            because("We are not using  xml its better to use compose activity ")
        }

        implementation(composeMaterial) {
            because("Supports material design components")
        }

        implementation(composeToolingDebug){

        }

        debugImplementation(composeToolingDebug) {

            because("Supports previews and other tooling stuff." )
        }
        implementation(commposeUI) {
            because("Supports compose ")
        }
    }

    implementation(Koin.koinAndroid)
}