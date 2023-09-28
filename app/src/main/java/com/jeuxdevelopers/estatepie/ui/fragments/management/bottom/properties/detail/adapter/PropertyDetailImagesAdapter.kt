package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemImageSlideBinding
import com.jeuxdevelopers.estatepie.network.responses.management.properties.PropertiesDetailsResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.explore.AdsDetailResponse

class PropertyDetailImagesAdapter(
) :
    ListAdapter<PropertiesDetailsResponse.Data.PropertyImage, PropertyDetailImagesAdapter.ImageViewHolder>(DiffCallback) {


    inner class ImageViewHolder(private val binding: ItemImageSlideBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item : PropertiesDetailsResponse.Data.PropertyImage){

            Glide.with(binding.root).load(item.image)
                .centerCrop()
                .placeholder(R.drawable.ic_house_gray)
                .error(R.drawable.ic_house_gray)
                .dontAnimate()
                .into(binding.houseImg);

        }

    }


    private object DiffCallback : DiffUtil.ItemCallback<PropertiesDetailsResponse.Data.PropertyImage>() {
        override fun areItemsTheSame(oldItem: PropertiesDetailsResponse.Data.PropertyImage, newItem: PropertiesDetailsResponse.Data.PropertyImage) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: PropertiesDetailsResponse.Data.PropertyImage, newItem: PropertiesDetailsResponse.Data.PropertyImage) =
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