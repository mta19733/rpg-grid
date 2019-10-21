import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("de.mannodermaus.android-junit5")
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
    implementation("io.reactivex.rxjava2:rxkotlin:2.4.0")

    val koinVersion = "2.0.1"
    implementation("org.koin:koin-androidx-viewmodel:$koinVersion")
    implementation("org.koin:koin-androidx-ext:$koinVersion")
    implementation("org.koin:koin-android:$koinVersion")

    // Core, Android.
    implementation("com.google.android.material:material:1.1.0-beta01")

    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.lifecycle:lifecycle-extensions:2.1.0")
    implementation("androidx.viewpager:viewpager:1.0.0")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.1.0")

    // Tests.
    testImplementation("org.assertj:assertj-core:3.13.2")
    testImplementation("io.mockk:mockk:1.9.3")

    val junitVersion = "5.5.2"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}
