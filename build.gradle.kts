buildscript {
    repositories {
        jcenter()
        google()
    }

    dependencies {
        classpath("de.mannodermaus.gradle.plugins:android-junit5:1.5.2.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.50")
        classpath("com.android.tools.build:gradle:3.5.1")
    }
}

allprojects {
    repositories {
        jcenter()
        google()
    }
}

tasks {
    withType<Wrapper> {
        gradleVersion = "5.6.2"
    }

    register<Delete>("clean") {
        delete(buildDir)
    }
}
