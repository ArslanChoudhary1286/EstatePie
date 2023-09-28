package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.explore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemAdsVerticalBinding
import com.jeuxdevelopers.estatepie.databinding.ItemTenantsFavoriteBinding
import com.jeuxdevelopers.estatepie.network.responses.tenants.explore.FavoritesResponse

class FavoriteAdapter(
    private val onFavoriteItemClicked: (FavoritesResponse.Data.Recommended) -> Unit
) :
    ListAdapter<FavoritesResponse.Data.Recommended, FavoriteAdapter.AdsGridViewHolder>(DiffCallback) {


    inner class AdsGridViewHolder(private val binding: ItemAdsVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FavoritesResponse.Data.Recommended) {
            binding.root.setOnClickListener { onFavoriteItemClicked(item) }

            binding.tvAddress.text = item.address
            binding.tvHouseName.text = item.name
            binding.tvAmount.text = "$"+item.price.toString()
            binding.chipTime.text = item.created_on
            Glide.with(binding.root).load(item.image)
                .centerCrop()
                .placeholder(R.drawable.ic_house_gray)
                .error(R.drawable.ic_house_gray)
                .dontAnimate()
                .into(binding.houseImg);

        }
    }


    private object DiffCallback : DiffUtil.ItemCallback<FavoritesResponse.Data.Recommended>() {
        override fun areItemsTheSame(oldItem: FavoritesResponse.Data.Recommended, newItem: FavoritesResponse.Data.Recommended) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: FavoritesResponse.Data.Recommended, newItem: FavoritesResponse.Data.Recommended) =
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