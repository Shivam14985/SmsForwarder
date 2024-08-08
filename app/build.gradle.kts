plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.shivambhardwaj.asifhood.smsforwarder"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.shivambhardwaj.asifhood.smsforwarder"
        minSdk = 23
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.play.services.ads)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation ("com.squareup.okhttp3:okhttp:4.12.0")
    //animation
    implementation("com.airbnb.android:lottie:6.5.0")
    //google signin
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    //image loading
    implementation("com.squareup.picasso:picasso:2.71828")
    //timeAgo
    implementation("com.github.marlonlom:timeago:4.0.3")
    //ShimmerEffect
    implementation("com.facebook.shimmer:shimmer:0.5.0@aar")
    //SwipeRefresh
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")
}