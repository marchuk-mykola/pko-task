object GradlePlugins {
    private const val androidBuildToolsVersion = "7.1.1"
    const val kotlinKspVersion = "1.9.10-1.0.13"

    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${AppDependencies.kotlinVersion}"

    const val androidBuildTools = "com.android.tools.build:gradle:$androidBuildToolsVersion"
    const val hiltPlugin = "com.google.dagger:hilt-android-gradle-plugin:${AppDependencies.Hilt.hiltPluginVersion}"
}