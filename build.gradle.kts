// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7")
    }
}


plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    kotlin("plugin.serialization") version "1.9.10" apply false
    alias(libs.plugins.ksp)  apply false
}