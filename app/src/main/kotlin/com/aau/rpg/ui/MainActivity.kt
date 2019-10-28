package com.aau.rpg.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.aau.rpg.R
import com.aau.rpg.core.grid.Grid
import com.aau.rpg.ui.connection.BluetoothViewModel
import com.aau.rpg.ui.connection.ConnectionFragment
import com.aau.rpg.ui.connection.Status
import com.aau.rpg.ui.grid.GridFragment
import com.aau.rpg.ui.management.ManagementFragment
import com.aau.rpg.ui.management.ManagementViewModel
import com.aau.rpg.ui.util.FragmentPager
import com.aau.rpg.ui.util.logDebug
import com.aau.rpg.ui.util.logError
import com.aau.rpg.ui.util.toast
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.tab_layout
import kotlinx.android.synthetic.main.activity_main.view_pager
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val bluetoothViewModel by viewModel<BluetoothViewModel>()
    private val settingsViewModel by viewModel<ManagementViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Connection state must be observed in root, so that it does not get cleared when
        // connection fragment cleans up.
        bluetoothViewModel.sentData.observe(this, sentDataObserver())
        bluetoothViewModel.status.observe(this, statusObserver())

        val errors = errorObserver()
        bluetoothViewModel.error.observe(this, errors)
        settingsViewModel.error.observe(this, errors)

        bluetoothViewModel.observeState()

        val fragments = listOf(
            getDrawable(R.drawable.ic_bluetooth) to ConnectionFragment(),
            getDrawable(R.drawable.ic_videogame_asset) to GridFragment(),
            getDrawable(R.drawable.ic_settings) to ManagementFragment()
        )

        setupPager(fragments)
        setupTabs(fragments)

        selectFirstTab()
    }

    private fun sentDataObserver() = Observer<String> { data ->
        logDebug("Sent data: $data")
        toast(getString(R.string.msg_sent_data))
    }

    private fun statusObserver() = Observer<Status> { status: Status ->
        val messageId = when (status) {
            Status.LOST_CONNECTION -> R.string.msg_bluetooth_device_lost_connection
            Status.NOT_CONNECTED -> R.string.msg_bluetooth_device_not_connected
            Status.NOT_FOUND -> R.string.msg_bluetooth_device_not_found
        }

        toast(getString(messageId))
    }

    private fun errorObserver() = Observer<Throwable> { error ->
        logError("Bluetooth error", error)
        toast(getString(R.string.msg_error, error?.message ?: ""))
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
        tab_layout.getTabAt(0)?.also(TabLayout.Tab::select)
    }
}
