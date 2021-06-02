buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath(Config.Dependencies.androidPlugin)
        classpath(Config.Dependencies.kotlinPlugin)
    }
}

allprojects {
    apply(plugin = "maven")
    repositories {
        mavenCentral()
        google()
        maven { url = uri(Config.Repositories.jitPack) }
        maven { url = uri(Config.Repositories.pluginGradle) }
    }
}