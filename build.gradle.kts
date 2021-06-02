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
    repositories {
        mavenCentral()
        google()
        maven { url = uri(Config.Repositories.jitPack) }
        maven { url = uri(Config.Repositories.pluginGradle) }
    }
}

tasks.register("clean",Delete::class){ delete(rootProject.buildDir) }