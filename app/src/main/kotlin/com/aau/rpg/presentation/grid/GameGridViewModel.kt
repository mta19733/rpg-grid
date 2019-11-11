package com.aau.rpg.presentation.grid

import androidx.lifecycle.MutableLiveData
import com.aau.rpg.data.grid.Grid
import com.aau.rpg.data.grid.Tile
import kotlin.math.max
import kotlin.math.min

class GameGridViewModel(
    private val idDelimiter: String,
    private val gridSize: Int,
    private val viewSize: Int
) : GridViewModel() {

    override val directionState = MutableLiveData<DirectionState>()

    override val currentGrid = MutableLiveData<Grid>(createGridInternal())
    override val viewGrid = MutableLiveData<Grid>(createGridInternal())

    override val viewIds = MutableLiveData<String>()

    private var position: Position =
        Position(0, 0)

    override fun createGrid(name: String) = createGridInternal(
        size = gridSize,
        name = name
    )

    override fun loadViewIds() {
        val tiles = grid.viewOf().tiles
        val ids = tiles
            .flatten()
            .mapIndexedNotNull { idx, tile ->
                if (tile.value) {
                    idx
                } else {
                    null
                }
            }
            .joinToString(idDelimiter)

        this.viewIds.value = ids
    }

    override fun loadGrid(grid: Grid) {
        currentGrid.value = grid
        position = grid.middleOf()

        notifyViewGridChanges()
    }

    override fun move(direction: Direction) {
        updateViewGridPosition(direction)
        notifyViewGridChanges()
    }

    override fun set(id: Int, value: Boolean) {
        val newTile = Tile(id, value)

        updateFullGrid(newTile)
    }

    private fun createGridInternal(size: Int = 0, name: String = "") = Grid(
        tiles = List(size) { rowIdx ->
            List(size) { colIdx ->
                Tile(
                    value = false,
                    id = rowIdx * size + colIdx
                )
            }
        },
        size = size,
        name = name
    )

    private fun Grid.viewOf(): Grid {
        val (row, col) = position

        val normViewSize = min(grid.size, viewSize)
        val rowSize = min(tiles.size, normViewSize)

        val tiles = tiles
            .subList(row, row + rowSize)
            .map { tileRow ->
                val colSize = min(tileRow.size, normViewSize)
                tileRow.subList(col, col + colSize)
            }

        return copy(
            tiles = tiles,
            size = normViewSize
        )
    }

    private fun Grid.middleOf(): Position {
        val multiplier = size / max(1, viewSize) / 2
        val middle = viewSize * multiplier

        return Position(
            row = middle,
            col = middle
        )
    }

    private fun resolveViewRow(direction: Direction?): Int {
        val row = position.row

        return when (direction) {
            Direction.UP -> max(0, row - viewSize)
            Direction.DOWN -> max(
                0,
                min(row + viewSize, grid.size - viewSize)
            )
            else -> row
        }
    }

    private fun resolveViewCol(direction: Direction?): Int {
        val col = position.col

        return when (direction) {
            Direction.LEFT -> max(0, col - viewSize)
            Direction.RIGHT -> max(
                0,
                min(col + viewSize, grid.size - viewSize)
            )
            else -> col
        }
    }

    private fun updateViewGridPosition(direction: Direction) {
        position = position.copy(
            row = resolveViewRow(direction),
            col = resolveViewCol(direction)
        )
    }

    private fun notifyViewGridChanges() {
        viewGrid.value = grid.viewOf()

        val (row, col) = position
        if (row == 0) {
            directionState.value = DirectionState(Direction.UP, false)
        } else {
            directionState.value = DirectionState(Direction.UP, true)
        }

        if (row + viewSize == grid.size) {
            directionState.value = DirectionState(Direction.DOWN, false)
        } else {
            directionState.value = DirectionState(Direction.DOWN, true)
        }

        if (col == 0) {
            directionState.value = DirectionState(Direction.LEFT, false)
        } else {
            directionState.value = DirectionState(Direction.LEFT, true)
        }

        if (col + viewSize == grid.size) {
            directionState.value = DirectionState(Direction.RIGHT, false)
        } else {
            directionState.value = DirectionState(Direction.RIGHT, true)
        }
    }

    private fun updateFullGrid(tile: Tile) {
        val newTiles = grid.tiles.map { tileRow ->
            tileRow.map { tileCol ->
                if (tileCol.id == tile.id) {
                    tile
                } else {
                    tileCol
                }
            }
        }

        currentGrid.value = grid.copy(
            tiles = newTiles
        )
    }

    /**
     * Current grid value.
     */
    private val grid: Grid
        get() = currentGrid.value ?: createGridInternal()
}

private data class Position(val row: Int, val col: Int)

