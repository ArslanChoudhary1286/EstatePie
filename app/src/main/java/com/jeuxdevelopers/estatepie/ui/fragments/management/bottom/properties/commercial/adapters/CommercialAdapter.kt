package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.commercial.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemCommercialBinding
import com.jeuxdevelopers.estatepie.network.responses.management.properties.CommercialPropertiesResponse

class CommercialAdapter(
    private val onItemClicked: (CommercialPropertiesResponse.Data.Proprety) -> Unit,
    private val onMenuItemClicked: (CommercialPropertiesResponse.Data.Proprety, Int) -> Unit,
) :
    ListAdapter<CommercialPropertiesResponse.Data.Proprety, CommercialAdapter.AdsGridViewHolder>(DiffCallback) {

    inner class AdsGridViewHolder(private val binding: ItemCommercialBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CommercialPropertiesResponse.Data.Proprety){

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


    private object DiffCallback : DiffUtil.ItemCallback<CommercialPropertiesResponse.Data.Proprety>() {
        override fun areItemsTheSame(oldItem: CommercialPropertiesResponse.Data.Proprety, newItem: CommercialPropertiesResponse.Data.Proprety) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: CommercialPropertiesResponse.Data.Proprety, newItem: CommercialPropertiesResponse.Data.Proprety) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsGridViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_commercial, parent, false)
        return AdsGridViewHolder(
            ItemCommercialBinding.bind(view)
        )
    }

    override fun onBindViewHolder(holder: AdsGridViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


}