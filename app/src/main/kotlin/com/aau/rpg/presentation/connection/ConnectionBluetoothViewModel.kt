package com.aau.rpg.presentation.connection

import androidx.lifecycle.MutableLiveData
import com.aau.rpg.core.bluetooth.BluetoothConnection
import com.aau.rpg.core.bluetooth.BluetoothService
import com.aau.rpg.core.bluetooth.BluetoothState
import com.aau.rpg.core.bluetooth.ConnectionState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

class ConnectionBluetoothViewModel(
    private val bluetooth: BluetoothService
) : BluetoothViewModel() {

    override val connectionData = MutableLiveData<ConnectionData>()

    override val connecting = MutableLiveData<Boolean>(false)
    override val connected = MutableLiveData<Boolean>(false)
    override val enabled = MutableLiveData<Boolean>(false)

    override val sentData = MutableLiveData<String>()

    override val status = MutableLiveData<Status>()

    override val error = MutableLiveData<Throwable>()

    private val connectionDisposables = CompositeDisposable()
    private val generalDisposables = CompositeDisposable()

    private var connection: BluetoothConnection? = null

    override fun observeState() {
        generalDisposables += bluetooth
            .observeState()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                ::onBluetoothStateChange,
                ::onError
            )
    }

    override fun disconnect() {
        connectionDisposables.clear()

        connection = null

        connectionData.value = ConnectionData(
            name = "",
            mac = ""
        )

        connecting.value = false
        connected.value = false
    }

    override fun connect() {
        connecting.value = true
        connected.value = false

        connectionDisposables += bluetooth
            .connect()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(::onNotFound)
            .doFinally {
                connecting.value = false
            }
            .subscribe(
                ::onConnect,
                ::onError
            )
    }

    override fun send(data: String) {
        connection
            ?.also { connection ->
                connectionDisposables += connection
                    .send(data)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        ::onSentData,
                        ::onError
                    )
            }
            ?: onNotConnected()
    }

    override fun onCleared() {
        super.onCleared()

        disconnect()

        generalDisposables.clear()
    }

    private fun onBluetoothStateChange(state: BluetoothState) {
        when (state) {
            BluetoothState.OFF -> {
                disconnect()
                enabled.value = false
            }
            BluetoothState.ON -> {
                enabled.value = true
            }
        }
    }

    private fun onError(error: Throwable) {
        this.error.value = error
    }

    private fun onConnectionStateChange(state: ConnectionState) {
        when (state) {
            ConnectionState.DISCONNECTED -> {
                disconnect()
                status.value = Status.LOST_CONNECTION
            }
            ConnectionState.CONNECTED -> {
                connecting.value = false
                connected.value = true
            }
        }
    }

    private fun onNotFound() {
        if (connected.value == false) {
            status.value = Status.NOT_FOUND
        }
    }

    private fun onConnect(connection: BluetoothConnection) {
        connectionDisposables += connection
            .observeState()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                ::onConnectionStateChange,
                ::onError
            )

        this.connectionData.value = ConnectionData(
            name = connection.name,
            mac = connection.mac
        )

        this.connection = connection
    }

    private fun onSentData(data: String) {
        sentData.value = data
    }

    private fun onNotConnected() {
        status.value = Status.NOT_CONNECTED
    }
}
