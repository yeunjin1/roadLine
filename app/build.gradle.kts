import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.lgcns.crossdev.onboarding1.roadline"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.lgcns.crossdev.onboarding1.roadline"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Set the properties within `local.properties` into a `Properties` class so that values
        // within `local.properties` (e.g. Maps API key) are accessible in this file.
        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        manifestPlaceholders["MAPS_API_KEY"] = properties.getProperty("MAPS_API_KEY", "")
        // Set API keys in BuildConfig
        buildConfigField("String", "MAPS_API_KEY", "\"${properties.getProperty(" MAPS_API_KEY ")}\"")
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    // All Modules
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":presentation"))

    implementation(libs.androidx.appcompat)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.timber)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

}