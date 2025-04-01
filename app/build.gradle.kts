plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.maps.trackola"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.maps.trackola"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
    }
}
dependencies {
    // OLA SDKs - add exclusions for duplicate classes
    implementation(files("libs/olaMapSdk.aar"))
    implementation("org.maplibre.gl:android-sdk:10.2.0")
    implementation("org.maplibre.gl:android-plugin-markerview-v9:1.0.0")
    implementation("org.maplibre.gl:android-plugin-annotation-v9:1.0.0")
    implementation("org.maplibre.gl:android-sdk-directions-models:5.9.0")
    implementation("org.maplibre.gl:android-sdk-services:5.9.0")
    implementation("org.maplibre.gl:android-sdk-turf:5.9.0")
    implementation("com.moengage:moe-android-sdk:12.6.01")

    // Android libraries
    implementation(libs.junit)
    implementation(libs.ext.junit)
    implementation(libs.espresso.core)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

}
