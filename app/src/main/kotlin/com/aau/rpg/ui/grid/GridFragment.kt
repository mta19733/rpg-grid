package com.aau.rpg.ui.grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aau.rpg.R
import com.aau.rpg.ui.connection.BluetoothViewModel
import kotlinx.android.synthetic.main.fragment_grid.view.grid
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class GridFragment : Fragment() {

    private val bluetooth by sharedViewModel<BluetoothViewModel>()

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

        val gridSize = resources.getInteger(R.integer.grid_size)

        view.grid.columnCount = gridSize
        view.grid.rowCount = gridSize

        (0 until gridSize).forEach { rowIdx ->
            (0 until gridSize).forEach { colIdx ->
                view.grid.addView(
                    GridButton(
                        context = view.context,
                        checked = false,
                        idx = rowIdx * gridSize + colIdx,
                        onClick = { idx, checked ->
                            println("$idx = $checked")
                        }
                    )
                )
            }
        }

        return view
    }
}
