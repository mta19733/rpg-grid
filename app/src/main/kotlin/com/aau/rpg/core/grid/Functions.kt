package com.aau.rpg.core.grid

import com.aau.rpg.ui.grid.Tile

/**
 * Returns 2D grid of [size] from given row [fromRow] and column [fromCol].
 */
fun Grid.subGrid(fromRow: Int, fromCol: Int, size: Int): Grid {
    val tiles = tiles
        .subList(fromRow, fromRow + size)
        .map { tileRow ->
            tileRow.subList(fromCol, fromCol + size)
        }

    return Grid(
        tiles = tiles,
        size = size
    )
}

/**
 * Returns normalized id string.
 */
fun Grid.normalizedIds(): String {
    val ids = tiles
        .flatten()
        .filter(Tile::value)
        .map(Tile::id)

    val min = ids.min() ?: 0

    return ids
        .map { id -> id - min }
        .joinToString(",")
}

/**
 * Returns 2D [Grid] of given size.
 */
fun grid(size: Int): Grid {
    val tiles = MutableList(size) { rowIdx ->
        MutableList(size) { colIdx ->
            Tile(
                value = false,
                id = rowIdx * size + colIdx
            )
        }
    }

    return Grid(
        tiles = tiles,
        size = size
    )
}
