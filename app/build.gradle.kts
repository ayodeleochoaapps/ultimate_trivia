plugins {

    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)

    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
    id("kotlin-kapt")


}

repositories {
    google() // For Android dependencies
    mavenCentral() // Maven Central for Google Cloud libraries
    maven {
        url = uri("https://maven.pkg.dev/google-cloud-java/releases")
    }
}

android {
    namespace = "com.ayoapps.blackcarddenied"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ayoapps.blackcarddenied"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        compose = false
        buildConfig = true
        dataBinding = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packagingOptions {
        resources {
        //    excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
            pickFirsts += "META-INF/DEPENDENCIES"
            pickFirsts += "META-INF/INDEX.LIST"
        }
    }
}

dependencies {

    implementation("androidx.databinding:databinding-runtime:8.7.2")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.generativeai)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.support.annotations)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("com.google.cloud:google-cloud-aiplatform:3.35.0")
   // implementation("com.google.protobuf:protobuf-javalite:3.25.5")
    implementation("io.grpc:grpc-okhttp:1.62.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    // kapt("androidx.lifecycle:lifecycle-compiler:2.8.7")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    // kapt("androidx.lifecycle:lifecycle-compiler:2.5.1")
    // kapt("androidx.lifecycle:lifecycle-compiler:2.3.1")

    kapt("com.android.tools.build:gradle:8.1.0")
    implementation ("com.google.code.gson:gson:2.8.8")





    //  implementation(libs.google.cloud.vertexai.v1140)

/*    implementation(libs.firebase.vertexai)

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))

    // Add the dependency for the Vertex AI in Firebase library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-vertexai")

    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")

    implementation("com.google.generativeai:generative-ai:1.0.0")
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")*/



    //  implementation("com.google.cloud:google-cloud-vertexai:1.11.2")



/*    // Force versions for conflicting transitive dependencies
    constraints {
        implementation("com.google.api.grpc:proto-google-common-protos:2.49.0") {
            because("Avoid conflicts between versions of proto-google-common-protos")
        }
        implementation("com.google.auth:google-auth-library-oauth2-http:1.30.0")
    }*/
    implementation(libs.google.auth.library.oauth2.http.v1220)

   // implementation(libs.google.cloud.vertexai.v281)
  //  implementation(libs.google.auth.library.oauth2.http)
/*    implementation("com.google.cloud:google-cloud-vertex-ai:2.8.0") // Vertex AI dependency
    implementation("com.google.auth:google-auth-library-oauth2-http:1.6.0") // Authentication library
    implementation("com.google.api-client:google-api-client:1.33.0") // API client library
    implementation ("com.google.cloud:google-cloud-aiplatform:3.35.0")*/



    val composeBom = platform("androidx.compose:compose-bom:2024.09.02")
    implementation(composeBom)

/*    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.ui:ui-tooling-preview")*/
}