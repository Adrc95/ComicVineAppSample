plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(libs.arrow.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.inject.javax.inject)
}
