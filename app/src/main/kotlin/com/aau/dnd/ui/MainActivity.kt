package com.aau.dnd.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.aau.dnd.R
import com.aau.dnd.core.bluetooth.BluetoothConnection
import com.aau.dnd.core.bluetooth.BluetoothService
import com.aau.dnd.core.bluetooth.BluetoothState
import com.aau.dnd.core.bluetooth.NullBluetoothConnection
import com.aau.dnd.util.FragmentPager
import com.google.android.material.tabs.TabLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_main.tab_layout
import kotlinx.android.synthetic.main.activity_main.view_pager
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    var bluetoothConnection: BluetoothConnection = NullBluetoothConnection

    private val bluetoothService by inject<BluetoothService>()
    private val connectFragment = ConnectionFragment()

    private val connectionDisposables = CompositeDisposable()
    private var stateDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        observeBluetoothState()

        val fragments = listOf(
            getDrawable(R.drawable.ic_bluetooth) to connectFragment,
            getDrawable(R.drawable.ic_videogame_asset) to PlayFragment(),
            getDrawable(R.drawable.ic_settings) to SettingsFragment()
        )

        setupPager(fragments)
        setupTabs(fragments)
    }

    override fun onResume() {
        super.onResume()
        selectFirstTab()
    }

    override fun onDestroy() {
        super.onDestroy()

        connectionDisposables.clear()
        stateDisposable?.dispose()
    }

    /**
     * Disconnect and cleanup Bluetooth connection from device.
     */
    fun disconnectBluetooth() {
        connectionDisposables.clear()
        bluetoothConnection = NullBluetoothConnection
        connectFragment.handleBluetoothDisconnect()
    }

    /**
     * Establish Bluetooth connection to device.
     */
    fun connectBluetooth() {
        connectionDisposables += bluetoothService
            .connect()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                ::handleBluetoothConnection,
                ::handleError
            )
    }

    private fun handleBluetoothConnection(connection: BluetoothConnection) {
        bluetoothConnection = connection
        connectFragment.handleBluetoothConnect(connection)
    }

    private fun handleBluetoothStateChange(state: BluetoothState) {
        if (BluetoothState.UNAVAILABLE == state) {
            connectFragment.handleBluetoothDisconnect()
        } else if (BluetoothState.OFF == state) {
            connectFragment.handleBluetoothDisconnect()
        }
    }

    private fun handleError(error: Throwable) {
        Log.e(MainActivity::class.java.simpleName, "Unhandled error", error)
    }

    private fun observeBluetoothState() {
        stateDisposable = bluetoothService
            .observeState()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                ::handleBluetoothStateChange,
                ::handleError
            )
    }

    private fun setupPager(fragments: List<Pair<*, Fragment>>) {
        val pager = FragmentPager(supportFragmentManager)

        fragments.forEach { (_, fragment) ->
            pager += fragment
        }

        view_pager.adapter = pager
    }

    private fun setupTabs(fragments: List<Pair<Drawable?, Fragment>>) {
        tab_layout.setupWithViewPager(view_pager)

        fragments
            .mapNotNull { (icon, _) -> icon }
            .mapIndexedNotNull { idx, icon ->
                tab_layout.getTabAt(idx)?.to(icon)
            }
            .forEach { (tab, icon) ->
                tab.icon = icon
            }
    }

    private fun selectFirstTab() {
        tab_layout
            .getTabAt(0)
            ?.also(TabLayout.Tab::select)
    }
}
