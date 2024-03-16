@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.kotlin)
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.itskidan.core_impl"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
        dataBinding = true
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Modules
    api(project(":core_api"))

    // Room
    implementation (libs.room.runtime)
    implementation (libs.room.ktx)
    implementation (libs.room.rxjava3)
    kapt (libs.room.compiler)

    // RxJava
    implementation (libs.rxjava3.rxjava)
    implementation (libs.rxjava3.rxandroid)
    implementation (libs.rxjava3.rxkotlin)

    //dagger
    implementation (libs.dagger)
    kapt(libs.dagger.compiler)
}