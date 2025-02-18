import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.goalpanzi.mission_mate.core.data.history"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}

dependencies {

    implementation(libs.bundles.test)
    implementation(libs.bundles.coroutines)
    implementation(libs.retrofit)

    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)

    implementation(project(":core:data:common"))
    implementation(project(":core:data:mission"))
    implementation(project(":core:domain:common"))
    implementation(project(":core:domain:history"))
    implementation(project(":core:domain:mission"))
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))
}
