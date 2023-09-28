package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.dashboard.incidentreport.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemIncidentReportBinding
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.IncidentReportResponse

class TenantIncidentReportAdapter(
    private val onItemClicked: (IncidentReportResponse.Data.IncidentRequest) -> Unit,
) :
    ListAdapter<IncidentReportResponse.Data.IncidentRequest, TenantIncidentReportAdapter.AdsGridViewHolder>(DiffCallback) {


    inner class AdsGridViewHolder(private val binding: ItemIncidentReportBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: IncidentReportResponse.Data.IncidentRequest) {

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


    private object DiffCallback : DiffUtil.ItemCallback<IncidentReportResponse.Data.IncidentRequest>() {
        override fun areItemsTheSame(oldItem: IncidentReportResponse.Data.IncidentRequest, newItem: IncidentReportResponse.Data.IncidentRequest) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: IncidentReportResponse.Data.IncidentRequest, newItem: IncidentReportResponse.Data.IncidentRequest) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsGridViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_incident_report, parent, false)
        return AdsGridViewHolder(
            ItemIncidentReportBinding.bind(view)
        )
    }

    override fun onBindViewHolder(holder: AdsGridViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
    }


}