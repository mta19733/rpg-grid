package com.aau.rpg.ui.grid

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aau.rpg.core.grid.Grid
import com.aau.rpg.core.grid.Position

abstract class GridViewModel : ViewModel() {

    /**
     * Information about position changes.
     */
    abstract val position: LiveData<Position>

    /**
     * Information about grid changes.
     */
    abstract val grid: LiveData<Grid>

    /**
     * Move current grid to given [direction].
     */
    abstract fun move(direction: Direction)

    /**
     * Set tile value.
     */
    abstract fun set(id: Int, value: Boolean)
}
