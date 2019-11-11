package com.aau.rpg.presentation.grid

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.aau.rpg.R
import com.aau.rpg.data.grid.Grid
import com.aau.rpg.data.grid.Tile
import com.aau.rpg.presentation.connection.BluetoothViewModel
import com.aau.rpg.presentation.management.ManagementViewModel
import com.aau.rpg.presentation.util.showAlertDialog
import kotlinx.android.synthetic.main.fragment_grid.button_down
import kotlinx.android.synthetic.main.fragment_grid.button_left
import kotlinx.android.synthetic.main.fragment_grid.button_right
import kotlinx.android.synthetic.main.fragment_grid.button_save
import kotlinx.android.synthetic.main.fragment_grid.button_sync
import kotlinx.android.synthetic.main.fragment_grid.button_up
import kotlinx.android.synthetic.main.fragment_grid.grid
import kotlinx.android.synthetic.main.fragment_grid.grid_name
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class GridFragment : Fragment() {

    private val managementViewModel by sharedViewModel<ManagementViewModel>()
    private val bluetoothViewModel by sharedViewModel<BluetoothViewModel>()
    private val gridViewModel by sharedViewModel<GridViewModel>()

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, saved: Bundle?): View =
        inflater.inflate(R.layout.fragment_grid, group, false)

    override fun onViewCreated(view: View, saved: Bundle?) {
        bluetoothViewModel.connected.observe(this, connectedObserver())

        gridViewModel.directionState.observe(this, directionStateObserver())
        gridViewModel.currentGrid.observe(this, currentGridObserver())
        gridViewModel.viewGrid.observe(this, viewGridObserver())
        gridViewModel.viewIds.observe(this, viewIdsObserver())

        setupNavigationButtons()
        setupSyncButton()
        setupSaveButton()
    }

    private fun directionStateObserver() = Observer<DirectionState> { (direction, enabled) ->
        val button = when (direction) {
            Direction.LEFT -> button_left
            Direction.RIGHT -> button_right
            Direction.UP -> button_up
            Direction.DOWN -> button_down
        }

        button.isEnabled = enabled
    }

    private fun currentGridObserver() = Observer<Grid> { grid ->
        val (enabled, name) = if (grid.size == 0) {
            false to ""
        } else {
            true to grid.name
        }

        button_save.isEnabled = enabled
        grid_name.text = name
    }

    private fun connectedObserver() = Observer<Boolean> { connected ->
        button_sync.isEnabled = connected
    }

    private fun viewIdsObserver() = Observer<String> { info ->
        bluetoothViewModel.send(info)
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

    private fun viewGridObserver() = Observer<Grid> { viewGrid ->
        val size = viewGrid.size
        if (grid.columnCount != size || grid.rowCount != size) {
            initializeGrid(viewGrid)
        } else {
            updateGrid(viewGrid)
        }
    }

    private fun setupNavigationButtons() {
        val navigation = listOf(
            button_left to Direction.LEFT,
            button_right to Direction.RIGHT,
            button_up to Direction.UP,
            button_down to Direction.DOWN
        )

        navigation.forEach { (button, direction) ->
            button.isEnabled = false
            button.setOnClickListener {
                gridViewModel.move(direction)
            }
        }
    }

    private fun setupSyncButton() {
        button_sync.setOnClickListener {
            gridViewModel.loadViewIds()
        }
    }

    private fun setupSaveButton() {
        button_save.setOnClickListener {
            showAlertDialog(
                positiveButtonText = getString(R.string.button_save),
                messageText = getString(R.string.text_grid_save),
                titleText = getString(R.string.text_grid_title_save),
                onSubmit = {
                    gridViewModel.currentGrid.value?.let { grid ->
                        managementViewModel.saveGrid(grid)
                    }
                }
            )
        }
    }
}
