package com.aau.rpg.presentation.connection

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

abstract class BluetoothViewModel : ViewModel() {

    /**
     * Information about current Bluetooth connection.
     */
    abstract val connectionData: LiveData<ConnectionData>

    /**
     * Flag indicating whether Bluetooth connection is **being** established.
     */
    abstract val connecting: LiveData<Boolean>

    /**
     * Flag indicating whether Bluetooth connection is established.
     */
    abstract val connected: LiveData<Boolean>

    /**
     * Flag indicating whether Bluetooth is enabled.
     */
    abstract val enabled: LiveData<Boolean>

    /**
     * Sent data strings.
     */
    abstract val sentData: LiveData<String>

    /**
     * Status notifications.
     */
    abstract val status: LiveData<Status>

    /**
     * Errors.
     */
    abstract val error: LiveData<Throwable>

    /**
     * Observe Bluetooth connection state.
     */
    abstract fun observeState()

    /**
     * Disconnect Bluetooth connection.
     */
    abstract fun disconnect()

    /**
     * Establish Bluetooth connection.
     */
    abstract fun connect()

    /**
     * Send string data via Bluetooth.
     */
    abstract fun send(data: String)
}
