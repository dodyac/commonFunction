object Config {

    const val testRunner = "androidx.test.runner.AndroidJUnitRunner"

    object Dependencies {
        const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
        const val androidPlugin = "com.android.tools.build:gradle:${Versions.gradle}"
    }

    object Repositories {
        const val jitPack = "https://jitpack.io"
        const val pluginGradle = "https://plugins.gradle.org/m2/"
    }

    object Plugins {
        const val android = "com.android.application"
        const val kotlinAndroid = "android"
        const val kotlinKapt = "kapt"
        const val androidLibrary = "com.android.library"
    }
}