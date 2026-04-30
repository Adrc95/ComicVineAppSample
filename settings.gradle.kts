pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "ComicVineAppSample"

include(
    ":app",
    ":domain",
    ":testing",
    ":data",
    ":core:database",
    ":core:network",
    ":core:datastore"
)
