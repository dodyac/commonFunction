buildscript {
    repositories {
        mavenCentral()
        jcenter()
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
        jcenter()
        google()
        maven { url = uri(Config.Repositories.gradleMaven)  }
    }
}

tasks.register("clean",Delete::class){ delete(rootProject.buildDir) }