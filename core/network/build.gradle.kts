import java.util.Properties

fun Properties.stringLiteral(name: String): String {
    val rawValue = getProperty(name).orEmpty().trim()
    val normalized = rawValue.removeSurrounding("\"")
    return "\"$normalized\""
}

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.adrc95.core.network"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 24

        val properties = Properties().apply {
            load(rootProject.file("local.properties").inputStream())
        }

        buildConfigField(
            "String",
            "COMIC_VINE_API_KEY",
            properties.stringLiteral("COMIC_VINE_API_KEY")
        )
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.arrow.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.squareup.retrofit.kotlinx.serialization)
    implementation(libs.google.hilt.android)
    ksp(libs.google.hilt.compiler)
    implementation(project(":data"))
    implementation(project(":domain"))

    testImplementation(libs.junit4)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.mockwebserver)
    testImplementation(project(":testing"))
}
