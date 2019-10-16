package com.aau.dnd.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.aau.dnd.R
import com.aau.dnd.core.bluetooth.BluetoothConnection
import com.aau.dnd.core.bluetooth.BluetoothService
import com.aau.dnd.core.bluetooth.NullBluetoothConnection
import com.aau.dnd.util.FragmentPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.tab_layout
import kotlinx.android.synthetic.main.activity_main.view_pager
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private var bluetoothConnection: BluetoothConnection = NullBluetoothConnection
    private val bluetoothService by inject<BluetoothService>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val fragments = listOf(
            getDrawable(R.drawable.ic_bluetooth) to ConnectFragment(),
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
