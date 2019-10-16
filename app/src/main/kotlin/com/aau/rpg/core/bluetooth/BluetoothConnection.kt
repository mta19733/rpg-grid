package com.aau.rpg.core.bluetooth

import io.reactivex.Single

interface BluetoothConnection {

    /**
     * Connected devices name.
     */
    val name: String

    /**
     * Connected devices mac address.
     */
    val mac: String

    /**
     * Send raw string data and receive a string.
     */
    fun send(data: String): Single<String>
}
