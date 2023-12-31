import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version libs.versions.kotlin.get()
    id("com.squareup.sqldelight")
    id("com.android.library")
}

kotlin {
    androidTarget()

//    listOf(
//        iosX64(),
//        iosArm64(),
//        iosSimulatorArm64()
//    ).forEach { iosTarget ->
//        iosTarget.binaries.framework {
//            baseName = "shared"
//            isStatic = true
//        }
//    }

    sourceSets {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        targetHierarchy.default()

        val commonMain by getting {
            dependencies {
                implementation(libs.coroutines)

                implementation(libs.ktor.core)
                implementation(libs.ktor.contentnegotiation)
                implementation(libs.ktor.serialization)

                implementation(libs.sqldelight.runtime)

                api(libs.koin.core)
            }
        }


        val androidMain by getting {
            dependencies {
                api(libs.androidx.core.ktx)
                implementation(libs.ktor.android)
                implementation(libs.sqldelight.android)
            }
        }

//        val iosX64Main by getting
//        val iosArm64Main by getting
//        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)

            dependencies{
                implementation(libs.ktor.drawin)
                implementation(libs.sqldelight.native)
            }

//            iosX64Main.dependsOn(this)
//            iosArm64Main.dependsOn(this)
//            iosSimulatorArm64Main.dependsOn(this)
        }
    }
}

android {
     compileSdk = libs.versions.compileSdk.get().toInt()
    namespace = "ir.quran.bayan.common"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}
