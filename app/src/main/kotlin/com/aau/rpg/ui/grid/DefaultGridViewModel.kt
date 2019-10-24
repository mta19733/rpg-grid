package com.aau.rpg.ui.grid

import androidx.lifecycle.MutableLiveData
import com.aau.rpg.core.grid.Grid
import com.aau.rpg.core.grid.GridStorageService
import com.aau.rpg.core.grid.Position
import com.aau.rpg.core.grid.positionOf
import com.aau.rpg.core.grid.view
import kotlin.math.max
import kotlin.math.min

class DefaultGridViewModel(
    gridStorageService: GridStorageService,
    private val viewSize: Int
) : GridViewModel() {

    private var position: Position = positionOf()
    private var fullGrid: Grid = gridStorageService.load()

    override val grid = MutableLiveData<Grid>(createViewGrid())

    override val info = MutableLiveData<String>()

    override fun createViewInfo() {
        val tiles = fullGrid
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
            .joinToString(",")

        this.info.value = ids
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
            Direction.UP -> max(row - viewSize, 0)
            Direction.DOWN -> min(row + viewSize, fullGrid.size - viewSize)
            else -> row
        }
    }

    private fun resolveViewCol(direction: Direction?): Int {
        val col = position.col

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

        position = newPosition
    }

    private fun createViewGrid() = fullGrid.view(
        position = position,
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
