package com.aau.rpg.ui.connection

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.aau.rpg.R
import com.aau.rpg.ui.util.toast
import kotlinx.android.synthetic.main.fragment_connection.button_connect
import kotlinx.android.synthetic.main.fragment_connection.device_info
import kotlinx.android.synthetic.main.fragment_connection.device_mac
import kotlinx.android.synthetic.main.fragment_connection.device_name
import kotlinx.android.synthetic.main.fragment_connection.progress_connecting
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ConnectionFragment : Fragment() {

    private val bluetoothViewModel by sharedViewModel<BluetoothViewModel>()

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, saved: Bundle?): View =
        inflater.inflate(R.layout.fragment_connection, group, false)

    override fun onViewCreated(view: View, saved: Bundle?) {
        bluetoothViewModel.connectionData.observe(this, connectionDataObserver())
        bluetoothViewModel.connecting.observe(this, connectingObserver())
        bluetoothViewModel.connected.observe(this, connectedObserver())
        bluetoothViewModel.enabled.observe(this, enabledObserver())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (REQUEST_ENABLE_BLUETOOTH == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                bluetoothViewModel.connect()
            } else {
                val message = if (VERSION.SDK_INT > VERSION_CODES.LOLLIPOP_MR1) {
                    getString(R.string.msg_bluetooth_location_not_enabled)
                } else {
                    getString(R.string.msg_bluetooth_not_enabled)
                }

                toast(message)
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

            device_info.visibility = View.VISIBLE
        } else {
            button_connect.text = getString(R.string.button_connect)
            button_connect.setOnClickListener(connectListener())

            device_info.visibility = View.GONE
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

    private fun disconnectListener() = View.OnClickListener {
        bluetoothViewModel.disconnect()
    }

    private fun connectListener() = View.OnClickListener {
        bluetoothViewModel.connect()
    }

    private fun requestListener() = View.OnClickListener {
        if (VERSION.SDK_INT > VERSION_CODES.LOLLIPOP_MR1
            && checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) == PERMISSION_DENIED
        ) {
            requestPermissions(
                requireActivity(),
                arrayOf(ACCESS_FINE_LOCATION),
                REQUEST_ENABLE_LOCATION
            )
        } else {
            startActivityForResult(
                Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),
                REQUEST_ENABLE_BLUETOOTH
            )
        }
    }
}

private const val REQUEST_ENABLE_BLUETOOTH = 1
private const val REQUEST_ENABLE_LOCATION = 2
