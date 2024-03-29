package com.aau.rpg.presentation.grid

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aau.rpg.data.grid.Grid

abstract class GridViewModel : ViewModel() {

    /**
     * Info about whether it is possible to navigate to given direction.
     */
    abstract val directionState: LiveData<DirectionState>

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
     * Create a new empty grid with given [name].
     */
    abstract fun createGrid(name: String): Grid

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
