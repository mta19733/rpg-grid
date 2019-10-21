package com.aau.rpg.core.bluetooth

import io.reactivex.Observable
import io.reactivex.Single

interface BluetoothConnection {

    /**
     * Connection name.
     */
    val name: String

    /**
     * Connection mac address.
     */
    val mac: String

    /**
     * Observe connection state.
     */
    fun observeState(): Observable<ConnectionState>

    /**
     * Send raw string information.
     */
    fun send(data: String): Single<String>
}
