package com.jeuxdevelopers.estatepie.ui.fragments.explore.detail.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemImageSlideBinding
import com.jeuxdevelopers.estatepie.network.responses.explore.ExploreRecommendedResponse

class ImagesAdapter(
) :
    ListAdapter<ExploreRecommendedResponse.Data.Recommended.PropertyImage, ImagesAdapter.ImageViewHolder>(DiffCallback) {


    inner class ImageViewHolder(private val binding: ItemImageSlideBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item : ExploreRecommendedResponse.Data.Recommended.PropertyImage){

            Glide.with(binding.root).load(item.image)
                .centerCrop()
                .placeholder(R.drawable.ic_house_gray)
                .error(R.drawable.ic_house_gray)
                .dontAnimate()
                .into(binding.houseImg);

        }

    }


    private object DiffCallback : DiffUtil.ItemCallback<ExploreRecommendedResponse.Data.Recommended.PropertyImage>() {
        override fun areItemsTheSame(oldItem: ExploreRecommendedResponse.Data.Recommended.PropertyImage, newItem: ExploreRecommendedResponse.Data.Recommended.PropertyImage) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: ExploreRecommendedResponse.Data.Recommended.PropertyImage, newItem: ExploreRecommendedResponse.Data.Recommended.PropertyImage) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemImageSlideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        val item = getItem(position)

        holder.bind(item)
    }


}