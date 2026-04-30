plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.adrc95.data"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.arrow.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.google.hilt.android)
    ksp(libs.google.hilt.compiler)
    implementation(project(":domain"))

    testImplementation(libs.junit4)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(project(":testing"))
}
