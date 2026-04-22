plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(libs.arrow.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(project(":data"))
    implementation(project(":domain"))
}
