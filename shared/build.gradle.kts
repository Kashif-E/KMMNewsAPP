import co.touchlab.skie.configuration.DefaultArgumentInterop
import co.touchlab.skie.configuration.EnumInterop
import co.touchlab.skie.configuration.FlowInterop
import co.touchlab.skie.configuration.FunctionInterop
import co.touchlab.skie.configuration.SealedInterop
import co.touchlab.skie.configuration.SuspendInterop

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    kotlin("plugin.serialization") version "2.2.0"
    kotlin("plugin.parcelize")
   // id("com.rickclephas.kmp.nativecoroutines") version "1.0.0-ALPHA-45"
    id("co.touchlab.skie") version "0.10.4"
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
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
                implementation(libs.kotlinx.coroutines.core)
                api("com.rickclephas.kmp:kmp-observableviewmodel-core:1.0.0-BETA-12")
                
                // SKIE configuration annotations for fine-grained control
                compileOnly("co.touchlab.skie:configuration-annotations:0.10.4")
                implementation(libs.androidx.room.runtime)
                implementation(libs.sqlite.bundled)
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

//nativeCoroutines {
//    this.k2Mode = true
//    this.
//}



android {
    namespace = "com.kashif.kmmnewsapp"
    compileSdk = libs.versions.compileSdk.get().toInt()
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
}
skie {
    features {
        group {
            SuspendInterop.Enabled(true) // Enhanced Swift async/await for suspend functions
            FlowInterop.Enabled(true) // Swift AsyncSequence support for Flow types
            FunctionInterop.LegacyName(false) // Use Swift-idiomatic function naming
            FunctionInterop.FileScopeConversion.Enabled(true) // Expose global functions to Swift
            DefaultArgumentInterop.Enabled(true) // Swift overloads for functions with default args
            DefaultArgumentInterop.MaximumDefaultArgumentCount(3) // Limit Swift overloads for default args
            EnumInterop.Enabled(true) // Improved enum interop with Swift
            EnumInterop.LegacyCaseName(false) // Use modern Swift case naming for enums
            SealedInterop.Enabled(true) // Enable sealed class interop for Swift
            SealedInterop.ExportEntireHierarchy(true) // Export full sealed class hierarchy
        }

        // Enable preview features for cutting-edge Swift integration
        enableSwiftUIObservingPreview = true
        enableFutureCombineExtensionPreview = true
    }
    
    build {
        // Enable distributable framework for better iOS integration
        produceDistributableFramework()
        
        // Enable Swift library evolution for better binary compatibility
        enableSwiftLibraryEvolution.set(true)
    }
    
    analytics {
        // Keep analytics enabled but disable upload during development
        enabled.set(true)
        disableUpload.set(true)
    }
}

dependencies {
    // KSP support for Room Compiler.
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}