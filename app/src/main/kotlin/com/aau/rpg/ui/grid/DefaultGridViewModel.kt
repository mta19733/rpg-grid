package com.aau.rpg.ui.grid

import androidx.lifecycle.MutableLiveData
import com.aau.rpg.core.grid.Grid
import com.aau.rpg.core.grid.GridStorageService
import com.aau.rpg.core.grid.subGrid
import kotlin.math.max

class DefaultGridViewModel(
    gridStorageService: GridStorageService,
    private val viewSize: Int
) : GridViewModel() {

    private var fullGrid: Grid = gridStorageService.load()

    private var viewRowIdx = 0
    private var viewColIdx = 0

    override val grid = MutableLiveData<Grid>(createViewGrid())

    override val tile = MutableLiveData<Tile>()

    override fun move(direction: Direction) {
        updateViewGridPosition(direction)
        notifyViewGridChanges()
    }

    override fun set(id: Int, value: Boolean) {
        val newTile = Tile(id, value)

        updateFullGrid(newTile)
        notifyViewGridChanges()

        tile.value = newTile
    }

    private fun resolveViewRow(direction: Direction?) = when (direction) {
        Direction.UP -> max(viewRowIdx - viewSize, 0)
        Direction.DOWN -> max(viewRowIdx + viewSize, 0)
        else -> viewRowIdx
    }

    private fun resolveViewCol(direction: Direction?) = when (direction) {
        Direction.LEFT -> max(viewColIdx - viewSize, 0)
        Direction.RIGHT -> max(viewColIdx + viewSize, 0)
        else -> viewColIdx
    }

    private fun updateViewGridPosition(direction: Direction) {
        viewRowIdx = resolveViewRow(direction)
        viewColIdx = resolveViewCol(direction)
    }

    private fun createViewGrid() = fullGrid.subGrid(
        fromRow = viewRowIdx,
        fromCol = viewColIdx,
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
