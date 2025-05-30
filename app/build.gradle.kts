

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.andrayudu.cicdin"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.andrayudu.cicdin"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") { // Use create for named configurations
            // These will be provided by GitHub Actions Secrets
            // Fallback to project properties for local builds if needed
            storeFile = file(System.getenv("UPLOAD_KEYSTORE_FILE") ?: project.findProperty("UPLOAD_KEYSTORE_FILE") ?: "E:\\CICD keystore")
            storePassword = System.getenv("UPLOAD_KEYSTORE_PASSWORD") ?: project.findProperty("UPLOAD_KEYSTORE_PASSWORD") as? String
            keyAlias = System.getenv("UPLOAD_KEY_ALIAS") ?: project.findProperty("UPLOAD_KEY_ALIAS") as? String
            keyPassword = System.getenv("UPLOAD_KEY_PASSWORD") ?: project.findProperty("UPLOAD_KEY_PASSWORD") as? String
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
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