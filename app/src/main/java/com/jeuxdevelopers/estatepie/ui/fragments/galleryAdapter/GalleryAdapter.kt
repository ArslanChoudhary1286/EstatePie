package com.jeuxdevelopers.estatepie.ui.fragments.galleryAdapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemAddImageBinding
import com.jeuxdevelopers.estatepie.databinding.ItemGalleryImagesBinding

class GalleryAdapter(
    private val onAddImageClicked: () -> Unit,
    private val onCrossImageClick: (Int) -> Unit,
) :
    ListAdapter<Bitmap, RecyclerView.ViewHolder>(DiffCallback) {

    companion object {
        const val ITEM_TYPE_CONTENT = 0
        const val ITEM_TYPE_BUTTON = 1
    }

    inner class GalleryViewHolder(private val binding: ItemGalleryImagesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Bitmap, position: Int) {

            binding.crossBtn.setOnClickListener {onCrossImageClick(position)}

            Glide.with(binding.root).load(item)
                .centerCrop()
                .placeholder(R.drawable.ic_house_gray)
                .error(R.drawable.ic_house_gray)
                .dontAnimate()
                .into(binding.galleryImage)


        }
    }

    inner class AddImageViewHolder(private val binding: ItemAddImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {

            binding.addPhoto.setOnClickListener {onAddImageClicked()}

        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Bitmap>() {
        override fun areItemsTheSame(oldItem: Bitmap, newItem: Bitmap) =
            oldItem == newItem

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Bitmap, newItem: Bitmap) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == ITEM_TYPE_CONTENT){
            return GalleryViewHolder(
                ItemGalleryImagesBinding
                    .bind(LayoutInflater.from(parent.context).inflate(R.layout.item_gallery_images, parent, false))
            )
        }else{
            return AddImageViewHolder(
                ItemAddImageBinding
                    .bind( LayoutInflater.from(parent.context).inflate(R.layout.item_add_image, parent, false))
            )
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder.itemViewType == ITEM_TYPE_CONTENT){

            (holder as GalleryViewHolder).bind(getItem(position), position)

        }else{

            (holder as AddImageViewHolder).bind()

        }

    }

    override fun getItemCount(): Int {
        return (super.getItemCount()?: 0) + 1
    }

    override fun getItemViewType(position: Int): Int {

        return if (position == itemCount - 1) {
            ITEM_TYPE_BUTTON
        } else {
            ITEM_TYPE_CONTENT
        }
    }


}