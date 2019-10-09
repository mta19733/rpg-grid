import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(29)

    buildToolsVersion = "29.0.2"

    defaultConfig {
        targetSdkVersion(29)
        minSdkVersion(22)

        applicationId = "com.aau.dnd"
        versionName = "1.0"
        versionCode = 1
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    sourceSets {
        val sets = listOf(
            getByName("test"),
            getByName("main")
        )

        sets.forEach { set ->
            set.java.srcDirs("src/${set.name}/kotlin")
        }
    }

    packagingOptions {
        exclude("META-INF/proguard/coroutines.pro")
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {

    // Language.
    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))

    // Utilities.
    implementation("com.polidea.rxandroidble2:rxandroidble:1.10.2")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")

    // Google.
    implementation("com.google.android.material:material:1.0.0")

    // Core, Android.
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.viewpager:viewpager:1.0.0")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.1.0")

    // Tests.
    testImplementation("org.assertj:assertj-core:3.13.2")
    testImplementation("junit:junit:4.12")
}
