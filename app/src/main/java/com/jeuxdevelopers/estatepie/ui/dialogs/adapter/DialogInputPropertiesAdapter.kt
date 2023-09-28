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
import com.jeuxdevelopers.estatepie.utils.ToastUtility

class DialogInputPropertiesAdapter(private val dialog: Dialog, private val inputText: MaterialTextView,
                                   private val inputText1: MaterialTextView?
) :
    ListAdapter<String, DialogInputPropertiesAdapter.ImageViewHolder>(DiffCallback) {


    inner class ImageViewHolder(private val binding: ItemDialogInputPropertiesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {

            binding.root.setOnClickListener {
                inputText.text = item


                if (inputText1 != null){

                    if (item == "For Sale" || item == "No" ){

                        inputText1.text = ""
                        inputText1.alpha = 0.4f
                        inputText1.isEnabled = false

                    }else{

                        inputText1.alpha = 1f
                        inputText1.isEnabled = true
                    }

                }

                dialog.dismiss()

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