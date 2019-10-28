package com.aau.rpg.ui.grid

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aau.rpg.core.grid.Grid

abstract class GridViewModel : ViewModel() {

    /**
     * Currently loaded full grid.
     */
    abstract val currentGrid: LiveData<Grid>

    /**
     * View of full grid (peephole).
     */
    abstract val viewGrid: LiveData<Grid>

    /**
     * Ids of [viewGrid].
     */
    abstract val viewIds: LiveData<String>

    /**
     * Load new [viewIds].
     */
    abstract fun loadViewIds()

    /**
     * Load a new grid into [currentGrid].
     */
    abstract fun loadGrid(grid: Grid)

    /**
     * Move [currentGrid] to given [direction].
     */
    abstract fun move(direction: Direction)

    /**
     * Change tile with [id] to new [value].
     */
    abstract fun set(id: Int, value: Boolean)
}
