package com.aau.rpg.core.bluetooth

import com.polidea.rxandroidble2.RxBleConnection
import com.polidea.rxandroidble2.RxBleConnection.RxBleConnectionState
import com.polidea.rxandroidble2.RxBleDevice
import io.reactivex.Observable
import io.reactivex.Single
import java.util.UUID

class RxBluetoothConnection(
    private val characteristicId: UUID,
    private val writeReties: Int,
    private val pin: String,
    private val connection: RxBleConnection,
    private val device: RxBleDevice
) : BluetoothConnection {

    override val name: String
        get() = device.name ?: device.macAddress

    override val mac: String
        get() = device.macAddress

    override fun observeState(): Observable<ConnectionState> = device
        .observeConnectionStateChanges()
        .startWith(device.connectionState)
        .map(::mapState)

    override fun send(data: String): Single<String> = connection
        .writeCharacteristic(characteristicId, createPayload(data))
        .retry(writeReties.toLong())
        .map { sent -> String(sent) }

    private fun mapState(state: RxBleConnectionState): ConnectionState {
        return when (state) {
            RxBleConnectionState.CONNECTED -> ConnectionState.CONNECTED
            RxBleConnectionState.CONNECTING,
            RxBleConnectionState.DISCONNECTING,
            RxBleConnectionState.DISCONNECTED -> ConnectionState.DISCONNECTED
        }
    }

    private fun createPayload(data: String): ByteArray =
        "$pin:$data".toByteArray()
}
