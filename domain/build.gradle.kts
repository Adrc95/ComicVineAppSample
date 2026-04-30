plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(libs.arrow.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.inject.javax.inject)
    testImplementation(libs.junit4)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(project(":testing"))
}
