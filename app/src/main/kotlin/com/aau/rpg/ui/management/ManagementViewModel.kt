package com.aau.rpg.ui.management

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aau.rpg.core.grid.Grid
import com.aau.rpg.core.grid.GridInfo

abstract class ManagementViewModel : ViewModel() {

    /**
     * Result of [isGridNameValid].
     */
    abstract val gridNameValid: LiveData<Boolean>

    /**
     * Information about grid updated via [updateGridName].
     */
    abstract val updated: LiveData<UpdatedGridInfo>

    /**
     * Is something currently loading (e.g. list of infos).
     */
    abstract val loading: LiveData<Boolean>

    /**
     * Grid loaded via [loadGrid].
     */
    abstract val loaded: LiveData<Grid>

    /**
     * Information about all existing grids.
     */
    abstract val infos: LiveData<List<GridInfo>>

    /**
     * Errors.
     */
    abstract val error: LiveData<Throwable>

    /**
     * Create a blank grid with given [name].
     */
    abstract fun createGrid(name: String)

    /**
     * Load a list all available grids.
     */
    abstract fun loadGrids()

    /**
     * Load grid with given [name].
     */
    abstract fun loadGrid(name: String)

    /**
     * Check if [newName] is a valid grid name.
     */
    abstract fun isGridNameValid(oldName: String? = null, newName: String)

    /**
     * Update grids name to [newName].
     */
    abstract fun updateGridName(oldName: String, newName: String)

    /**
     * Save [grid].
     */
    abstract fun saveGrid(grid: Grid)

    /**
     * Delete grid with given [name].
     */
    abstract fun deleteGrid(name: String)
}
