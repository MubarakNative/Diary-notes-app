plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
}

android {
    namespace = "com.mubarak.madexample"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mubarak.madexample"
        minSdk = 21
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        dataBinding = true
    }
}

dependencies {

    val roomVersion = "2.6.1"
    val daggerVersion = "2.50"
    val viewModelVersion = "2.7.0"
    val coroutineVersion = "1.7.3"
    val navVersion = "2.7.7"

    // Android Core (Kotlin)
    implementation("androidx.core:core-ktx:1.12.0")

    // Android Core (Ui)
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.google.android.material:material:1.11.0")
    implementation ("androidx.coordinatorlayout:coordinatorlayout:1.2.0")


    // Hilt (Dependency Injection)
    implementation("com.google.dagger:hilt-android:$daggerVersion")
    ksp("com.google.dagger:hilt-android-compiler:$daggerVersion")

    // Room (Local Database)
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-paging:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$viewModelVersion")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    // Kotlin Ktx (for simplicity delegate's etc,..)
    implementation ("androidx.fragment:fragment-ktx:1.6.2")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    // Preference (Data Store)
    implementation ("androidx.datastore:datastore-preferences:1.0.0")

    // Coroutines (Asynchronous Task)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion")

    // Preference Setting Compat (Setting screen)
    implementation ("androidx.preference:preference-ktx:1.2.1")

    // LeakCanary
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.13")

    // Test
    testImplementation("junit:junit:4.13.2")
    testImplementation ("androidx.arch.core:core-testing:2.2.0")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    testImplementation("androidx.test.ext:junit-ktx:1.1.5")
    testImplementation("androidx.test:core-ktx:1.5.0")




    // Example Instrumental Test
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}