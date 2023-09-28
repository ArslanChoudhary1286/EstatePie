package com.jeuxdevelopers.estatepie.ui.fragments.explore.viewall.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemAdsVerticalBinding
import com.jeuxdevelopers.estatepie.network.responses.explore.ExploreRecommendedResponse
import com.jeuxdevelopers.estatepie.network.responses.explore.ViewAllRecomResponse
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import timber.log.Timber
import javax.sql.DataSource

class ViewAllRecomAdapter(
    private val onRecommendedItemClicked: (ViewAllRecomResponse.Data.Recommended) -> Unit,
) :
    ListAdapter<ViewAllRecomResponse.Data.Recommended, ViewAllRecomAdapter.AdsGridViewHolder>(DiffCallback) {


    inner class AdsGridViewHolder(private val binding: ItemAdsVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item : ViewAllRecomResponse.Data.Recommended){

            binding.root.setOnClickListener { onRecommendedItemClicked(item) }
            binding.tvAddress.text = item.address
            binding.tvHouseName.text = item.name
            binding.tvAmount.text = StringBuilder().append("$").append(item.price.toString())
            binding.chipTime.text = item.created_on

            Glide.with(binding.root).load(item.image)
                .centerCrop()
                .placeholder(R.drawable.ic_house_gray)
                .error(R.drawable.ic_house_gray)
                .dontAnimate()
                .into(binding.houseImg);


        }
    }


    private object DiffCallback : DiffUtil.ItemCallback<ViewAllRecomResponse.Data.Recommended>() {
        override fun areItemsTheSame(oldItem: ViewAllRecomResponse.Data.Recommended, newItem: ViewAllRecomResponse.Data.Recommended) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: ViewAllRecomResponse.Data.Recommended, newItem: ViewAllRecomResponse.Data.Recommended) =
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