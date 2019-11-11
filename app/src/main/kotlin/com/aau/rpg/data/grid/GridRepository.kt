package com.aau.rpg.data.grid

import io.reactivex.Observable

interface GridRepository {

    /**
     * Returns information about available grids.
     */
    fun loadGridInfos(): Observable<List<GridInfo>>

    /**
     * Returns a single grid by [name].
     */
    fun loadGrid(name: String): Observable<Grid>

    /**
     * Saves provided grid.
     */
    fun saveGrid(grid: Grid): Observable<Unit>

    /**
     * Deletes grid with given [name].
     */
    fun deleteGrid(name: String): Observable<Unit>
}
