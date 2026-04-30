import java.util.Properties

fun Properties.stringLiteral(name: String): String {
    val rawValue = getProperty(name).orEmpty().trim()
    val normalized = rawValue.removeSurrounding("\"")
    return "\"$normalized\""
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.legacy.kapt)
    alias(libs.plugins.navigation.safe.args)
}

android {
    namespace = "com.adrc95.comicvineappsample"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.adrc95.comicvineappsample"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.adrc95.comicvineappsample.HiltTestRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
        dataBinding = true
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.google.material)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.google.hilt.android)
    ksp(libs.google.hilt.compiler)
    implementation(libs.arrow.core)
    implementation(libs.hdodenhof.circleimageview)
    implementation(libs.airbnb.lottie)
    implementation(libs.apachat.swipereveallayout)
    implementation(libs.bumptech.glide)
    ksp(libs.bumptech.glide.compiler)
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    implementation(project(":data"))
    implementation(project(":domain"))
    testImplementation(libs.junit4)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(project(":testing"))
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.google.hilt.testing)
    androidTestImplementation(libs.mockwebserver)
    androidTestImplementation(libs.androidx.room.runtime)
    androidTestImplementation(libs.androidx.datastore.preferences)
    androidTestImplementation(libs.kotlin.coroutines.test)
    androidTestImplementation(libs.kotlinx.serialization.json)
    androidTestImplementation(libs.squareup.retrofit)
    androidTestImplementation(libs.squareup.retrofit.kotlinx.serialization)
    androidTestImplementation(libs.squareup.okhttp.logging)
    kspAndroidTest(libs.google.hilt.compiler)
}
