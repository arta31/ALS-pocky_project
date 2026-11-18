plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.als_pocky"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.als_pocky"
        minSdk = 29
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    // Fitur baru: kalender panen (github.com/kizitonwose/Calendar)
    implementation(libs.kizitonwose.calendar.view)
    // Fitur baru: grafik dana (github.com/PhilJay/MPAndroidChart)
    implementation(libs.mpandroidchart)
    // Progres 6 (P11): RecyclerView untuk grid Beranda
    implementation(libs.androidx.recyclerview)
    // Progres 6 (P11): titik indikator halaman onboarding (github.com/tommybuonomo/dotsindicator)
    implementation(libs.dotsindicator)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}