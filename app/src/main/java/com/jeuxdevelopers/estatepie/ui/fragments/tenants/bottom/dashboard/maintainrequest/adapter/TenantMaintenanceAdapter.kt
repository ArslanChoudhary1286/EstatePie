package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.dashboard.maintainrequest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemMaintenanceRequestBinding
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.MaintenanceRequestResponse

class TenantMaintenanceAdapter(
    private val onItemClicked: (MaintenanceRequestResponse.Data.MaintenanceRequest) -> Unit,
) :
    ListAdapter<MaintenanceRequestResponse.Data.MaintenanceRequest, TenantMaintenanceAdapter.AdsGridViewHolder>(DiffCallback) {


    inner class AdsGridViewHolder(private val binding: ItemMaintenanceRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MaintenanceRequestResponse.Data.MaintenanceRequest) {

            binding.root.setOnClickListener { onItemClicked(item) }
            binding.tvTitle.text = item.title
            binding.tvDate.text = item.created_at
            binding.tvDescription.text = item.description
            binding.tvStatus.text = item.status
            Glide.with(binding.root).load(item.property_image)
                .centerCrop()
                .placeholder(R.drawable.ic_house_gray)
                .error(R.drawable.ic_house_gray)
                .dontAnimate()
                .into(binding.itemImage)

        }
    }


    private object DiffCallback : DiffUtil.ItemCallback<MaintenanceRequestResponse.Data.MaintenanceRequest>() {
        override fun areItemsTheSame(oldItem: MaintenanceRequestResponse.Data.MaintenanceRequest, newItem: MaintenanceRequestResponse.Data.MaintenanceRequest) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: MaintenanceRequestResponse.Data.MaintenanceRequest, newItem: MaintenanceRequestResponse.Data.MaintenanceRequest) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsGridViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_maintenance_request, parent, false)
        return AdsGridViewHolder(
            ItemMaintenanceRequestBinding.bind(view)
        )
    }

    override fun onBindViewHolder(holder: AdsGridViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
    }


}