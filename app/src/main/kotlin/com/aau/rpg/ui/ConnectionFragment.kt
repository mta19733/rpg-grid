package com.aau.rpg.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aau.rpg.R
import com.aau.rpg.core.bluetooth.BluetoothConnection
import com.aau.rpg.core.bluetooth.NullBluetoothConnection
import com.aau.rpg.util.toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_connection.view.button_connect
import kotlinx.android.synthetic.main.fragment_connection.view.button_send
import kotlinx.android.synthetic.main.fragment_connection.view.device_mac
import kotlinx.android.synthetic.main.fragment_connection.view.device_name
import kotlinx.android.synthetic.main.fragment_connection.view.progress_connecting

class ConnectionFragment : Fragment() {

    private val sendDisposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(
            R.layout.fragment_connection,
            container,
            false
        )

        val mainActivity = (activity as MainActivity)
        val bluetoothConnection = mainActivity.bluetoothConnection

        view.device_name.text = bluetoothConnection.name
        view.device_mac.text = bluetoothConnection.mac

        view.button_connect.text =
            if (NullBluetoothConnection == bluetoothConnection) {
                getString(R.string.button_connect)
            } else {
                getString(R.string.button_disconnect)
            }

        view.button_connect.setOnClickListener { connect() }

        view.button_send.setOnClickListener { send() }
        view.button_send.isEnabled = NullBluetoothConnection != bluetoothConnection

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        sendDisposables.clear()
    }

    /**
     * Handle Bluetooth connection drop.
     */
    fun handleBluetoothDisconnect() {
        sendDisposables.clear()

        view?.apply {
            button_connect.text = getString(R.string.button_connect)
            button_connect.isEnabled = true
            button_send.isEnabled = false

            progress_connecting.visibility = View.GONE

            device_name.text = ""
            device_mac.text = ""
        }
    }

    /**
     * Handle Bluetooth connection establishment.
     */
    fun handleBluetoothConnect(connection: BluetoothConnection) {
        view?.apply {
            button_connect.text = getString(R.string.button_disconnect)
            button_connect.isEnabled = true
            button_send.isEnabled = true

            progress_connecting.visibility = View.GONE

            device_name.text = connection.name
            device_mac.text = connection.mac
        }
    }

    private fun connect() {
        view?.apply {
            progress_connecting.visibility = View.VISIBLE
            button_connect.isEnabled = false
        }

        val mainActivity = activity as MainActivity
        if (NullBluetoothConnection == mainActivity.bluetoothConnection) {
            mainActivity.connectBluetooth()
        } else {
            mainActivity.disconnectBluetooth()
        }
    }

    private fun handleSendResponse(response: String) {
        toast(response)
    }

    private fun handleError(error: Throwable) {
        Log.e(ConnectionFragment::class.java.simpleName, "Unhandled error", error)
    }

    private fun send() {
        sendDisposables += (activity as MainActivity)
            .bluetoothConnection
            .send("Hello World")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                ::handleSendResponse,
                ::handleError
            )
    }
}
