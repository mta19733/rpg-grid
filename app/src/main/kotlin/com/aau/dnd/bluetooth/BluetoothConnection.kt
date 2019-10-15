package com.aau.dnd.bluetooth

import io.reactivex.Single

interface BluetoothConnection {

    /**
     * Send raw string data and receive a string.
     */
    fun send(data: String): Single<String>
}
