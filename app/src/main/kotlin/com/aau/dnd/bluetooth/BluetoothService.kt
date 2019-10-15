package com.aau.dnd.bluetooth

import io.reactivex.Observable

interface BluetoothService {

    /**
     * Track Bluetooth state.
     */
    fun observeState(): Observable<BluetoothState>

    /**
     * Establish Bluetooth connection with any available Bluetooth device.
     */
    fun connect(): Observable<BluetoothConnection>
}
