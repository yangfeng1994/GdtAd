pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven ("https://jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven ("https://jitpack.io")
        gradle.settingsEvaluated {
            flatDir {
                dirs(rootProject.children.map { it.name + "/libs" }.toTypedArray())
            }
        }
    }
}

rootProject.name = "GdtAd"
include(":app")
include(":gdtlibrary")
