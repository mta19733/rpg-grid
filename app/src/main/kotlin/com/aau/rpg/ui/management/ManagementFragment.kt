package com.aau.rpg.ui.management

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.aau.rpg.R
import com.aau.rpg.core.grid.Grid
import com.aau.rpg.core.grid.GridInfo
import com.aau.rpg.ui.grid.GridViewModel
import com.aau.rpg.ui.util.showAlertDialog
import kotlinx.android.synthetic.main.alert_grid_info.view.grid_name
import kotlinx.android.synthetic.main.fragment_management.button_create
import kotlinx.android.synthetic.main.fragment_management.list_grids
import kotlinx.android.synthetic.main.fragment_management.progress_loading
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ManagementFragment : Fragment() {

    private val managementViewModel by sharedViewModel<ManagementViewModel>()
    private val gridViewModel by sharedViewModel<GridViewModel>()

    private val adapter by lazy(::createGirdInfoRecyclerView)

    private var dialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, saved: Bundle?): View =
        inflater.inflate(R.layout.fragment_management, group, false)

    override fun onViewCreated(view: View, saved: Bundle?) {
        list_grids.layoutManager = LinearLayoutManager(view.context)
        list_grids.adapter = adapter

        button_create.setOnClickListener(gridCreateListener())

        managementViewModel.gridNameValid.observe(this, gridNameValidObserver())
        managementViewModel.updated.observe(this, updatedObserver())
        managementViewModel.loading.observe(this, loadingObserver())
        managementViewModel.loaded.observe(this, loadedObserver())
        managementViewModel.infos.observe(this, infosObserver())

        managementViewModel.loadGrids()
    }

    private fun showGridAlertDialog(
        titleText: String,
        positiveButtonText: String,
        gridName: String? = "",
        allowSubmit: Boolean = false,
        onChange: (name: String) -> Unit,
        onSubmit: (name: String) -> Unit
    ): AlertDialog {
        val layoutInflater = LayoutInflater.from(requireContext())
        val alertBody = layoutInflater.inflate(
            R.layout.alert_grid_info,
            view as ViewGroup,
            false
        )

        alertBody.grid_name.setText(gridName)
        alertBody.grid_name.addTextChangedListener { newName ->
            onChange(newName.toString())
        }

        val dialog = showAlertDialog(
            positiveButtonText = positiveButtonText,
            titleText = titleText,
            onSubmit = {
                onSubmit(alertBody.grid_name.text.toString())
            },
            view = alertBody
        )

        dialog
            .getButton(AlertDialog.BUTTON_POSITIVE)
            .isEnabled = allowSubmit

        return dialog
    }

    private fun gridCreateListener() = View.OnClickListener {
        dialog = showGridAlertDialog(
            titleText = getString(R.string.text_grid_title_create),
            positiveButtonText = getString(R.string.button_create),
            onChange = { name ->
                managementViewModel.isGridNameValid(
                    newName = name
                )
            },
            onSubmit = { name ->
                managementViewModel.createGrid(name)
            }
        )
    }

    private fun gridNameValidObserver() = Observer<Boolean> { valid ->
        dialog
            ?.getButton(AlertDialog.BUTTON_POSITIVE)
            ?.isEnabled = valid
    }

    private fun onDelete(info: GridInfo) {
        showAlertDialog(
            positiveButtonText = getString(R.string.button_delete),
            messageText = getString(R.string.text_grid_delete, info.name),
            titleText = getString(R.string.text_grid_title_delete),
            onSubmit = {
                managementViewModel.deleteGrid(info.name)
            }
        )
    }

    private fun onClick(info: GridInfo) {
        showAlertDialog(
            positiveButtonText = getString(R.string.button_load),
            messageText = getString(R.string.text_grid_load, info.name),
            titleText = getString(R.string.text_grid_title_load),
            onSubmit = {
                managementViewModel.loadGrid(info.name)
            }
        )
    }

    private fun onEdit(info: GridInfo) {
        dialog = showGridAlertDialog(
            titleText = getString(R.string.text_grid_title_edit),
            positiveButtonText = getString(R.string.button_save),
            allowSubmit = true,
            gridName = info.name,
            onChange = { name ->
                managementViewModel.isGridNameValid(
                    oldName = info.name,
                    newName = name
                )
            },
            onSubmit = { name ->
                managementViewModel.updateGridName(
                    oldName = info.name,
                    newName = name
                )
            }
        )
    }

    private fun createGirdInfoRecyclerView(): GridInfoRecyclerViewAdapter =
        GridInfoRecyclerViewAdapter(
            onDelete = ::onDelete,
            onClick = ::onClick,
            onEdit = ::onEdit
        )

    private fun updatedObserver() = Observer<UpdatedGridInfo> { updated ->
        val currentName = gridViewModel.currentGrid.value?.name
        if (updated.oldName == currentName && updated.newName != currentName) {
            managementViewModel.loadGrid(updated.newName)
        }
    }

    private fun loadingObserver() = Observer<Boolean> { loading ->
        progress_loading.visibility = if (loading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun loadedObserver() = Observer<Grid> { grid ->
        gridViewModel.loadGrid(grid)
    }

    private fun infosObserver() = Observer<List<GridInfo>> { infos ->
        adapter += infos
    }
}
