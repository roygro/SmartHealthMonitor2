import java.util.Properties

// Leer local.properties
val localProps = Properties()
val localPropertiesFile = file("local.properties")
if (localPropertiesFile.exists()) {
    localProps.load(localPropertiesFile.inputStream())
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)  // ← AGREGAR
    id("com.google.devtools.ksp") version "2.0.21-1.0.28"
}

android {
    namespace = "mx.edu.utng.prgs.smarthealthmonitor2"
    compileSdk = 36

    defaultConfig {
        applicationId = "mx.edu.utng.prgs.smarthealthmonitor2"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Leer credenciales de Neon desde local.properties
        buildConfigField("String", "NEON_API_KEY",
            "\"${localProps.getProperty("NEON_API_KEY", "")}\"")
        buildConfigField("String", "NEON_HOST",
            "\"${localProps.getProperty("NEON_HOST", "")}\"")
        buildConfigField("String", "NEON_DB",
            "\"${localProps.getProperty("NEON_DB", "")}\"")
        buildConfigField("String", "NEON_USER",
            "\"${localProps.getProperty("NEON_USER", "")}\"")
        buildConfigField("String", "NEON_PASSWORD",
            "\"${localProps.getProperty("NEON_PASSWORD", "")}\"")
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
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.navigation.compose)
    implementation("com.google.android.gms:play-services-wearable:18.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")


    // Cast SDK
    implementation("androidx.mediarouter:mediarouter:1.7.0")
    implementation("com.google.android.gms:play-services-cast-framework:21.5.0")

    // Eclipse Paho MQTT para Android
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    implementation("org.eclipse.paho:org.eclipse.paho.android.service:1.1.1")

    // Kotlinx Serialization para JSON
    implementation(libs.kotlinx.serialization.json)

    // Retrofit + OkHttp para llamadas a Neon HTTP API
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // WorkManager para sync periódico en background
    implementation("androidx.work:work-runtime-ktx:2.9.1")

}