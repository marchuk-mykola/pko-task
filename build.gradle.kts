plugins {
    id("com.android.application") version "8.1.1" apply false
    id("com.android.library") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version AppDependencies.kotlinVersion apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://maven.google.com")
    }
    dependencies {
        classpath(GradlePlugins.hiltPlugin)
        classpath(GradlePlugins.androidBuildTools)
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.6")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

allprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }
}