buildscript {
    repositories {
        jcenter()
        google()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.50")
        classpath("com.android.tools.build:gradle:3.1.0")
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
