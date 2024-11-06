import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.project.bonggong"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.project.bonggong"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "OPENAI_API_KEY", gradleLocalProperties(rootDir).getProperty("OPENAI_API_KEY"))
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            // proguard 활성화
            isMinifyEnabled = false

            // 기본 설정
            proguardFiles (
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
                "proguard-rules-debug.pro"  // debug version에 대한 별도의 룰 적용
            )
        }
        release {
            // proguard 활성화
            isMinifyEnabled = false

            // 기본 설정
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Glide 라이브러리 및 컴파일러 추가
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)

    // Navigation 라이브러리 추가
    implementation (libs.navigation.fragment.ktx)
    implementation (libs.navigation.ui.ktx)

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // markdown 라이브러리 추가
    implementation ("io.noties.markwon:core:4.6.2")
    implementation ("io.noties.markwon:linkify:4.6.2")

    //Material Components 라이브러리 추가
    implementation ("com.google.android.material:material:1.8.0")
}