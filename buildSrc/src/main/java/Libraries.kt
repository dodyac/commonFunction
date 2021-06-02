import org.gradle.api.artifacts.dsl.DependencyHandler

object Libraries {
    //Android
    private const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    private const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    private const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    private const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    private const val legacySupport = "androidx.legacy:legacy-support-v4:${Versions.legacySupport}"
    private const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Versions.fragmentKtx}"
    private const val testJunit = "androidx.test.ext:junit:${Versions.testJunit}"
    private const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
    private const val jUnit = "junit:junit:${Versions.jUnit}"
    // UI library
    private const val material = "com.google.android.material:material:${Versions.material}"
    private const val imageSlider = "com.github.smarteist:autoimageslider:${Versions.imageSlider}"
    // Network client
    private const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    private const val retrofitGson  = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    //Others
    private const val uCrop = "com.github.yalantis:ucrop:${Versions.uCrop}"
    private const val toasty = "com.github.GrenderG:Toasty:${Versions.toasty}"
    private const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    private const val swipeRefresh = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefresh}"
    private const val jodaTime = "net.danlew:android.joda:${Versions.jodaTime}"
    private const val textDrawable = "com.amulyakhare:com.amulyakhare.textdrawable:${Versions.textDrawable}"
    private const val finestWebView = "com.thefinestartist:finestwebview:${Versions.finestWebView}"
    private const val shimmerRecycler = "com.github.mike14u:shimmer-recyclerview-x:${Versions.shimmerRecycler}"
    private const val loaderView = "com.elyeproj.libraries:loaderviewlibrary:${Versions.loaderView}"
    private const val inAppUpdater = "com.github.SanojPunchihewa:InAppUpdater:${Versions.inAppUpdater}"
    private const val tabLayoutHelper = "com.h6ah4i.android.tablayouthelper:tablayouthelper:${Versions.tabLayoutHelper}"
    private const val glideKapt = "com.github.bumptech.glide:compiler:${Versions.glide}"
    private const val playCoreKtx = "com.google.android.play:core-ktx:${Versions.playCoreKtx}"

    val appLibraries = arrayListOf<String>().apply {
        add(kotlinStdlib)
        add(coreKtx)
        add(appCompat)
        add(constraintLayout)
        add(legacySupport)
        add(fragmentKtx)
        add(material)
        add(toasty)
        add(glide)
        add(swipeRefresh)
        add(jodaTime)
        add(textDrawable)
        add(finestWebView)
        add(shimmerRecycler)
        add(loaderView)
        add(inAppUpdater)
        add(tabLayoutHelper)
        add(playCoreKtx)
        add(imageSlider)
        add(retrofit)
        add(retrofitGson)
        add(uCrop)
    }

    val appRootLibraries = arrayListOf<String>().apply {
        add(kotlinStdlib)
        add(coreKtx)
        add(appCompat)
        add(constraintLayout)
        add(legacySupport)
        add(fragmentKtx)
        add(material)
    }

    val kapt = arrayListOf<String>().apply {
        add(glideKapt)
    }

    val testLibraries = arrayListOf<String>().apply {
        add(jUnit)
    }

    val androidTestLibraries = arrayListOf<String>().apply {
        add(testJunit)
        add(espressoCore)
    }

    fun DependencyHandler.implementationX(list: List<String>) {
        list.forEach { dependency -> add("implementation", dependency) }
    }

    fun DependencyHandler.kaptX(list: List<String>) {
        list.forEach { dependency -> add("kapt", dependency) }
    }

    fun DependencyHandler.androidTestImplementationX(list: List<String>) {
        list.forEach { dependency -> add("androidTestImplementation", dependency) }
    }

    fun DependencyHandler.testImplementationX(list: List<String>) {
        list.forEach { dependency -> add("testImplementation", dependency) }
    }
}