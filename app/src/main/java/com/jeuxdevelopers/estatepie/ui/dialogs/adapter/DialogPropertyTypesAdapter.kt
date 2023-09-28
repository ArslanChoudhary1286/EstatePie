package com.jeuxdevelopers.estatepie.ui.dialogs.adapter

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemDialogInputPropertiesBinding
import com.jeuxdevelopers.estatepie.network.responses.management.properties.PropertyTypeResponse
import com.jeuxdevelopers.estatepie.utils.ToastUtility

class DialogPropertyTypesAdapter(private val dialog: Dialog, private val inputText: MaterialTextView
) :
    ListAdapter<PropertyTypeResponse.Data, DialogPropertyTypesAdapter.ImageViewHolder>(DiffCallback) {


    inner class ImageViewHolder(private val binding: ItemDialogInputPropertiesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PropertyTypeResponse.Data) {

            binding.root.setOnClickListener {
                // this value set to view
                inputText.text = item.name



                dialog.dismiss()

            }
            // this value set to adapter view
            binding.tvYes.text = item.name

        }

    }


    private object DiffCallback : DiffUtil.ItemCallback<PropertyTypeResponse.Data>() {
        override fun areItemsTheSame(oldItem: PropertyTypeResponse.Data, newItem: PropertyTypeResponse.Data) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: PropertyTypeResponse.Data, newItem: PropertyTypeResponse.Data) =
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