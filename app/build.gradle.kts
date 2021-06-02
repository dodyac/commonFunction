import Libraries.androidTestImplementationX
import Libraries.implementationX
import Libraries.testImplementationX

plugins {
    maven
    id(Config.Plugins.android)
    kotlin(Config.Plugins.kotlinAndroid)
}

android {
    compileSdkVersion(Versions.compileSdk)
    buildToolsVersion(Versions.buildTool)

    defaultConfig {
        applicationId = ApplicationId.appId
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = Versions.versionCode
        versionName = Versions.versionName
        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = Config.testRunner
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
    implementationX(Libraries.appRootLibraries)
    androidTestImplementationX(Libraries.androidTestLibraries)
    testImplementationX(Libraries.testLibraries)
}