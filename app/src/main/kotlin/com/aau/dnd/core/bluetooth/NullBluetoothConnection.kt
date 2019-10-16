package com.aau.dnd.core.bluetooth

import io.reactivex.Single

object NullBluetoothConnection : BluetoothConnection {

    override fun send(data: String): Single<String>  = Single.error(
        Throwable("Null connection can't send data")
    )
}
