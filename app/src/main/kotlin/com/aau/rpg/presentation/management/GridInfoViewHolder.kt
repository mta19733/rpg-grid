package com.aau.rpg.presentation.management

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GridInfoViewHolder(
    itemView: View,
    val name: TextView,
    val delete: Button,
    val edit: Button
) : RecyclerView.ViewHolder(itemView)
