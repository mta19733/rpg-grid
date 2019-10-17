package com.aau.rpg.core.bluetooth

import com.aau.rpg.core.grid.Grid
import io.reactivex.Observable
import io.reactivex.Single

interface BluetoothConnection {

    /**
     * Current connection state.
     */
    val state: ConnectionState

    /**
     * Connected devices name.
     */
    val name: String

    /**
     * Connected devices mac address.
     */
    val mac: String

    /**
     * Observe connection state.
     */
    fun observeState(): Observable<ConnectionState>

    /**
     * Send grid information.
     */
    fun send(grid: Grid): Single<ByteArray>
}
