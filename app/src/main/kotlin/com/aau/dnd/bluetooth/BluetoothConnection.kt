package com.aau.dnd.bluetooth

import com.polidea.rxandroidble2.RxBleConnection
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.UUID

class BluetoothConnection(private val connection: RxBleConnection) {

    fun read(): Single<ByteArray> = connection
        .readCharacteristic(UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8"))
        .subscribeOn(Schedulers.io())

    fun send(): Single<ByteArray> = connection
        .writeCharacteristic(
            UUID.fromString("b39e3e2a-f44c-4819-b25b-04d456889ea1"),
            "Hey from Android".toByteArray()
        )
        .subscribeOn(Schedulers.io())
}
