plugins {
    id("com.google.gms.google-services")
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // Kotlin serialization plugin for type safe routes and navigation arguments
    kotlin("plugin.serialization") version "2.0.21"

}

val googleApiKey = project.findProperty("GOOGLE_API_KEY") ?: ""

android {
    namespace = "com.example.taskassistant"
    compileSdk = 35

    defaultConfig {

        applicationId = "com.example.taskassistant"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "GOOGLE_API_KEY", "\"$googleApiKey\"")
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

        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
}

dependencies {
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation (libs.material3)
    implementation (libs.coil.compose)  // Latest Coil Compose



    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.ui.text.android)
    implementation(libs.foundation)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.androidx.activity.compose.v180)
    implementation (libs.firebase.messaging)
    implementation(libs.androidx.material3.vlatestversion)
    implementation(libs.androidx.ui.text.google.fonts.vlatestversion)
    implementation(libs.play.services.base)
    implementation (libs.androidx.material3.v110)
    implementation(platform(libs.androidx.compose.bom.v20240401))
    implementation(libs.androidx.foundation)

    // Jetpack Compose integration
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.ui.v161)
    implementation(libs.material3)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose.v172)
    implementation(libs.androidx.compose.ui.ui2)
    implementation (libs.firebase.firestore.ktx)
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.androidx.compose.ui.ui2)

    implementation(libs.androidx.ui.text.google.fonts.v181)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.ui.text.google.fonts.v167) // adjust to your Compose version
    implementation(libs.androidx.core)

//    implementation(libs.androidx.navigation.compose.jvmstubs)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


}