plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp") version GradlePlugins.kotlinKspVersion
}

android {
    namespace = "com.pko.core.data"
    compileSdk = AndroidConfig.compileSdk

    defaultConfig {
        minSdk = AndroidConfig.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("String", "baseUrl", "\"https://api.themoviedb.org/3/\"")
            buildConfigField("String", "imageBaseUrl", "\"https://image.tmdb.org/t/p/w500/\"")
            buildConfigField("String", "apiKey", "\"7ccb52399a2dc14ab708b25db1ddda46\"")
        }
        debug {
            buildConfigField("String", "baseUrl", "\"https://api.themoviedb.org/3/\"")
            buildConfigField("String", "imageBaseUrl", "\"https://image.tmdb.org/t/p/w500/\"")
            buildConfigField("String", "apiKey", "\"7ccb52399a2dc14ab708b25db1ddda46\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:domain"))

    api(AppDependencies.AndroidX.roomRuntime)
    api(AppDependencies.AndroidX.roomKtx)
    ksp(AppDependencies.AndroidX.roomCompiler)

    api(AppDependencies.Retrofit.refrofit)
    api(AppDependencies.Retrofit.loggingInterceptor)
    api(AppDependencies.Retrofit.retrofitMoshiConverter)
    api(AppDependencies.Retrofit.retrofitScalarsConverter)

    ksp(AppDependencies.Moshi.moshiKapt)
    api(AppDependencies.Moshi.moshiKotlin)

    api(AppDependencies.Kotlin.coroutinesCore)

    api(AppDependencies.Lifecycle.lifecycleExtensions)

    implementation(AppDependencies.Hilt.hiltAndroid)
    kapt(AppDependencies.Hilt.hiltAndroidKapt)
}
