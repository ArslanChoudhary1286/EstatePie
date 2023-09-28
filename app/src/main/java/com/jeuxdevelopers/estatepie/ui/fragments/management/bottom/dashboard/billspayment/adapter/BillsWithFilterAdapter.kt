package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.billspayment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemBillsByTenantIdBinding
import com.jeuxdevelopers.estatepie.databinding.ItemBillsWithFilterBinding
import com.jeuxdevelopers.estatepie.databinding.ItemUnpaidBillsBinding
import com.jeuxdevelopers.estatepie.network.responses.management.billing.UnPaidBillByTenantIdResponse
import com.jeuxdevelopers.estatepie.network.responses.management.dashboard.PropertyBillsResponse

class BillsWithFilterAdapter(
    private val onItemClicked: (PropertyBillsResponse.Data.Bill) -> Unit,
) :
    ListAdapter<PropertyBillsResponse.Data.Bill, BillsWithFilterAdapter.AdsGridViewHolder>(DiffCallback) {


    inner class AdsGridViewHolder(private val binding: ItemBillsWithFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PropertyBillsResponse.Data.Bill) {

            binding.root.setOnClickListener { onItemClicked(item) }
            binding.tvBillName.text = item.property_name
            binding.tvBillAmount.text = "$"+item.amount.toString()
            binding.tvStatus.text = item.status
            Glide.with(binding.root).load(item.image)
                .centerCrop()
                .placeholder(R.drawable.ic_house_gray)
                .error(R.drawable.ic_house_gray)
                .dontAnimate()
                .into(binding.iconImage);

        }
    }


    private object DiffCallback : DiffUtil.ItemCallback<PropertyBillsResponse.Data.Bill>() {
        override fun areItemsTheSame(oldItem: PropertyBillsResponse.Data.Bill, newItem: PropertyBillsResponse.Data.Bill) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: PropertyBillsResponse.Data.Bill, newItem: PropertyBillsResponse.Data.Bill) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsGridViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_bills_with_filter, parent, false)
        return AdsGridViewHolder(
            ItemBillsWithFilterBinding.bind(view)
        )
    }

    override fun onBindViewHolder(holder: AdsGridViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
    }


}