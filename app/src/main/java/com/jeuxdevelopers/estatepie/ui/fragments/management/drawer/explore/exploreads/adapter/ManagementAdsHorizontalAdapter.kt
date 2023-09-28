package com.jeuxdevelopers.estatepie.ui.fragments.management.drawer.explore.exploreads.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.AdsGridItemBinding
import com.jeuxdevelopers.estatepie.network.responses.explore.ExploreRecommendedResponse
import com.jeuxdevelopers.estatepie.ui.fragments.explore.exploreads.adapters.AdsHorizontalAdapter

class ManagementAdsHorizontalAdapter(
    private val onPopularItemClicked: (ExploreRecommendedResponse.Data.Popular) -> Unit,
) :
    ListAdapter<ExploreRecommendedResponse.Data.Popular, ManagementAdsHorizontalAdapter.AdsGridViewHolder>(
        ManagementAdsHorizontalAdapter.DiffCallback
    ) {


    inner class AdsGridViewHolder(private val binding: AdsGridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ExploreRecommendedResponse.Data.Popular) {

            binding.root.setOnClickListener { onPopularItemClicked(item) }
            binding.tvAddress.text = item.address
            binding.tvHouseName.text = item.name
            binding.tvAmount.text = StringBuilder().append("$").append(item.price.toString())
            binding.tvSale.text = item.purpose

            Glide.with(binding.root).load(item.image)
                .centerCrop()
                .placeholder(R.drawable.ic_house_gray)
                .error(R.drawable.ic_house_gray)
                .dontAnimate()
                .into(binding.houseImage);

        }
    }


    private object DiffCallback : DiffUtil.ItemCallback<ExploreRecommendedResponse.Data.Popular>() {
        override fun areItemsTheSame(oldItem: ExploreRecommendedResponse.Data.Popular, newItem: ExploreRecommendedResponse.Data.Popular) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: ExploreRecommendedResponse.Data.Popular, newItem: ExploreRecommendedResponse.Data.Popular) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsGridViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.ads_grid_item, parent, false)

        return AdsGridViewHolder(
            AdsGridItemBinding.bind(view)
        )
    }

    override fun onBindViewHolder(holder: AdsGridViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
    }

}