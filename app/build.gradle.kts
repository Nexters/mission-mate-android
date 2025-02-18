import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("missionmate.android.application")
    alias(libs.plugins.google.service)
}

android {
    namespace = "com.goalpanzi.mission_mate"

    defaultConfig {
        applicationId = "com.goalpanzi.mission_mate"
        targetSdk = 34
        versionCode = 7
        versionName = "1.1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("./mission-mate-keystore.jks")
            storePassword = gradleLocalProperties(rootDir, providers).getProperty("SIGNING_STORE_PASSWORD")
            keyAlias =  gradleLocalProperties(rootDir, providers).getProperty("SIGNING_KEY_ALIAS")
            keyPassword =  gradleLocalProperties(rootDir, providers).getProperty("SIGNING_KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isDebuggable = false
        }
    }
}

dependencies {

    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.analytics)

    implementation(project(":feature:main"))
    implementation(project(":feature:login"))
    implementation(project(":core:designsystem"))

    implementation(project(":core:data:auth"))
    implementation(project(":core:data:common"))
    implementation(project(":core:data:mission"))
    implementation(project(":core:data:onboarding"))
    implementation(project(":core:data:setting"))
    implementation(project(":core:data:user"))
    implementation(project(":core:data:history"))
    implementation(project(":core:push"))
    implementation(project(":core:notification"))
}
