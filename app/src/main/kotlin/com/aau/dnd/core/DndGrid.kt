package com.aau.dnd.core

import android.bluetooth.BluetoothAdapter
import com.aau.dnd.R
import com.aau.dnd.bluetooth.BluetoothService
import com.polidea.rxandroidble2.RxBleClient

class DndGrid : android.app.Application(), DndGridContext {

    override lateinit var bluetoothService: BluetoothService
        private set

    override fun onCreate() {
        super.onCreate()

        bluetoothService = BluetoothService(
            scanDurationMillis = resources.getInteger(R.integer.scan_duration_millis).toLong(),
            unknownDeviceName = resources.getString(R.string.device_unknown_name),
            adapter = BluetoothAdapter.getDefaultAdapter(),
            client = RxBleClient.create(baseContext)
        )
    }
}
