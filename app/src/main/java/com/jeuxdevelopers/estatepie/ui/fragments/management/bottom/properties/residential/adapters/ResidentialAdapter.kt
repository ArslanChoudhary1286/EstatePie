package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.residential.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemResidentialBinding
import com.jeuxdevelopers.estatepie.network.responses.management.properties.AdsPropertiesResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.ResidentialPropertiesResponse

class ResidentialAdapter(
    private val onItemClicked: (ResidentialPropertiesResponse.Data.Property) -> Unit,
    private val onMenuItemClicked: (ResidentialPropertiesResponse.Data.Property, Int) -> Unit,
) :
    ListAdapter<ResidentialPropertiesResponse.Data.Property, ResidentialAdapter.AdsGridViewHolder>(DiffCallback) {

    inner class AdsGridViewHolder(private val binding: ItemResidentialBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResidentialPropertiesResponse.Data.Property) {

            binding.root.setOnClickListener { onItemClicked(item) }

            binding.root.setOnLongClickListener{
                onMenuItemClicked(item, adapterPosition)
                true
            }
            binding.tvHouseName.text = item.name
            binding.tvAddress.text = item.address
            binding.tvAmount.text = item.unit

            Glide.with(binding.root).load(item.image)
                .centerCrop()
                .placeholder(R.drawable.ic_house_gray)
                .error(R.drawable.ic_house_gray)
                .dontAnimate()
                .into(binding.houseImage)

        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<ResidentialPropertiesResponse.Data.Property>() {
        override fun areItemsTheSame(oldItem: ResidentialPropertiesResponse.Data.Property, newItem: ResidentialPropertiesResponse.Data.Property) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: ResidentialPropertiesResponse.Data.Property, newItem: ResidentialPropertiesResponse.Data.Property) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsGridViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_residential, parent, false)
        return AdsGridViewHolder(
            ItemResidentialBinding.bind(view)
        )
    }

    override fun onBindViewHolder(holder: AdsGridViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


}