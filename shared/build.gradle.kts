plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    kotlin("plugin.serialization") version "2.2.0"
    kotlin("plugin.parcelize")
    id("com.rickclephas.kmp.nativecoroutines") version "1.0.0-ALPHA-45"
}

version = "1.0"

kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    kotlin.targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java) {
        // export correct artifact to use all classes of library directly from Swift
        binaries.withType(org.jetbrains.kotlin.gradle.plugin.mpp.Framework::class.java).all {
            export(libs.moko.mvvm.core)
        }
    }
    
    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.bundles.ktor.common)
                implementation(libs.koin.core)
                implementation(libs.bundles.kotlinx)
                api(libs.moko.mvvm.core)
                implementation(libs.kotlinx.coroutines.core)
                api("com.rickclephas.kmp:kmp-observableviewmodel-core:1.0.0-BETA-12")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.client.android)
                implementation(libs.koin.android)
            }
        }
        val iosMain by creating {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
    }
}

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
    languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
}

nativeCoroutines {
    // Optional: customize plugin behavior
    // exposedSeverity = ExposedSeverity.ERROR
    // suffix = "Native"
}

android {
    namespace = "com.kashif.kmmnewsapp"
    compileSdk = libs.versions.compileSdk.get().toInt()
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
}