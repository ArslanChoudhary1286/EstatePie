package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.requests.event.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemEventUpdatesBinding
import com.jeuxdevelopers.estatepie.network.responses.management.requestReports.AllEventListResponse

class EventsUpdatesAdapter(
    private val onItemClicked: (AllEventListResponse.Data.Event) -> Unit,
    private val onDeleteClicked: (AllEventListResponse.Data.Event) -> Unit,
) :
    ListAdapter<AllEventListResponse.Data.Event, EventsUpdatesAdapter.AdsGridViewHolder>(DiffCallback) {

    inner class AdsGridViewHolder(private val binding: ItemEventUpdatesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AllEventListResponse.Data.Event) {

            binding.root.setOnClickListener { onItemClicked(item) }
            binding.btnDelete.setOnClickListener { onDeleteClicked(item) }
            binding.tvUserName.text = item.title
            binding.tvStatus.text = item.status
            binding.tvEventType.text = item.event_type
            binding.tvDate.text = item.date
            binding.tvTime.text = item.time
            if (item.property_name.isNotEmpty())
                binding.tvPropertyName.text = item.property_name
            else
                binding.tvPropertyName.text = "N/A"
            binding.tvAddress.text = item.address

        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<AllEventListResponse.Data.Event>() {
        override fun areItemsTheSame(oldItem: AllEventListResponse.Data.Event, newItem: AllEventListResponse.Data.Event) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: AllEventListResponse.Data.Event, newItem: AllEventListResponse.Data.Event) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsGridViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_event_updates, parent, false)
        return AdsGridViewHolder(
            ItemEventUpdatesBinding.bind(view)
        )
    }

    override fun onBindViewHolder(holder: AdsGridViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
    }

}