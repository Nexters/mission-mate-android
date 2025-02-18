import com.goalpanzi.mission_mate.convention.configureHiltAndroid
import com.goalpanzi.mission_mate.convention.libs

plugins {
    id("missionmate.android.library")
    id("missionmate.android.compose")
}

android {
    packaging {
        resources {
            excludes.add("META-INF/**")
        }
    }
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

}

configureHiltAndroid()

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation"))
    implementation(project(":core:domain:user"))
    implementation(project(":core:domain:setting"))
    implementation(project(":core:domain:mission"))
    implementation(project(":core:domain:common"))
    implementation(project(":core:domain:auth"))
    implementation(project(":core:domain:onboarding"))
    implementation(project(":core:domain:history"))
    implementation(project(":core:ui"))

}
