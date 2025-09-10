import com.example.android.Config
import com.example.android.Dependencies

plugins {
    alias(libs.plugins.library)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = Config.Modules.Features.onboarding
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = Config.testInstrumentationRunner

        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompiler.get()
    }
}

dependencies {

    //region modules
    implementation(project(Dependencies.Modules.core))
    implementation(project(Dependencies.Modules.domain))
    implementation(project(Dependencies.Modules.resources))
    // end region

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material)

    //region compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui.ui)
    implementation(libs.compose.ui.util)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.activity)
    implementation(libs.compose.material)
    implementation(libs.compose.material3)
    implementation(libs.compose.livedata)
    implementation(libs.compose.runtime)
    implementation(libs.accompanist.controller)
    implementation(libs.accompanist.permissions)
    implementation(libs.coil)
    // end region

    //region navigation
    implementation(libs.navigation.core)
    implementation(libs.navigation.compose)
    // end region

    //region coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.test)
    // end region

    //region Lifecycle
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.viewModel)
    implementation(libs.lifecycle.viewModelCompose)
    implementation(libs.lifecycle.viewModelSavedState)
    implementation(libs.lifecycle.compiler)
    //endregion

    //region DI
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.navigation.compose)
    //endregion

    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    implementation(libs.com.google.zxing)
    implementation(libs.com.google.guava)
    implementation(libs.lottie.compose)

    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
