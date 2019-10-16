package com.aau.rpg.core.bluetooth

import com.polidea.rxandroidble2.RxBleConnection
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.UUID

class RxBluetoothConnection(
    override val name: String,
    override val mac: String,
    private val stringCharacteristicId: UUID,
    private val writeReties: Int,
    private val connection: RxBleConnection
) : BluetoothConnection {

    override fun send(data: String): Single<String> = connection
        .writeCharacteristic(stringCharacteristicId, data.toByteArray())
        .subscribeOn(Schedulers.io())
        .retry(writeReties.toLong())
        .map { response ->
            String(response)
        }
}
