plugins {
    id(Config.Plugins.android)
    id(Config.Plugins.kotlinAndroid)
    id(Config.Plugins.kotlinKapt)
}

android {
    compileSdkVersion(Versions.compileSdk)

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
    implementation(Libraries.kotlinStdlib)
    implementation(Libraries.coreKtx)
    implementation(Libraries.appCompat)
    implementation(Libraries.constraintLayout)
    implementation(Libraries.legacySupport)
    implementation(Libraries.fragmentKtx)
    androidTestImplementation(Libraries.testJunit)
    androidTestImplementation(Libraries.espressoCore)
    implementation(Libraries.material)
    implementation(Libraries.toasty)
    implementation(Libraries.glide)
    implementation(Libraries.swipeRefresh)
    implementation(Libraries.jodaTime)
    implementation(Libraries.textDrawable)
    implementation(Libraries.finestWebView)
    implementation(Libraries.shimmerRecycler)
    implementation(Libraries.loaderView)
    implementation(Libraries.inAppUpdater)
    implementation(Libraries.tabLayoutHelper)
    implementation(Libraries.playCoreKtx)
    kapt(Libraries.glideKapt)
    implementation(Libraries.imageSlider)
    implementation(Libraries.retrofit)
    implementation(Libraries.retrofitGson)
    implementation(Libraries.uCrop)
    testImplementation(Libraries.jUnit)
}