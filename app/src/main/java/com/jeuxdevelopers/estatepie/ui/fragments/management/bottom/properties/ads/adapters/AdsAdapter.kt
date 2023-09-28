package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.ads.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemAdsBinding
import com.jeuxdevelopers.estatepie.network.responses.management.properties.AdsPropertiesResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.CommercialPropertiesResponse
import kotlin.reflect.KFunction1

class AdsAdapter(
    private val onItemClicked: (AdsPropertiesResponse.Data.AdProprety) -> Unit,
    private val onMenuItemClicked: (AdsPropertiesResponse.Data.AdProprety, Int) -> Unit,
) :
    ListAdapter<AdsPropertiesResponse.Data.AdProprety, AdsAdapter.AdsGridViewHolder>(DiffCallback) {


    inner class AdsGridViewHolder(private val binding: ItemAdsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AdsPropertiesResponse.Data.AdProprety){

            binding.root.setOnLongClickListener{
                onMenuItemClicked(item, adapterPosition)
                true
            }

            binding.root.setOnClickListener { onItemClicked(item) }
            Glide.with(binding.root).load(item.image)
                .centerCrop()
                .placeholder(R.drawable.ic_house_gray)
                .error(R.drawable.ic_house_gray)
                .dontAnimate()
                .into(binding.houseImage)
            binding.tvHouseName.text = item.name
            binding.tvAddress.text = item.address
            binding.tvAmount.text = "$" + item.price.toString()
        }
    }


    private object DiffCallback : DiffUtil.ItemCallback<AdsPropertiesResponse.Data.AdProprety>() {
        override fun areItemsTheSame(oldItem: AdsPropertiesResponse.Data.AdProprety, newItem: AdsPropertiesResponse.Data.AdProprety) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: AdsPropertiesResponse.Data.AdProprety, newItem: AdsPropertiesResponse.Data.AdProprety) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsGridViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_ads, parent, false)
        return AdsGridViewHolder(
            ItemAdsBinding.bind(view)
        )
    }

    override fun onBindViewHolder(holder: AdsGridViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


}