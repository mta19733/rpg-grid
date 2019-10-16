package com.aau.dnd.core.bluetooth

import io.reactivex.Single

interface BluetoothConnection {

    /**
     * Send raw string data and receive a string.
     */
    fun send(data: String): Single<String>
}
