package com.aau.dnd.ui

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aau.dnd.R
import com.aau.dnd.core.bluetooth.BluetoothConnection
import com.aau.dnd.core.bluetooth.BluetoothService
import com.aau.dnd.core.bluetooth.BluetoothState
import com.aau.dnd.core.bluetooth.NullBluetoothConnection
import com.aau.dnd.util.toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_connect.view.button_connect
import kotlinx.android.synthetic.main.fragment_connect.view.button_send
import org.koin.android.ext.android.inject

class ConnectFragment : Fragment() {

    private val bluetoothService by inject<BluetoothService>()

    private var connection: BluetoothConnection = NullBluetoothConnection

    // Everything related to connection can be disposed at any time.
    private val connectionDisposables = CompositeDisposable()

    // State should be observed all the time, so it is separate.
    private var stateDisposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(
            R.layout.fragment_connect,
            container,
            false
        )

        observeState()

        view.button_connect.setOnClickListener { handleConnect() }
        view.button_connect.isEnabled = BluetoothState.ON == bluetoothService.state

        view.button_send.setOnClickListener { handleSend() }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()

        connectionDisposables.clear()
        stateDisposable?.dispose()
    }

    private fun cleanupBluetoothBadState() {
        connectionDisposables.clear()
        connection = NullBluetoothConnection

        view?.button_connect?.isEnabled = false
        view?.button_send?.isEnabled = false
    }

    private fun requestBluetooth() {
        startActivityForResult(
            Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),
            REQUEST_ENABLE_BLUETOOTH
        )
    }

    private fun handleBluetoothState(state: BluetoothState) {
        when (state) {
            BluetoothState.UNAVAILABLE -> {
                cleanupBluetoothBadState()
                toast(getString(R.string.bluetooth_unavailable))
            }
            BluetoothState.OFF -> {
                cleanupBluetoothBadState()
                toast(getString(R.string.bluetooth_off))

                requestBluetooth()
            }
            BluetoothState.ON -> {
                view?.button_connect?.isEnabled = true
            }
        }
    }

    private fun observeState() {
        stateDisposable = bluetoothService
            .observeState()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                ::handleBluetoothState,
                ::handleError
            )
    }

    private fun handleConnection(connection: BluetoothConnection) {
        this.connection = connection

        view?.button_connect?.isEnabled = false
        view?.button_send?.isEnabled = true
    }

    private fun handleError(error: Throwable) {
        Log.e(TAG, "Unhandled error", error)
    }

    private fun handleConnect() {
        connectionDisposables += bluetoothService
            .connect()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                ::handleConnection,
                ::handleError
            )
    }

    private fun handleSendResponse(response: String) = toast(response)

    private fun handleSend() {
        connectionDisposables += connection
            .send("Hello World")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                ::handleSendResponse,
                ::handleError
            )
    }
}

private const val REQUEST_ENABLE_BLUETOOTH = 1
private val TAG: String = ConnectFragment::class.java.simpleName
