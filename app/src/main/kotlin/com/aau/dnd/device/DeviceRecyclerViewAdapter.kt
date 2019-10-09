package com.aau.dnd.device

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils.loadAnimation
import androidx.recyclerview.widget.RecyclerView
import com.aau.dnd.R

class DeviceRecyclerViewAdapter(
    private val onClick: (device: Device) -> Unit,
    private val context: Context
) : RecyclerView.Adapter<DeviceViewHolder>() {

    private val devices = mutableListOf<Device>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DeviceViewHolder {
        val view = LayoutInflater
            .from(context)
            .inflate(
                R.layout.row_device,
                parent,
                false
            )

        val holder = DeviceViewHolder(
            name = view.findViewById(R.id.name),
            view = view
        )

        view.setOnClickListener {
            handleClick(holder)
        }

        return holder
    }

    override fun getItemCount() = devices.size

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val animation = loadAnimation(context, android.R.anim.slide_in_left)

        holder.itemView.startAnimation(animation)
        holder.name.text = devices[position].name
    }

    override fun onViewDetachedFromWindow(holder: DeviceViewHolder) {
        holder.itemView.clearAnimation()
    }

    fun setDevices(devices: Iterable<Device>) {
        this.devices.clear()
        this.devices.addAll(devices)

        notifyDataSetChanged()
    }

    private fun handleClick(holder: DeviceViewHolder) {
        onClick(devices[holder.adapterPosition])
    }
}
