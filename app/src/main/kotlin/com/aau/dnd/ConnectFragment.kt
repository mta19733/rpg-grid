package com.aau.dnd

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aau.dnd.bluetooth.BluetoothConnection
import com.aau.dnd.bluetooth.BluetoothState
import com.aau.dnd.bluetooth.RxBluetoothService
import com.aau.dnd.util.ctx
import com.aau.dnd.util.toast
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_connect.view.button_connect
import kotlinx.android.synthetic.main.fragment_connect.view.button_send

class ConnectFragment : Fragment() {

    private lateinit var bluetoothService: RxBluetoothService

    private var connection: BluetoothConnection? = null

    // Everything related to connection can be disposed at any time.
    private val connectionDisposables = CompositeDisposable()

    // State should be observed all the time, so it is separate.
    private var stateDisposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bluetoothService = ctx.bluetoothService

        val view = inflater.inflate(
            R.layout.fragment_connect,
            container,
            false
        )

        observeState()

        view.button_connect.setOnClickListener { handleConnect() }
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
        connection = null
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
            .subscribe(
                ::handleBluetoothState,
                ::handleError
            )
    }

    private fun handleConnection(connection: BluetoothConnection) {
        this.connection = connection
        view?.button_send?.isEnabled = true
    }

    private fun handleError(error: Throwable) {
        Log.e(TAG, "Unhandled error", error)
    }

    private fun handleConnect() {
        bluetoothService
            .connect()
            .subscribe(
                ::handleConnection,
                ::handleError
            )
            ?.also { disposable ->
                connectionDisposables.add(disposable)
            }
    }

    private fun handleSend() {
        connection
            ?.send("Hello World")
            ?.subscribe(
                { response -> toast(response) },
                ::handleError
            )
            ?.also { disposable ->
                connectionDisposables.add(disposable)
            }
    }
}

private const val REQUEST_ENABLE_BLUETOOTH = 1
private val TAG: String = ConnectFragment::class.java.simpleName
