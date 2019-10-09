package com.aau.dnd.core

import com.aau.dnd.bluetooth.BluetoothService

class DndGrid : android.app.Application(), DndGridContext {

    override lateinit var bluetoothService: BluetoothService
        private set

    override fun onCreate() {
        super.onCreate()

        bluetoothService = BluetoothService()
    }
}
