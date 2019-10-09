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

    // A separate set of macs is maintained to keep devices unique.
    private val deviceMacs = mutableSetOf<String>()
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
            view = view,
            name = view.findViewById(R.id.name),
            mac = view.findViewById(R.id.mac)
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

        devices[position].run {
            holder.name.text = name
            holder.mac.text = mac
        }
    }

    override fun onViewDetachedFromWindow(holder: DeviceViewHolder) {
        holder.itemView.clearAnimation()
    }

    fun clearDevices() {
        val previousSize = devices.size

        deviceMacs.clear()
        devices.clear()

        notifyItemRangeRemoved(0, previousSize)
    }

    fun addDevice(device: Device) {
        val previousSize = devices.size

        if (device.mac !in deviceMacs) {
            deviceMacs.add(device.mac)
            devices.add(device)

            notifyItemInserted(previousSize)
        }
    }

    private fun handleClick(holder: DeviceViewHolder) {
        onClick(devices[holder.adapterPosition])
    }
}
