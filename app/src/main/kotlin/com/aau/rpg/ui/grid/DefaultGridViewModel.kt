package com.aau.rpg.ui.grid

import androidx.lifecycle.MutableLiveData
import com.aau.rpg.core.grid.Grid
import com.aau.rpg.core.grid.GridStorageService
import com.aau.rpg.core.grid.Position
import com.aau.rpg.core.grid.positionOf
import com.aau.rpg.core.grid.subGrid
import kotlin.math.max
import kotlin.math.min

class DefaultGridViewModel(
    gridStorageService: GridStorageService,
    private val viewSize: Int
) : GridViewModel() {

    private var fullGrid: Grid = gridStorageService.load()

    override val position = MutableLiveData<Position>(positionOf())

    override val grid = MutableLiveData<Grid>(createViewGrid())

    override fun move(direction: Direction) {
        updateViewGridPosition(direction)
        notifyViewGridChanges()
    }

    override fun set(id: Int, value: Boolean) {
        val newTile = Tile(id, value)

        updateFullGrid(newTile)
    }

    private fun currentPosition() = position.value ?: positionOf()

    private fun resolveViewRow(direction: Direction?): Int {
        val row = currentPosition().row

        return when (direction) {
            Direction.UP -> max(row - viewSize, 0)
            Direction.DOWN -> min(row + viewSize, fullGrid.size - viewSize)
            else -> row
        }
    }

    private fun resolveViewCol(direction: Direction?): Int {
        val col = currentPosition().col

        return when (direction) {
            Direction.LEFT -> max(col - viewSize, 0)
            Direction.RIGHT -> min(col + viewSize, fullGrid.size - viewSize)
            else -> col
        }
    }

    private fun updateViewGridPosition(direction: Direction) {
        val newPosition = Position(
            row = resolveViewRow(direction),
            col = resolveViewCol(direction)
        )

        if (newPosition != position.value) {
            position.value = newPosition
        }
    }

    private fun createViewGrid() = fullGrid.subGrid(
        position = currentPosition(),
        size = viewSize
    )

    private fun notifyViewGridChanges() {
        grid.value = createViewGrid()
    }

    private fun updateFullGrid(tile: Tile) {
        val newTiles = fullGrid.tiles.map { tileRow ->
            tileRow.map { tileCol ->
                if (tileCol.id == tile.id) {
                    tile
                } else {
                    tileCol
                }
            }
        }

        fullGrid = Grid(
            tiles = newTiles,
            size = fullGrid.size
        )
    }
}
