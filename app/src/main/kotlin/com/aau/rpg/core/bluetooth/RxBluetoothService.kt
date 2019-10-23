package com.aau.rpg.core.bluetooth

import android.os.ParcelUuid
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.RxBleClient.State
import com.polidea.rxandroidble2.RxBleDevice
import com.polidea.rxandroidble2.scan.ScanResult
import com.polidea.rxandroidble2.scan.ScanSettings
import io.reactivex.Observable
import java.util.UUID
import java.util.concurrent.TimeUnit

class RxBluetoothService(
    serviceId: UUID,
    private val connectDelayMillis: Long,
    private val scanTimeoutMillis: Long,
    private val connectRetries: Int,
    private val writeReties: Int,
    private val pin: String,
    private val characteristicId: UUID,
    private val client: RxBleClient
) : BluetoothService {

    private val parcelServiceId = ParcelUuid(serviceId)

    override fun observeState(): Observable<BluetoothState> = client
        .observeStateChanges()
        .startWith(client.state)
        .map(::mapState)

    override fun connect(): Observable<BluetoothConnection> = client
        .scanBleDevices(scanSettings())
        .take(scanTimeoutMillis, TimeUnit.MILLISECONDS)
        .filter(::filter)
        .map(ScanResult::getBleDevice)
        .take(1)
        .flatMap(::connect)

    private fun mapState(state: State) = when (state) {
        State.BLUETOOTH_NOT_AVAILABLE,
        State.LOCATION_PERMISSION_NOT_GRANTED,
        State.LOCATION_SERVICES_NOT_ENABLED,
        State.BLUETOOTH_NOT_ENABLED -> BluetoothState.OFF
        State.READY -> BluetoothState.ON
    }

    private fun scanSettings() = ScanSettings
        .Builder()
        .setShouldCheckLocationServicesState(false)
        .build()

    private fun filter(result: ScanResult): Boolean =
        result.scanRecord.serviceUuids?.contains(parcelServiceId) ?: false

    private fun connect(device: RxBleDevice) = device
        .establishConnection(false)
        .delay(connectDelayMillis, TimeUnit.MILLISECONDS)
        .retry(connectRetries.toLong())
        .map { connection ->
            RxBluetoothConnection(
                characteristicId = characteristicId,
                writeReties = writeReties,
                connection = connection,
                device = device,
                pin = pin
            )
        }
}
