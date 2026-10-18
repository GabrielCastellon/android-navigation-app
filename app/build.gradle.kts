plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt") apply true
}

// Load the Google Maps API key from local.properties
val localProperties = java.util.Properties()
val localFile = rootProject.file("local.properties")

if (localFile.exists()) {
    localFile.inputStream().use {
        localProperties.load(it)
    }
}

android {
    namespace = "com.gabriel.gabrielcastellon_comp304lab4_ex1"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.gabriel.gabrielcastellon_comp304lab4_ex1"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // Insert Google Maps API key
        manifestPlaceholders["MAPS_API_KEY"] =
            localProperties.getProperty("MAPS_API_KEY", "")


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Room (Database)
    implementation("androidx.room:room-runtime:2.6.1")
    implementation(libs.play.services.location)
    implementation(libs.androidx.runtime.livedata)
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // WorkManager (Background Tasks)
    implementation("androidx.work:work-runtime-ktx:2.10.0")

    // Retrofit (API Calls)
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Google Maps SDK and Utilities
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.maps.android:maps-compose:2.14.0")
    implementation("com.google.maps.android:android-maps-utils:3.8.2")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.5.0")

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
}