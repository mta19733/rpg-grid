package com.aau.dnd

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aau.dnd.device.Device
import com.aau.dnd.device.DeviceRecyclerViewAdapter
import com.aau.dnd.util.generateDelay
import com.aau.dnd.util.generateDevices
import kotlinx.android.synthetic.main.fragment_connect.refresh_devices
import kotlinx.android.synthetic.main.fragment_connect.view.list_devices
import kotlinx.android.synthetic.main.fragment_connect.view.refresh_devices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ConnectFragment : Fragment() {

    private val connectJob = Job()
    private val connectScope = CoroutineScope(Dispatchers.Main + connectJob)

    private lateinit var deviceAdapter: DeviceRecyclerViewAdapter
    private lateinit var bluetooth: BluetoothAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        deviceAdapter = DeviceRecyclerViewAdapter(
            onClick = ::handleConnect,
            context = requireContext()
        )

        bluetooth = BluetoothAdapter.getDefaultAdapter()

        return inflate(inflater, container)
            .apply(::setupDeviceList)
    }

    override fun onDestroy() {
        super.onDestroy()
        connectJob.cancel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (REQUEST_ENABLE_BLUETOOTH == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                refreshDevices()
            } else {
                refresh_devices.isRefreshing = false

                val text = "Bluetooth must be enabled to scan devices"

                Toast
                    .makeText(context, text, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun inflate(inflater: LayoutInflater, container: ViewGroup?) = inflater.inflate(
        R.layout.fragment_connect,
        container,
        false
    )

    private fun requestBluetooth() {
        startActivityForResult(
            Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),
            REQUEST_ENABLE_BLUETOOTH
        )
    }

    private fun handleConnect(device: Device) {
        if (bluetooth.isEnabled) {
            val text = "Connecting to ${device.id}, name: ${device.name}"

            Toast
                .makeText(context, text, Toast.LENGTH_LONG)
                .show()
        } else {
            requestBluetooth()
        }
    }

    private fun refreshDevices() {
        connectScope.launch {
            delay(generateDelay())
            deviceAdapter.setDevices(generateDevices())

            refresh_devices.isRefreshing = false
        }
    }

    private fun handleRefresh() {
        if (bluetooth.isEnabled) {
            refreshDevices()
        } else {
            requestBluetooth()
        }
    }

    private fun setupDeviceList(view: View) {
        view.list_devices.layoutManager = LinearLayoutManager(requireContext())
        view.list_devices.adapter = deviceAdapter

        view.refresh_devices.setOnRefreshListener {
            handleRefresh()
        }
    }
}

private const val REQUEST_ENABLE_BLUETOOTH = 1
