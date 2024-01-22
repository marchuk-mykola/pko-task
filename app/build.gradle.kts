plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp") version GradlePlugins.kotlinKspVersion
}

android {
    compileSdk = 34

    defaultConfig {
        applicationId = "com.pko.movie"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    namespace = "com.pko.movie"
}

dependencies {
    implementation(project(":features:now-playing-movies"))
    implementation(project(":features:movie-details"))
    implementation(project(":core:presentation"))
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    implementation(project(":di"))

    implementation(AppDependencies.Hilt.hiltAndroid)
    kapt(AppDependencies.Hilt.hiltAndroidKapt)
}

kapt {
    correctErrorTypes = true
}