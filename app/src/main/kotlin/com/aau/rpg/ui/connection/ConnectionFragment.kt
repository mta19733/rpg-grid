package com.aau.rpg.ui.connection

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.aau.rpg.R
import com.aau.rpg.ui.grid.Grid
import com.aau.rpg.util.toast
import kotlinx.android.synthetic.main.fragment_connection.button_connect
import kotlinx.android.synthetic.main.fragment_connection.button_send
import kotlinx.android.synthetic.main.fragment_connection.device_mac
import kotlinx.android.synthetic.main.fragment_connection.device_name
import kotlinx.android.synthetic.main.fragment_connection.progress_connecting
import kotlinx.android.synthetic.main.fragment_connection.view.button_send
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ConnectionFragment : Fragment() {

    private val bluetooth by sharedViewModel<BluetoothViewModel>()

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

        bluetooth.connectionData.observe(this, connectionDataObserver())
        bluetooth.connecting.observe(this, connectingObserver())
        bluetooth.connected.observe(this, connectedObserver())
        bluetooth.enabled.observe(this, enabledObserver())

        view.button_send.setOnClickListener(sendListener())

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (REQUEST_ENABLE_BLUETOOTH == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                bluetooth.connect()
            } else {
                toast(getString(R.string.msg_bluetooth_not_enabled))
            }
        }
    }

    private fun connectionDataObserver() = Observer<ConnectionData> { data ->
        device_name.text = data.name
        device_mac.text = data.mac
    }

    private fun connectedObserver() = Observer<Boolean> { connected ->
        if (connected) {
            button_connect.text = getString(R.string.button_disconnect)
            button_connect.setOnClickListener(disconnectListener())

            button_send.isEnabled = true
        } else {
            button_connect.text = getString(R.string.button_connect)
            button_connect.setOnClickListener(connectListener())

            button_send.isEnabled = false
        }
    }

    private fun enabledObserver() = Observer<Boolean> { enabled ->
        if (enabled == false) {
            button_connect.setOnClickListener(requestListener())
        }
    }

    private fun connectingObserver() = Observer<Boolean> { connecting ->
        button_connect.isEnabled = !connecting

        progress_connecting.visibility = if (connecting) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun sendListener() = View.OnClickListener {
        val testGrid = Grid(
            tiles = listOf(
                listOf(true, false, false),
                listOf(false, true, false),
                listOf(false, false, true)
            )
        )

        bluetooth.send(testGrid)
    }

    private fun disconnectListener() = View.OnClickListener {
        bluetooth.disconnect()
    }

    private fun connectListener() = View.OnClickListener {
        bluetooth.connect()
    }

    private fun requestListener() = View.OnClickListener {
        startActivityForResult(
            Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),
            REQUEST_ENABLE_BLUETOOTH
        )
    }
}

private const val REQUEST_ENABLE_BLUETOOTH = 1
