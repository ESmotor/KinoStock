plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
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
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.gridlayout:gridlayout:1.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.airbnb.android:lottie:6.1.0")
    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.6.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")
    // Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")
    // paging
    implementation ("androidx.paging:paging-runtime:3.2.1")
    implementation ("androidx.paging:paging-runtime-ktx:3.2.1")
    // delegate
    implementation( "com.hannesdorfmann:adapterdelegates4-kotlin-dsl-viewbinding:4.3.2")
    //dagger
    implementation ("com.google.dagger:dagger:2.43.2")
    kapt("com.google.dagger:dagger-compiler:2.43.2")
    // Room
    implementation ("androidx.room:room-runtime:2.6.0")
    implementation ("androidx.room:room-ktx:2.6.0")
    kapt ("androidx.room:room-compiler:2.6.0")
    // Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

}