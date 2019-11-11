package com.aau.rpg.data.grid

data class Grid(

    /**
     * Name of the grid.
     */
    val name: String,

    /**
     * Size of the grid. Note that width and height are the same, aka 2D grid.
     */
    val size: Int,

    /**
     * Contents of 2D gird.
     */
    val tiles: List<List<Tile>>
)
