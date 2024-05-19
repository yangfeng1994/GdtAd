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

//publishing {
//    publications {
//        create<MavenPublication>("maven") {
//            groupId = "com.mantu.gdtAd"
//            artifactId = "gdtAd"
//            version = "1.0.4"
//        }
//    }
//}

dependencies {
    implementation("com.makeramen:roundedimageview:2.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("com.qq.e.union:union:4.575.1445")
    implementation("com.github.bumptech.glide:glide:4.11.0")
    implementation(files("libs/meidiation-TTAdapter-5.8.1.3.aar"))
    implementation(files("libs/meidiation-Util.aar"))
    implementation(files("libs/open_ad_sdk_5.8.1.3.aar"))
}