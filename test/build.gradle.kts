@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("java-library")
    alias(libs.plugins.org.jetbrains.kotlin.jvm)

}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // No Module

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.javax.inject)

//    implementation(libs.hilt.android)
//    kapt(libs.hilt.compiler)
//    coreLibraryDesugaring(libs.desugar.jdk.libs)

}