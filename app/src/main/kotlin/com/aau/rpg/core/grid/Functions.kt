package com.aau.rpg.core.grid

import com.aau.rpg.ui.grid.Tile

/**
 * Returns 2D grid of [size] from given [position].
 */
fun Grid.subGrid(position: Position, size: Int): Grid {
    val (row, col) = position

    val tiles = tiles
        .subList(row, row + size)
        .map { tileRow ->
            tileRow.subList(col, col + size)
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
 * Returns new [Position].
 */
fun positionOf(row: Int = 0, col: Int = 0) = Position(row, col)

/**
 * Returns 2D [Grid] of given size.
 */
fun gridOf(size: Int): Grid {
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
