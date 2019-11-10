package com.aau.rpg.ui.grid

import androidx.lifecycle.MutableLiveData
import com.aau.rpg.core.grid.Grid
import com.aau.rpg.core.grid.Position
import com.aau.rpg.core.grid.gridOf
import com.aau.rpg.core.grid.positionOf
import com.aau.rpg.core.grid.view
import kotlin.math.max
import kotlin.math.min

class GameGridViewModel(
    private val idDelimiter: String,
    private val viewSize: Int
) : GridViewModel() {

    override val directionState = MutableLiveData<DirectionState>()

    override val currentGrid = MutableLiveData<Grid>(gridOf())
    override val viewGrid = MutableLiveData<Grid>(gridOf())

    override val viewIds = MutableLiveData<String>()

    private var position: Position = positionOf()

    override fun loadViewIds() {
        val tiles = grid
            .view(position, viewSize)
            .tiles

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
        position = positionOf()
        currentGrid.value = grid

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
        val newPosition = Position(
            row = resolveViewRow(direction),
            col = resolveViewCol(direction)
        )

        position = newPosition
    }

    private fun createViewGrid() = grid.view(
        position = position,
        size = min(grid.size, viewSize)
    )

    private fun notifyViewGridChanges() {
        viewGrid.value = createViewGrid()

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
        get() = currentGrid.value ?: gridOf()
}
