plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.myapplication.test.HiltCucumberRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("path/to/release.keystore")
            storePassword = "yourStorePassword"
            keyAlias = "yourKeyAlias"
            keyPassword = "yourKeyPassword"
        }
    }

    buildTypes {
        debug {
            buildConfigField(
                "String",
                "JSON_REPORT_PLUGIN",
                "\"json:/data/data/com.example.myapplication/files/cucumber.json\"",
            )
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    @Suppress("UnstableApiUsage")
    testOptions {
        animationsDisabled = true
        managedDevices {
            localDevices {
                create("pixel2api34") {
                    device = "Pixel 2"
                    apiLevel = 34
                    systemImageSource = "aosp"
                }
            }
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
    implementation(libs.androidx.browser)
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    androidTestImplementation(libs.cucumber.android)
    androidTestImplementation(libs.cucumber.android.hilt)

    // Compose UI テストと Espresso Intents を併用
    androidTestImplementation(libs.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.espresso.intents)
    androidTestImplementation(libs.androidx.test.uiautomator)

    // Hilt＋Compose 連携が要るなら
    implementation(libs.hilt.navigation.compose)

    // androidTest（DI を使う Step 定義用）
    androidTestImplementation(libs.dagger.hilt.android)
    kspAndroidTest(libs.dagger.hilt.compiler)
}
