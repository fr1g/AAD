plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "moe.vot.own.projs.aad.pr.clipperget"
    compileSdk = 36

    defaultConfig {
        applicationId = "moe.vot.own.projs.aad.pr.clipperget"
        minSdk = 29
        targetSdk = 36
        versionCode = 3
        versionName = "1.1.1"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.camera.core)
    implementation(libs.camera.view)
    implementation(libs.camera.lifecycle)
    implementation(libs.camera.video)
    implementation(libs.camera.camera2)

    testImplementation(libs.junit)

    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}