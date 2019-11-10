package com.aau.rpg.core.grid

import com.aau.rpg.ui.grid.Tile
import kotlin.math.max
import kotlin.math.min

/**
 * Returns a "view" 2D grid of [size] from given [position].
 */
fun Grid.viewOf(position: Position, size: Int): Grid {
    val (row, col) = position

    val rowSize = min(tiles.size, size)
    val tiles = tiles
        .subList(row, row + rowSize)
        .map { tileRow ->
            val colSize = min(tileRow.size, size)
            tileRow.subList(col, col + colSize)
        }

    return copy(
        tiles = tiles,
        size = size
    )
}

/**
 * Returns position which is the middle of the grid.
 */
fun Grid.middleOf(viewSize: Int): Position {
    val multiplier = size / max(1, viewSize) / 2
    val middle = viewSize * multiplier

    return Position(
        row = middle,
        col = middle
    )
}

/**
 * Returns 2D with given parameters.
 */
fun gridOf(
    size: Int = 0,
    name: String = "",
    tiles: List<List<Tile>> = createTiles(size)
): Grid = Grid(
    tiles = tiles,
    size = size,
    name = name
)

private fun createTiles(size: Int) = List(size) { rowIdx ->
    List(size) { colIdx ->
        Tile(
            value = false,
            id = rowIdx * size + colIdx
        )
    }
}
