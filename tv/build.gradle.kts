import java.util.Properties

// ⭐ Leer local.properties
val localProps = Properties()
val localPropertiesFile = file("../local.properties")
if (localPropertiesFile.exists()) {
    localProps.load(localPropertiesFile.inputStream())
}
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    alias(libs.plugins.kotlin.serialization)  // ← AGREGAR
}

android {
    namespace = "mx.edu.utng.tv"
    compileSdk = 36

    defaultConfig {
        applicationId = "mx.edu.utng.tv"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        // ⭐ BuildConfig para Neon
        buildConfigField("String", "NEON_HOST", "\"${localProps.getProperty("NEON_HOST", "")}\"")
        buildConfigField("String", "NEON_API_KEY", "\"${localProps.getProperty("NEON_API_KEY", "")}\"")
        buildConfigField("String", "NEON_DB", "\"${localProps.getProperty("NEON_DB", "")}\"")
        buildConfigField("String", "NEON_USER", "\"${localProps.getProperty("NEON_USER", "")}\"")
        buildConfigField("String", "NEON_PASSWORD", "\"${localProps.getProperty("NEON_PASSWORD", "")}\"")

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true  // ⭐ AGREGAR ESTO
    }
}

dependencies {
    // Core
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")

    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.10.01"))

    // Compose Core
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.compose.runtime:runtime-livedata")

    // Compose for TV
    implementation("androidx.tv:tv-foundation:1.0.0-alpha10")
    implementation("androidx.tv:tv-material:1.0.0-alpha10")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.8.5")

    // ViewModel + Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")

    // Activity Compose
    implementation("androidx.activity:activity-compose:1.9.3")

    // ExoPlayer para reproducción
    implementation("com.google.android.exoplayer:exoplayer-core:2.19.1")
    implementation("com.google.android.exoplayer:exoplayer-ui:2.19.1")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    implementation("org.eclipse.paho:org.eclipse.paho.android.service:1.1.1")
    implementation(libs.kotlinx.serialization.json)

    // Retrofit para Neon
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
}