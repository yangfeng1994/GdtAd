@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    `maven-publish`
}
group = "com.github.yangfeng1994"
version = "1.0.5"

android {
    namespace = "com.mantu.gdt.ad"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        ndk {
            abiFilters += arrayOf("armeabi-v7a", "arm64-v8a")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding {
        enable = true
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.mantu.gdtAd"
            artifactId = "gdtAd"
            version = "1.0.5"
        }
    }
}

dependencies {
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    api("com.qq.e.union:union:4.580.1450")
    api("com.github.bumptech.glide:glide:4.11.0")
    api("com.miui.zeus:mimo-ad-sdk:5.3.2")
    implementation ("androidx.appcompat:appcompat:1.4.1")
    implementation ("androidx.recyclerview:recyclerview:1.0.0")
    implementation ("com.google.code.gson:gson:2.8.5")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.9.0")
}