package com.aau.rpg.core.bluetooth

import com.aau.rpg.core.grid.Grid
import com.polidea.rxandroidble2.RxBleConnection
import com.polidea.rxandroidble2.RxBleConnection.RxBleConnectionState
import com.polidea.rxandroidble2.RxBleDevice
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.UUID

class RxBluetoothConnection(
    private val characteristicId: UUID,
    private val writeReties: Int,
    private val pin: String,
    private val connection: RxBleConnection,
    private val device: RxBleDevice
) : BluetoothConnection {

    override val state: ConnectionState
        get() = mapState(device.connectionState)

    override val name: String
        get() = device.name ?: device.macAddress

    override val mac: String
        get() = device.macAddress

    override fun observeState(): Observable<ConnectionState> = device
        .observeConnectionStateChanges()
        .startWith(device.connectionState)
        .subscribeOn(Schedulers.io())
        .map(::mapState)

    override fun send(grid: Grid): Single<ByteArray> = connection
        .writeCharacteristic(characteristicId, createPayload(grid))
        .subscribeOn(Schedulers.io())
        .retry(writeReties.toLong())

    private fun mapState(state: RxBleConnectionState): ConnectionState {
        return when (state) {
            RxBleConnectionState.CONNECTED -> ConnectionState.CONNECTED
            RxBleConnectionState.CONNECTING,
            RxBleConnectionState.DISCONNECTING,
            RxBleConnectionState.DISCONNECTED -> ConnectionState.DISCONNECTED
        }
    }

    private fun createPayload(grid: Grid): ByteArray {
        val joinedIds = grid
            .data
            .flatten()
            .mapIndexedNotNull { idx, enabled ->
                if (enabled) {
                    idx.toString()
                } else {
                    null
                }
            }
            .joinToString(",")

        return "$pin:$joinedIds".toByteArray()
    }
}
