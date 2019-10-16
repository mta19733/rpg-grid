package com.aau.rpg.core.bluetooth

import io.reactivex.Single

object NullBluetoothConnection : BluetoothConnection {

    override val name = ""

    override val mac = ""

    override fun send(data: String): Single<String> = Single.just("")
}
