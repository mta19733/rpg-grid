package com.aau.rpg.ui.grid

import android.content.Context
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.ToggleButton
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.setMargins
import com.aau.rpg.R

typealias CheckChangeListener = (id: Int, checked: Boolean) -> Unit

class GridButton(context: Context) : ToggleButton(context) {

    private val clickAnimation = loadAnimation(context, R.anim.bounce)
    private var onCheckChange: CheckChangeListener = emptyListener

    constructor(
        context: Context,
        checked: Boolean,
        id: Int,
        onCheckChange: CheckChangeListener
    ) : this(context) {
        this.onCheckChange = onCheckChange
        this.background = getDrawable(context, R.drawable.selector_button_grid)
        this.isChecked = checked
        this.id = id

        val margin = resources.getDimension(R.dimen.button_grid_margin).toInt()
        val size = resources.getDimension(R.dimen.button_grid_size).toInt()

        val linearLayoutParams = LinearLayout.LayoutParams(size, size)
        val gridLayoutParams = GridLayout.LayoutParams(linearLayoutParams).apply {
            setMargins(margin)
            columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        }

        layoutParams = gridLayoutParams

        textOff = ""
        textOn = ""
        text = ""

        startAnimation()
        setupCheckChangeListener()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, width)
    }

    fun refresh(checked: Boolean, id: Int) {

        // Listener has to be removed before changing checked state, otherwise it gets triggered
        // unnecessarily. Afterwards it has to be reset.
        val oldOnCheckChange = onCheckChange

        this.onCheckChange = emptyListener
        this.isChecked = checked
        this.onCheckChange = oldOnCheckChange

        this.id = id
    }

    private fun startAnimation() {
        startAnimation(clickAnimation)
    }

    private fun setupCheckChangeListener() {
        setOnCheckedChangeListener { _, newChecked ->
            startAnimation()
            onCheckChange(id, newChecked)
        }
    }
}

private val emptyListener: CheckChangeListener = { _, _ -> }
