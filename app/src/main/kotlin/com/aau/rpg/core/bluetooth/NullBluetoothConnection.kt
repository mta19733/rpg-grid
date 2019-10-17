package com.aau.rpg.core.bluetooth

import com.aau.rpg.core.grid.Grid
import io.reactivex.Observable
import io.reactivex.Single

object NullBluetoothConnection : BluetoothConnection {

    override val state = ConnectionState.DISCONNECTED

    override val name = ""

    override val mac = ""

    override fun observeState(): Observable<ConnectionState> = Observable.empty()

    override fun send(grid: Grid): Single<ByteArray> = Single.just(ByteArray(0))
}
