plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "1.9.22-1.0.17"
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.planmanager"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.planmanager"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        resValue("string", "web_client_1", properties["WEB_CLIENT_1"]?.toString() ?: "358827406368-u6uknvfvrh8lfalrae8biobjjp604igi.apps.googleusercontent.com")
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
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.22-1.0.17")

    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.compose.ui:ui-android:1.6.3")
    ksp("androidx.room:room-compiler:2.6.1")

    //Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    //google
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    implementation("androidx.credentials:credentials:1.3.0-alpha01")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0-alpha01")
    implementation ("com.google.android.libraries.identity.googleid:googleid:1.1.0")

    //google calendar
    implementation ("com.google.oauth-client:google-oauth-client-jetty:1.23.0")
    implementation ("com.google.apis:google-api-services-calendar:v3-rev305-1.23.0")
    implementation ("com.google.android.gms:play-services-auth:20.4.0")

    //to avoid conflicts in libraries
    implementation ("com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava")
    implementation("com.google.api-client:google-api-client-android:1.23.0") {
        exclude(group = "org.apache.httpcomponents", module = "httpclient")
    }

    //easily control permissions
    implementation ("pub.devrel:easypermissions:3.0.0")

    implementation("androidx.compose.runtime:runtime:1.6.3")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.3")
    implementation("androidx.compose.runtime:runtime-rxjava2:1.6.3")
    implementation ("androidx.compose.ui:ui:1.6.3")
    implementation ("androidx.compose.material:material:1.6.3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

    implementation("com.squareup.moshi:moshi:1.15.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.0")

    implementation("androidx.credentials:credentials:1.3.0-alpha01")


    implementation("androidx.cardview:cardview:1.0.0")

}