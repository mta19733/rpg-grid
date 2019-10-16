package com.aau.rpg.core.bluetooth

import io.reactivex.Observable

interface BluetoothService {

    /**
     * Current Bluetooth state.
     */
    val state: BluetoothState

    /**
     * Track Bluetooth state.
     */
    fun observeState(): Observable<BluetoothState>

    /**
     * Establish Bluetooth connection with any available Bluetooth device.
     */
    fun connect(): Observable<BluetoothConnection>
}
