package com.aau.dnd.util

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.aau.dnd.R

class ColoredSwipeRefreshLayout(
    context: Context,
    attrs: AttributeSet?
) : SwipeRefreshLayout(context, attrs) {

    init {
        setColorSchemeColors(
            ContextCompat.getColor(context, R.color.color_accent),
            ContextCompat.getColor(context, R.color.color_primary)
        )
    }
}
