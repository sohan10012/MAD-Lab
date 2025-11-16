plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.passapp"
    compileSdk = 36   // FIXED

    defaultConfig {
        applicationId = "com.example.passapp"
        minSdk = 24
        targetSdk = 36   // FIXED
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {

    // Default Android libs
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // RecyclerView for password list UI
    implementation("androidx.recyclerview:recyclerview:1.3.0")

    // AES-256 encrypted shared preferences
    implementation("androidx.security:security-crypto:1.1.0")

    // Fingerprint / biometric unlock
    implementation("androidx.biometric:biometric:1.2.0-alpha04")

    // Gson for JSON storage
    implementation("com.google.code.gson:gson:2.10.1")

    implementation("com.google.android.material:material:1.9.0")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
