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

## Links
* [StackOverflow BLE tips](https://stackoverflow.com/questions/17870189/android-4-3-bluetooth-low-energy-unstable).
* [Reddit BLE tips](https://www.reddit.com/r/androiddev/comments/4ofnbp/bluetooth_ble_development_is_miserable_on_android).
* [BLE example](https://github.com/Polidea/RxAndroidBle/blob/master/sample/src/main/java/com/polidea/rxandroidble2/sample/example2_connection/ConnectionExampleActivity.java).
