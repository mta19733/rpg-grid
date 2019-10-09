package com.aau.dnd.bluetooth

import android.bluetooth.BluetoothAdapter
import com.aau.dnd.device.Device
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.RxBleClient.State
import com.polidea.rxandroidble2.scan.ScanResult
import com.polidea.rxandroidble2.scan.ScanSettings
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class BluetoothService(
    private val scanDurationMillis: Long,
    private val unknownDeviceName: String,
    private val adapter: BluetoothAdapter,
    private val client: RxBleClient
) {

    val devices: List<Device>
        get() = scannedDevices.toList()

    val enabled: Boolean
        get() = adapter.isEnabled

    private val scannedDeviceMacs = mutableSetOf<String>()
    private val scannedDevices = mutableListOf<Device>()

    fun scanDevices(): Observable<Device> = if (State.READY == client.state) {
        clearDevices()
        startScan()
    } else {
        Observable.error(
            Exception("Bluetooth is not ready")
        )
    }

    fun connect(device: Device): Observable<BluetoothConnection> = client
        .getBleDevice(device.mac)
        .establishConnection(false)
        .subscribeOn(Schedulers.io())
        .map(::BluetoothConnection)

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

    private fun clearDevices() {
        scannedDeviceMacs.clear()
        scannedDevices.clear()
    }

    private fun startScan() = client
        .scanBleDevices(scanSettings())
        .subscribeOn(Schedulers.io())
        .map(::mapScanResult)
        .takeUntil(
            Observable.timer(
                scanDurationMillis,
                TimeUnit.MILLISECONDS
            )
        )
        .filter { device ->
            !scannedDeviceMacs.contains(device.mac)
        }
        .doOnNext { device ->
            scannedDeviceMacs.add(device.mac)
            scannedDevices.add(device)
        }
}
