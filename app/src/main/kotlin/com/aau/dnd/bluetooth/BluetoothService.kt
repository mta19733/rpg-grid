package com.aau.dnd.bluetooth

import android.bluetooth.BluetoothAdapter
import com.aau.dnd.device.Device
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.RxBleClient.State
import com.polidea.rxandroidble2.scan.ScanResult
import com.polidea.rxandroidble2.scan.ScanSettings
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class BluetoothService(
    private val scanDurationMillis: Long,
    private val unknownDeviceName: String,
    private val adapter: BluetoothAdapter,
    private val client: RxBleClient
) {

    val enabled: Boolean
        get() = adapter.isEnabled

    fun scan(): Observable<Device> = if (client.state == State.READY) {
        scanInternal()
    } else {
        Observable.error(
            BluetoothException("Bluetooth is not ready")
        )
    }

    private fun mapScanResult(result: ScanResult) = result
        .bleDevice
        .run {
            Device(
                name = name ?: unknownDeviceName,
                mac = macAddress
            )
        }

    private fun scanSettings() = ScanSettings
        .Builder()
        .build()

    private fun scanInternal() = client
        .scanBleDevices(scanSettings())
        .map(::mapScanResult)
        .takeUntil(
            Observable.timer(
                scanDurationMillis,
                TimeUnit.MILLISECONDS
            )
        )
}
