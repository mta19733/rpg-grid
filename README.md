# RPG Grid [![CircleCI](https://circleci.com/gh/Edvinas01/rpg-grid.svg?style=svg&circle-token=5c03dc54e44329a22a9a97221ae0b4363951e5d8)](https://circleci.com/gh/Edvinas01/rpg-grid)
Android application to control electrochromic display based, role-playing game 
board. The hardware code can be found on [Arduino project].

## Prerequisites
To build this project, the following tools are required:
* [JDK] 8 or grater.
* [Intellij IDEA] with [Android Support] (installed by default) plugin or 
[Android Studio].
* An Android device with enabled dev mode.

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

[JDK]: https://openjdk.java.net/install
[Intellij IDEA]: https://www.jetbrains.com/idea
[Android Support]: https://plugins.jetbrains.com/plugin/1792-android-support
[Android Studio]: https://developer.android.com/studio
[Arduino project]: https://github.com/KonrolMathisen/RPG-grid-arduino-19733
