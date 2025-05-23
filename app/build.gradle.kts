plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.ingoma.tourism"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ingoma.tourism"
        minSdk = 26
        targetSdk = 35
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation ("com.facebook.fresco:fresco:3.0.0")
    implementation ("com.facebook.shimmer:shimmer:0.5.0")
    implementation ("me.relex:circleindicator:2.1.6")
   // implementation ("com.github.User:Repo:Tag")
    implementation ("com.github.smarteist:autoimageslider:1.4.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")

    implementation("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")
    implementation("androidx.activity:activity:1.8.0")
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("com.kizitonwose.calendar:view:2.0.3")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation ("com.hbb20:ccp:2.5.0")
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    implementation ("com.github.aabhasr1:OtpView:v1.1.2")




}