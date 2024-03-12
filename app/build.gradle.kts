plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin)
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.itskidan.kinostock"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.itskidan.kinostock"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
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
    implementation(libs.fragment.ktx)
    implementation(libs.recyclerview)
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.gridlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //Lottie
    implementation (libs.lottie)

    //Retrofit
    implementation (libs.retrofit2)
    implementation (libs.retrofit2.converter.gson)

    //Okhttp
    implementation (libs.okhttp3.logging.interceptor)

    // Glide
    implementation (libs.glide)
    annotationProcessor (libs.glide.compiler)

    // paging
    implementation (libs.paging.runtime)
    implementation (libs.paging.runtime.ktx)

    // delegate
    implementation( libs.adapterdelegates4.kotlin.dsl.viewbinding)

    //dagger
    implementation (libs.dagger)
    kapt(libs.dagger.compiler)

    // Room
    implementation (libs.room.runtime)
    implementation (libs.room.ktx)
    implementation (libs.room.rxjava3)
    kapt (libs.room.compiler)

    // Coroutines
    implementation (libs.coroutines.core)
    implementation (libs.coroutines.android)

    // LifeCycle
    implementation(libs.lifecycle.viewmodel.ktx)

    // Timber
    implementation(libs.timber)

    // RxJava
    implementation (libs.rxjava3.rxjava)
    implementation (libs.rxjava3.rxandroid)
    implementation (libs.rxjava3.rxkotlin)
    implementation (libs.rxjava3.retrofit.adapter)

    //Modules
    implementation(project(":remote_module"))
}