package com.aau.dnd

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getColor
import com.aau.dnd.util.ColoredTabListener
import com.aau.dnd.util.FragmentPager
import kotlinx.android.synthetic.main.activity_main.tab_layout
import kotlinx.android.synthetic.main.activity_main.view_pager


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val fragments = listOf(
            getDrawable(R.drawable.ic_bluetooth) to ConnectFragment(),
            getDrawable(R.drawable.ic_videogame_asset) to PlayFragment(),
            getDrawable(R.drawable.ic_settings) to SettingsFragment()
        )

        // Setup pager, swiping left and right.
        val pager = FragmentPager(supportFragmentManager)

        fragments.forEach { (_, fragment) ->
            pager += fragment
        }

        view_pager.adapter = pager

        // Setup tabs.
        tab_layout.setupWithViewPager(view_pager)

        fragments.forEachIndexed { idx, (icon, _) ->
            tab_layout.getTabAt(idx)?.icon = icon
        }

        // Setup tab coloring.
        val tabUnselected = getColor(applicationContext, R.color.color_icon_unselected)
        val tabSelected = getColor(applicationContext, R.color.color_icon_selected)

        tab_layout.addOnTabSelectedListener(
            ColoredTabListener(
                tabUnselectedColorTint = tabUnselected,
                tabSelectedColorTint = tabSelected
            )
        )
    }
}
