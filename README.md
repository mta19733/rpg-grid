# D&D Grid [![CircleCI](https://circleci.com/gh/Edvinas01/dnd-grid.svg?style=svg&circle-token=6536e12c520e4c7231f7a4fd4e37d460a21ba614)](https://circleci.com/gh/Edvinas01/dnd-grid)
Android application to control electrochromic display based D&D Grid.

## Prerequisites
To build this project, the following tools are required:
* [JDK] 8 or grater.
* [Intellij IDEA] with [Android Support] (installed by default) plugin or 
[Android Studio].
* An Android device with enabled dev mode.

[JDK]: https://openjdk.java.net/install
[Intellij IDEA]: https://www.jetbrains.com/idea
[Android Support]: https://plugins.jetbrains.com/plugin/1792-android-support
[Android Studio]: https://developer.android.com/studio

## Running
1. Open up your IDE.
2. Connect your Android device via USB.
3. Start the `debug-app` run configuration.

## Building
Navigate to root project directory and run the following:
```bash
./gradlew build
```

After this is done, navigate to `app/build/outputs/apk/release` and grab the 
APK files.
