package com.aau.rpg.core.grid

import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

interface GridStorageService {

    /**
     * Load grid from previous session.
     */
    fun load(): Single<Grid>

    /**
     * Returns a list of grid names.
     */
    fun list(): Observable<List<String>>

    /**
     * Returns a single grid by name.
     */
    fun load(name: String): Observable<Grid>

    /**
     * Saves provided grid with given name.
     */
    fun save(grid: Grid): Observable<Unit>

    /**
     * Deletes grid with given name.
     */
    fun delete(name: String): Observable<Unit>
}
