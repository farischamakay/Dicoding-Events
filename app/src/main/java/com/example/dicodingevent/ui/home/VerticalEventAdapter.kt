package com.example.dicodingevent.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.data.response.EventResponse
import com.example.dicodingevent.databinding.ItemEventVerticalBinding

class VerticalEventAdapter :
    ListAdapter<EventResponse.EventData, VerticalEventAdapter.ViewHolder>(DiffCallback) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    companion object {
        private val DiffCallback =
            object : androidx.recyclerview.widget.DiffUtil.ItemCallback<EventResponse.EventData>() {
                override fun areItemsTheSame(
                    oldItem: EventResponse.EventData,
                    newItem: EventResponse.EventData
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: EventResponse.EventData,
                    newItem: EventResponse.EventData
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemEventVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(data)
        }
        holder.bind(data)
    }

    class ViewHolder(private var itemBinding: ItemEventVerticalBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: EventResponse.EventData) {
            Glide.with(itemBinding.imgEvent.context)
                .load(item.imageLogo)
                .into(itemBinding.imgEvent)
            itemBinding.tvTitleEvent.text = item.name
            itemBinding.tvDateEvent.text = item.endTime
            itemBinding.tvQuotaEvent.text = item.quota
            itemBinding.tvPenyelenggaraEvent.text = item.ownerName
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: EventResponse.EventData)
    }
}