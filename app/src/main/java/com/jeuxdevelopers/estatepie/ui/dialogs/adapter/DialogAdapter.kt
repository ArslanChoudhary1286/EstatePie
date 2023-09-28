package com.jeuxdevelopers.estatepie.ui.dialogs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeuxdevelopers.estatepie.databinding.ItemDialogInputPropertiesBinding

class DialogAdapter(
    private val onDialogItemClicked: (String, Int) -> Unit,
) :
    ListAdapter<String, DialogAdapter.ImageViewHolder>(DiffCallback) {

    inner class ImageViewHolder(private val binding: ItemDialogInputPropertiesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {

            binding.root.setOnClickListener {
                onDialogItemClicked(item, adapterPosition)
            }

            binding.tvYes.text = item
        }

    }

    private object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemDialogInputPropertiesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        val item = getItem(position)

        holder.bind(item)
    }

}