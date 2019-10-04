package com.aau.dnd.util

import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.tabs.TabLayout

class ColoredTabListener(
    private val tabUnselectedColorTint: Int,
    private val tabSelectedColorTint: Int
) : TabLayout.OnTabSelectedListener {

    override fun onTabReselected(tab: TabLayout.Tab) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
        tab.setIconTint(tabUnselectedColorTint)
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        tab.setIconTint(tabSelectedColorTint)
    }

    private fun TabLayout.Tab.setIconTint(tint: Int) {
        icon
            ?.let(DrawableCompat::wrap)
            ?.also { icon ->
                DrawableCompat.setTint(icon, tint)
            }
    }
}
