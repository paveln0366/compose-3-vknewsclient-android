import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.vkid.manifest.placeholders)
    alias(libs.plugins.kotlin.kapt)
}

val localProps = Properties()
rootProject.file("local.properties").inputStream().use { localProps.load(it) }

val clientId: String = localProps.getProperty("VKIDClientID")
    ?: error("VKIDClientID is not set in local.properties")
val clientSecret: String = localProps.getProperty("VKIDClientSecret")
    ?: error("VKIDClientSecret is not set in local.properties")


android {
    namespace = "com.example.vknewsclient"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.vknewsclient"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        addManifestPlaceholders(
            mapOf(
                "VKIDClientID" to clientId,
                "VKIDClientSecret" to clientSecret,
                "VKIDRedirectHost" to "vk.ru",
                "VKIDRedirectScheme" to "vk$clientId",
            )
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug") // For release build
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.com.google.code.gson)
    implementation(libs.io.coil.kt.coil3.compose)
    implementation(libs.io.coil.kt.coil3.compose)
    implementation(libs.com.vk.android.sdk.core)
    implementation(libs.com.vk.android.sdk.api)
    implementation(libs.vkid)
    implementation(libs.coil.compose)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
}