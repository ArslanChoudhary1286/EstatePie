package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.billspayment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemResidentialBinding
import com.jeuxdevelopers.estatepie.databinding.ItemUtilityBillsBinding
import com.jeuxdevelopers.estatepie.network.responses.management.dashboard.MangeUtilitiesResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.AdsPropertiesResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.ResidentialPropertiesResponse

class UtilityBillsAdapter(
    private val onItemClicked: (MangeUtilitiesResponse.Data.Utility) -> Unit,
) :
    ListAdapter<MangeUtilitiesResponse.Data.Utility, UtilityBillsAdapter.AdsGridViewHolder>(DiffCallback) {

    inner class AdsGridViewHolder(private val binding: ItemUtilityBillsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MangeUtilitiesResponse.Data.Utility) {

            binding.root.setOnClickListener { onItemClicked(item) }

            binding.tvUserName.text = item.name
            binding.tvAddress.text = item.address
            binding.tvPrice.text = item.total

            Glide.with(binding.root).load(item.image)
                .centerCrop()
                .placeholder(R.drawable.ic_house_gray)
                .error(R.drawable.ic_house_gray)
                .dontAnimate()
                .into(binding.houseImage)

        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<MangeUtilitiesResponse.Data.Utility>() {
        override fun areItemsTheSame(oldItem: MangeUtilitiesResponse.Data.Utility, newItem: MangeUtilitiesResponse.Data.Utility) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: MangeUtilitiesResponse.Data.Utility, newItem: MangeUtilitiesResponse.Data.Utility) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsGridViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_utility_bills, parent, false)
        return AdsGridViewHolder(
            ItemUtilityBillsBinding.bind(view)
        )
    }

    override fun onBindViewHolder(holder: AdsGridViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


}