package com.aau.rpg.ui.grid

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.aau.rpg.R
import com.aau.rpg.core.grid.Grid
import com.aau.rpg.ui.util.toast
import kotlinx.android.synthetic.main.fragment_grid.grid
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class GridFragment : Fragment() {

    private val gridViewModel by sharedViewModel<GridViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(
            R.layout.fragment_grid,
            container,
            false
        )

        gridViewModel.grid.observe(this, observeGrid())
        gridViewModel.tile.observe(this, observeTile())

        return view
    }

    private fun observeGrid() = Observer<Grid> { newGrid ->
        val size = newGrid.size
        if (grid.columnCount != size || grid.rowCount != size) {
            grid.removeAllViews()

            grid.columnCount = size
            grid.rowCount = size

            val context = requireContext()
            newGrid.tiles
                .flatten()
                .forEach { tile ->
                    addButton(context, tile)
                }
        }
    }

    private fun onGridButtonClick(idx: Int, checked: Boolean) {
        gridViewModel.set(idx, checked)
    }

    private fun addButton(context: Context, tile: Tile) {
        val button = GridButton(
            context = context,
            checked = tile.value,
            id = tile.id,
            onClick = ::onGridButtonClick
        )

        grid.addView(button)
    }

    private fun observeTile() = Observer<Tile> { tile ->
        grid.findViewById<GridButton>(tile.id)?.isChecked = tile.value
        toast(tile.toString())
    }
}
