package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.requests.incident.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemRequestsByPropertyIdBinding
import com.jeuxdevelopers.estatepie.network.responses.management.requestReports.IncidentWithFilterResponse

class IncidentWithFilterAdapter(
    private val onItemClicked: (IncidentWithFilterResponse.Data.IncidentRequest) -> Unit,
) :
    ListAdapter<IncidentWithFilterResponse.Data.IncidentRequest, IncidentWithFilterAdapter.AdsGridViewHolder>(DiffCallback) {


    inner class AdsGridViewHolder(private val binding: ItemRequestsByPropertyIdBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: IncidentWithFilterResponse.Data.IncidentRequest) {

            binding.root.setOnClickListener { onItemClicked(item) }
            binding.tvUserName.text = item.name
            binding.tvDate.text = item.created_at
            binding.tvStatus.text = item.status
            binding.tvDescription.text = item.description
            Glide.with(binding.root).load(item.image)
                .centerCrop()
                .placeholder(R.drawable.ic_house_gray)
                .error(R.drawable.ic_house_gray)
                .dontAnimate()
                .into(binding.iconImage)

        }
    }


    private object DiffCallback : DiffUtil.ItemCallback<IncidentWithFilterResponse.Data.IncidentRequest>() {
        override fun areItemsTheSame(oldItem: IncidentWithFilterResponse.Data.IncidentRequest, newItem: IncidentWithFilterResponse.Data.IncidentRequest) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: IncidentWithFilterResponse.Data.IncidentRequest, newItem: IncidentWithFilterResponse.Data.IncidentRequest) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsGridViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_requests_by_property_id, parent, false)
        return AdsGridViewHolder(
            ItemRequestsByPropertyIdBinding.bind(view)
        )
    }

    override fun onBindViewHolder(holder: AdsGridViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
    }

}