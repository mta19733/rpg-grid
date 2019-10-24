package com.aau.rpg.ui.grid

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.aau.rpg.R
import com.aau.rpg.core.grid.Grid
import kotlinx.android.synthetic.main.fragment_grid.button_down
import kotlinx.android.synthetic.main.fragment_grid.button_left
import kotlinx.android.synthetic.main.fragment_grid.button_right
import kotlinx.android.synthetic.main.fragment_grid.button_up
import kotlinx.android.synthetic.main.fragment_grid.grid
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class GridFragment : Fragment() {

    private val gridViewModel by sharedViewModel<GridViewModel>()

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, saved: Bundle?): View =
        inflater.inflate(R.layout.fragment_grid, group, false)

    override fun onViewCreated(view: View, saved: Bundle?) {
        gridViewModel.grid.observe(this, observeGrid())
        setupNavigation()
    }

    private fun setupNavigation() {
        val navigation = listOf(
            button_left to Direction.LEFT,
            button_right to Direction.RIGHT,
            button_up to Direction.UP,
            button_down to Direction.DOWN
        )

        navigation.forEach { (button, direction) ->
            button.setOnClickListener {
                gridViewModel.move(direction)
            }
        }
    }

    private fun onCheckChange(id: Int, checked: Boolean) {
        gridViewModel.set(id, checked)
    }

    private fun addButton(context: Context, tile: Tile) {
        val button = GridButton(
            context = context,
            checked = tile.value,
            id = tile.id,
            onCheckChange = ::onCheckChange
        )

        grid.addView(button)
    }

    private fun initializeGrid(newGrid: Grid) {
        grid.removeAllViews()

        val size = newGrid.size
        grid.columnCount = size
        grid.rowCount = size

        val context = requireContext()
        newGrid.tiles
            .flatten()
            .forEach { tile ->
                addButton(context, tile)
            }
    }

    private fun updateGrid(newGrid: Grid) {
        val tiles = newGrid.tiles.flatten()

        grid.children.forEachIndexed { idx, button ->
            if (button is GridButton) {
                val tile = tiles[idx]

                button.refresh(
                    checked = tile.value,
                    id = tile.id
                )
            }
        }
    }

    private fun observeGrid() = Observer<Grid> { newGrid ->
        val size = newGrid.size

        if (grid.columnCount != size || grid.rowCount != size) {
            initializeGrid(newGrid)
        } else {
            updateGrid(newGrid)
        }
    }
}
