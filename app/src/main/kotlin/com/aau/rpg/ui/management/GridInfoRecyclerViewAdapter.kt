package com.aau.rpg.ui.management

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aau.rpg.R
import com.aau.rpg.core.grid.GridInfo
import kotlinx.android.synthetic.main.list_grid_info.view.button_delete
import kotlinx.android.synthetic.main.list_grid_info.view.button_edit
import kotlinx.android.synthetic.main.list_grid_info.view.text_name

class GridInfoRecyclerViewAdapter(
    private val onDelete: (info: GridInfo) -> Unit,
    private val onClick: (info: GridInfo) -> Unit,
    private val onEdit: (info: GridInfo) -> Unit
) : RecyclerView.Adapter<GridInfoViewHolder>() {

    private val infos = mutableListOf<GridInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridInfoViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.list_grid_info,
                parent,
                false
            )

        return GridInfoViewHolder(
            delete = itemView.button_delete,
            edit = itemView.button_edit,
            name = itemView.text_name,
            itemView = itemView
        )
    }

    override fun getItemCount() = infos.size

    override fun onBindViewHolder(holder: GridInfoViewHolder, position: Int) {
        val info = infos[position]

        holder.delete.setOnClickListener {
            onDelete(info)
        }

        holder.itemView.setOnClickListener {
            onClick(info)
        }

        holder.edit.setOnClickListener {
            onEdit(info)
        }

        holder.name.text = info.name
    }

    operator fun plusAssign(infos: Iterable<GridInfo>) {
        this.infos.clear()
        this.infos.addAll(infos)

        notifyDataSetChanged()
    }
}
