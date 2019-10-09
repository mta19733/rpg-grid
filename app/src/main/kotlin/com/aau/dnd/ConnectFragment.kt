package com.aau.dnd

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aau.dnd.bluetooth.BluetoothService
import com.aau.dnd.device.Device
import com.aau.dnd.device.DeviceRecyclerViewAdapter
import com.aau.dnd.util.ColoredSwipeRefreshLayout
import com.aau.dnd.util.ctx
import com.aau.dnd.util.toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_connect.refresh_devices
import kotlinx.android.synthetic.main.fragment_connect.view.list_devices
import kotlinx.android.synthetic.main.fragment_connect.view.refresh_devices

class ConnectFragment : Fragment() {

    private lateinit var bluetoothService: BluetoothService
    private lateinit var deviceAdapter: DeviceRecyclerViewAdapter

    private var scanDevices: Disposable? = null
    private var connection: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bluetoothService = ctx.bluetoothService

        deviceAdapter = DeviceRecyclerViewAdapter(
            devices = bluetoothService.devices,
            onClick = ::handleConnect,
            context = requireContext()
        )

        return inflate(inflater, container)
            .apply(::setupDeviceList)
    }

    override fun onDestroy() {
        super.onDestroy()

        scanDevices?.dispose()
        connection?.dispose()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (REQUEST_ENABLE_BLUETOOTH == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                refreshDevices(refresh_devices)
            } else {
                refresh_devices.isRefreshing = false
                toast("Bluetooth must be enabled to scan devices")
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
        if (bluetoothService.enabled) {
            connection = bluetoothService
                .connect(device)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ connection ->
                    toast("Connected to device $device, via $connection")
                }, { error ->
                    Log.e(TAG, "Could not connect to device $device", error)
                })

        } else {
            requestBluetooth()
        }
    }

    private fun refreshDevices(refresh: ColoredSwipeRefreshLayout) {
        deviceAdapter.clearDevices()

        scanDevices?.dispose()
        scanDevices = bluetoothService
            .scanDevices()
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                refresh.isRefreshing = false
            }
            .subscribe({ device ->
                deviceAdapter.addDevice(device)
            }, { error ->
                Log.e(TAG, "Could not scan devices", error)
            })
    }

    private fun handleRefresh() {
        if (bluetoothService.enabled) {
            refreshDevices(refresh_devices)
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
private val TAG: String = ConnectFragment::class.java.simpleName
