package com.aau.dnd.bluetooth

import android.os.ParcelUuid
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.RxBleClient.State
import com.polidea.rxandroidble2.RxBleDevice
import com.polidea.rxandroidble2.scan.ScanResult
import com.polidea.rxandroidble2.scan.ScanSettings
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.UUID
import java.util.concurrent.TimeUnit

class RxBluetoothService(
    serviceId: UUID,
    private val connectDelayMillis: Long,
    private val scanTimeoutMillis: Long,
    private val connectRetries: Int,
    private val writeReties: Int,
    private val stringCharacteristicId: UUID,
    private val client: RxBleClient
) : BluetoothService {

    private val parcelServiceId = ParcelUuid(serviceId)

    override fun observeState(): Observable<BluetoothState> = client
        .observeStateChanges()
        .startWith(client.state)
        .subscribeOn(AndroidSchedulers.mainThread())
        .map(::mapState)

    override fun connect(): Observable<BluetoothConnection> = client
        .scanBleDevices(scanSettings())
        .subscribeOn(AndroidSchedulers.mainThread())
        .take(scanTimeoutMillis, TimeUnit.MILLISECONDS)
        .filter(::filter)
        .map(ScanResult::getBleDevice)
        .flatMap(::connect)

    private fun mapState(state: State) = when (state) {
        State.BLUETOOTH_NOT_AVAILABLE,
        State.LOCATION_PERMISSION_NOT_GRANTED,
        State.LOCATION_SERVICES_NOT_ENABLED -> BluetoothState.UNAVAILABLE
        State.BLUETOOTH_NOT_ENABLED -> BluetoothState.OFF
        State.READY -> BluetoothState.ON
    }

    private fun scanSettings() = ScanSettings
        .Builder()
        .build()

    private fun filter(result: ScanResult) =
        result.scanRecord.serviceUuids?.contains(parcelServiceId) ?: false

    private fun connect(device: RxBleDevice) = device
        .establishConnection(false)
        .delay(connectDelayMillis, TimeUnit.MILLISECONDS)
        .retry(connectRetries.toLong())
        .map { connection ->
            RxBluetoothConnection(
                stringCharacteristicId = stringCharacteristicId,
                writeReties = writeReties,
                connection = connection
            )
        }
}
