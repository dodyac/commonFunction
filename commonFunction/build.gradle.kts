import Libraries.androidTestImplementationX
import Libraries.implementationX
import Libraries.kaptX
import Libraries.testImplementationX

plugins {
    id(Config.Plugins.androidLibrary)
    kotlin(Config.Plugins.kotlinAndroid)
    kotlin(Config.Plugins.kotlinKapt)
    maven
}

android {
    compileSdkVersion(Versions.compileSdk)
    buildToolsVersion(Versions.buildTool)

    defaultConfig {
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = Versions.versionCode
        versionName = Versions.versionName
        testInstrumentationRunner = Config.testRunner
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures.viewBinding = true
    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    implementationX(Libraries.appLibraries)
    androidTestImplementationX(Libraries.androidTestLibraries)
    kaptX(Libraries.kapt)
    testImplementationX(Libraries.testLibraries)
}