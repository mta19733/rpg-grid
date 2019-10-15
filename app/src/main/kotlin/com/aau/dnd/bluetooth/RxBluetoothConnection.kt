package com.aau.dnd.bluetooth

import com.polidea.rxandroidble2.RxBleConnection
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.UUID

class RxBluetoothConnection(
    private val stringCharacteristicId: UUID,
    private val writeReties: Int,
    private val connection: RxBleConnection
) : BluetoothConnection {

    override fun send(data: String): Single<String> = connection
        .writeCharacteristic(stringCharacteristicId, data.toByteArray())
        .observeOn(AndroidSchedulers.mainThread())
        .retry(writeReties.toLong())
        .map { response ->
            String(response)
        }
}
