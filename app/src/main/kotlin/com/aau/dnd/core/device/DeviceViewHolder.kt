package com.aau.dnd.core.device

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DeviceViewHolder(
    view: View,
    val name: TextView,
    val mac: TextView
) : RecyclerView.ViewHolder(view)
