package com.example.dicodingevent.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.data.response.EventResponse
import com.example.dicodingevent.databinding.ItemEventHorizontalBinding

class HorizontalEventAdapter :
    ListAdapter<EventResponse.EventData, HorizontalEventAdapter.CustomViewHolder>(DiffCallback) {

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
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: EventResponse.EventData,
                    newItem: EventResponse.EventData
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding =
            ItemEventHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClickedHorizontal(data)
        }
    }

    class CustomViewHolder(private var itemBinding: ItemEventHorizontalBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: EventResponse.EventData) {
            Glide.with(itemBinding.imgEventHorizontal.context)
                .load(item.imageLogo)
                .into(itemBinding.imgEventHorizontal)
            itemBinding.tvTitleEventHorizontal.text = item.name
        }
    }

    interface OnItemClickCallback {
        fun onItemClickedHorizontal(data: EventResponse.EventData)
    }

}