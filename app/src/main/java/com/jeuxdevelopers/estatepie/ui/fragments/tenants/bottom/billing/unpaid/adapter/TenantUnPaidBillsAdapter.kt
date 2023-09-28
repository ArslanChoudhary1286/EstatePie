package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.billing.unpaid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemBillsByTenantIdBinding
import com.jeuxdevelopers.estatepie.databinding.ItemUpcomingBillsBinding
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.ListOfBillsResponse
import com.jeuxdevelopers.estatepie.utils.ToastUtility

class TenantUnPaidBillsAdapter(
    private val onItemClicked: (ListOfBillsResponse.Data.MaintenanceRequest) -> Unit,
) :
    ListAdapter<ListOfBillsResponse.Data.MaintenanceRequest, TenantUnPaidBillsAdapter.AdsGridViewHolder>(DiffCallback) {

    inner class AdsGridViewHolder(private val binding: ItemUpcomingBillsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListOfBillsResponse.Data.MaintenanceRequest) {

            binding.root.setOnClickListener { onItemClicked(item) }
            binding.tvBillName.text = item.bill_type
            binding.tvBillAmount.text = "$"+item.amount.toString()
            binding.tvStatus.text = item.status
            binding.tvDate.text = item.date
            Glide.with(binding.root).load(item.bill_type_icon)
                .centerCrop()
                .placeholder(R.drawable.ic_house_gray)
                .error(R.drawable.ic_house_gray)
                .dontAnimate()
                .into(binding.iconImage);

        }
    }


    private object DiffCallback : DiffUtil.ItemCallback<ListOfBillsResponse.Data.MaintenanceRequest>() {
        override fun areItemsTheSame(oldItem: ListOfBillsResponse.Data.MaintenanceRequest, newItem: ListOfBillsResponse.Data.MaintenanceRequest) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: ListOfBillsResponse.Data.MaintenanceRequest, newItem: ListOfBillsResponse.Data.MaintenanceRequest) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsGridViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_upcoming_bills, parent, false)
        return AdsGridViewHolder(
            ItemUpcomingBillsBinding.bind(view)
        )
    }

    override fun onBindViewHolder(holder: AdsGridViewHolder, position: Int) {

        val item = getItem(position)
        holder.bind(item)

    }


}