package com.aau.dnd.core.bluetooth

import io.reactivex.Single

object NullBluetoothConnection : BluetoothConnection {

    override val name = ""

    override val mac = ""

    override fun send(data: String): Single<String> = Single.just("")
}
