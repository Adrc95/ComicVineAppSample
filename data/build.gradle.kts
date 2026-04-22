plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(libs.arrow.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.inject.javax.inject)
    implementation(libs.touchlab.kermit)
    implementation(project(":domain"))
}
