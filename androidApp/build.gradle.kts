plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp") version "1.7.10-1.0.6"

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

    applicationVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
}

dependencies {
    implementation(project(":shared"))

    with(ComposeDestination){

        implementation(composeDestination)
        ksp(composeDestinationPlugin)
    }
    with(Material3){
        implementation(material3)
        implementation(window)
    }
    with(Accompanist){
        implementation(coil)
        implementation(webview)
    }
    with(Compose){
        implementation(util){

        }
        implementation(composeActivity) {
            because("We are not using  xml its better to use compose activity ")
        }


        implementation(composeToolingDebug){
            because("Supports preview of composables")
        }

        debugImplementation(composeToolingDebug) {

            because("Supports previews and other tooling stuff." )
        }
        implementation(composeUI) {
            because("Supports compose ")
        }
    }

    implementation(Koin.koinAndroid)
}