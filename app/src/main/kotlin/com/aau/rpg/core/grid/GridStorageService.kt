package com.aau.rpg.core.grid

interface GridStorageService {

    /**
     * Load last saved grid.
     */
    fun load(): Grid
}
