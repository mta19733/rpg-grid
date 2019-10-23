package com.aau.rpg.ui.grid

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aau.rpg.core.grid.Grid

abstract class GridViewModel : ViewModel() {

    /**
     * Information about grid changes.
     */
    abstract val grid: LiveData<Grid>

    /**
     * Information about tile changes.
     */
    abstract val tile: LiveData<Tile>

    /**
     * Move current grid to given [direction].
     */
    abstract fun move(direction: Direction)

    /**
     * Set tile value.
     */
    abstract fun set(id: Int, value: Boolean)
}
