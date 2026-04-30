plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.adrc95.core.datastore"
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
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.google.hilt.android)
    ksp(libs.google.hilt.compiler)
    implementation(project(":data"))
    implementation(project(":domain"))

    testImplementation(libs.junit4)
    testImplementation(libs.kotlin.coroutines.test)
}
