package com.aau.dnd.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class FragmentPager(fragmentManager: FragmentManager) : FragmentPagerAdapter(
    fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    private val fragments = mutableListOf<Fragment>()

    override fun getItem(position: Int) = fragments[position]

    override fun getCount(): Int = fragments.size

    operator fun plusAssign(fragment: Fragment) {
        fragments += fragment
    }
}
