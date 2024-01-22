object AppDependencies {
    const val kotlinVersion = "1.8.20"
    const val lifecycleExtensionVersion = "1.1.1"
    const val glideVersion = "4.13.0"
    const val roomVersion = "2.6.0"

    object Lifecycle {
        const val lifecycleExtensions = "android.arch.lifecycle:extensions:$lifecycleExtensionVersion"
    }

    object AndroidX {
        const val appCompat = "androidx.appcompat:appcompat:1.6.1"
        const val material = "com.google.android.material:material:1.11.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.4"
        const val roomRuntime = "androidx.room:room-runtime:$roomVersion"
        const val roomCompiler = "androidx.room:room-compiler:$roomVersion"
        const val roomKtx = "androidx.room:room-ktx:$roomVersion"
        const val navigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:2.7.6"
        const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:2.7.6"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:1.6.2"
        const val multidex = "com.android.support:multidex:2.0.1"
    }

    object Kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
        const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1"
        const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1"
    }

    object Retrofit {
        private const val retrofitVersion = "2.9.0"
        private const val loggingVersion = "4.9.3"
        private const val scalarsVersion = "2.9.0"

        const val refrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
        const val retrofitMoshiConverter = "com.squareup.retrofit2:converter-moshi:$retrofitVersion"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$loggingVersion"
        const val retrofitScalarsConverter = "com.squareup.retrofit2:converter-scalars:$scalarsVersion"
    }

    object Moshi {
        private const val moshiVersion = "1.15.0"

        const val moshiKapt = "com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion"
        const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:$moshiVersion"
    }

    object Glide {
        const val glide = "com.github.bumptech.glide:glide:$glideVersion"
        const val glideCompiler = "com.github.bumptech.glide:compiler:$glideVersion"
    }

    object Hilt {
        private const val hiltVersion = "2.46"
        const val hiltPluginVersion = "2.46"

        const val hiltAndroidKapt = "com.google.dagger:hilt-android-compiler:$hiltVersion"

        const val hiltAndroid = "com.google.dagger:hilt-android:$hiltVersion"
    }
}
