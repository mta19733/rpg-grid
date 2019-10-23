package com.aau.rpg.ui.grid

import android.content.Context
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.ToggleButton
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.setMargins
import com.aau.rpg.R

class GridButton(context: Context) : ToggleButton(context) {

    constructor(
        context: Context,
        checked: Boolean,
        idx: Int,
        onClick: (idx: Int, checked: Boolean) -> Unit
    ) : this(context) {

        this.background = getDrawable(context, R.drawable.selector_button_grid)
        this.isChecked = checked

        val margin = context.resources.getInteger(R.integer.button_grid_margin)
        val size = context.resources.getInteger(R.integer.button_grid_size)

        val linearLayoutParams = LinearLayout.LayoutParams(size, size)
        val gridLayoutParams = GridLayout.LayoutParams(linearLayoutParams).apply {
            setMargins(margin)
            columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        }

        layoutParams = gridLayoutParams

        textOff = ""
        textOn = ""
        text = ""

        val changeAnimation = loadAnimation(context, R.anim.bounce)
        setOnCheckedChangeListener { button, newChecked ->
            button.startAnimation(changeAnimation)
            onClick(idx, newChecked)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, width)
    }
}
