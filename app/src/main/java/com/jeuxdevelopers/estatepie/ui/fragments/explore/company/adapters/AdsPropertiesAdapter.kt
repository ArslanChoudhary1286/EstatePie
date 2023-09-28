package com.jeuxdevelopers.estatepie.ui.fragments.explore.company.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemAdsVerticalBinding
import com.jeuxdevelopers.estatepie.network.responses.tenants.explore.AdsPropertyResponse

class AdsPropertiesAdapter(
    private val onAdsPropertiesItemClicked: (AdsPropertyResponse.Data) -> Unit,
) :
    ListAdapter<AdsPropertyResponse.Data, AdsPropertiesAdapter.AdsGridViewHolder>(DiffCallback) {


    inner class AdsGridViewHolder(private val binding: ItemAdsVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item : AdsPropertyResponse.Data){

            binding.root.setOnClickListener { onAdsPropertiesItemClicked(item) }
//            binding.tvAddress.text = item.address
//            binding.tvHouseName.text = item.name
//            binding.tvAmount.text = item.price.toString()
//            binding.chipTime.text = item.created_on
//            Glide.with(binding.root).load(item.image).into(binding.houseImg);

        }
    }


    private object DiffCallback : DiffUtil.ItemCallback<AdsPropertyResponse.Data>() {
        override fun areItemsTheSame(oldItem: AdsPropertyResponse.Data, newItem: AdsPropertyResponse.Data) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: AdsPropertyResponse.Data, newItem: AdsPropertyResponse.Data) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsGridViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_ads_vertical, parent, false)
        return AdsGridViewHolder(
            ItemAdsVerticalBinding.bind(view)
        )
    }

    override fun onBindViewHolder(holder: AdsGridViewHolder, position: Int) {

        val item = getItem(position)

        holder.bind(item)


    }



}