package com.aau.rpg.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.aau.rpg.R
import com.aau.rpg.ui.connection.BluetoothViewModel
import com.aau.rpg.ui.connection.ConnectionFragment
import com.aau.rpg.ui.connection.Status
import com.aau.rpg.ui.grid.GridFragment
import com.aau.rpg.util.FragmentPager
import com.aau.rpg.util.toast
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.tab_layout
import kotlinx.android.synthetic.main.activity_main.view_pager
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val connectionViewModel by viewModel<BluetoothViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Connection state must be observed in root, so that it does not get cleared when
        // connection fragment cleans up.
        connectionViewModel.observeState()
        connectionViewModel.sentData.observe(this, sentDataObserver())
        connectionViewModel.status.observe(this, statusObserver())
        connectionViewModel.error.observe(this, errorObserver())

        val fragments = listOf(
            getDrawable(R.drawable.ic_bluetooth) to ConnectionFragment(),
            getDrawable(R.drawable.ic_videogame_asset) to GridFragment(),
            getDrawable(R.drawable.ic_settings) to SettingsFragment()
        )

        setupPager(fragments)
        setupTabs(fragments)
    }

    override fun onResume() {
        super.onResume()
        selectFirstTab()
    }

    private fun sentDataObserver() = Observer<String> { data ->
        toast("${getString(R.string.msg_prefix_sent)} $data")
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
        val message = error?.message ?: ""
        toast("${getString(R.string.msg_prefix_error)} $message")

        Log.e(
            MainActivity::class.java.simpleName,
            "Bluetooth error",
            error
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
        tab_layout.getTabAt(0)?.also(TabLayout.Tab::select)
    }
}
