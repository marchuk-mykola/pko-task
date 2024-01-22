plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version GradlePlugins.kotlinKspVersion
}

android {
    namespace = "com.pko.core.presentation"
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
            buildConfigField("String", "imageBaseUrl", "\"https://image.tmdb.org/t/p/w500/\"")
        }
        debug {
            buildConfigField("String", "imageBaseUrl", "\"https://image.tmdb.org/t/p/w500/\"")
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
    api(project(":core:domain"))

    api(AppDependencies.AndroidX.appCompat)
    api(AppDependencies.AndroidX.material)
    api(AppDependencies.AndroidX.constraintLayout)
    api(AppDependencies.AndroidX.fragmentKtx)

    api(AppDependencies.AndroidX.navigationFragmentKtx)
    api(AppDependencies.AndroidX.navigationUiKtx)

    api(AppDependencies.Kotlin.coroutinesAndroid)
    api(AppDependencies.AndroidX.multidex)

    api(AppDependencies.Glide.glide)
    annotationProcessor(AppDependencies.Glide.glideCompiler)

}
