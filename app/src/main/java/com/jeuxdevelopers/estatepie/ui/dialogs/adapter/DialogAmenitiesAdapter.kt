package com.jeuxdevelopers.estatepie.ui.dialogs.adapter

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemDialogAmenitiesBinding
import com.jeuxdevelopers.estatepie.databinding.ItemDialogInputPropertiesBinding
import com.jeuxdevelopers.estatepie.network.responses.management.properties.AmenititesResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.PropertyTypeResponse
import com.jeuxdevelopers.estatepie.utils.ToastUtility

class DialogAmenitiesAdapter(
) :
    ListAdapter<AmenititesResponse.Data, DialogAmenitiesAdapter.ImageViewHolder>(DiffCallback) {

    var amenitiesName: MutableList<String> = mutableListOf()
    var amenitiesId: MutableList<String> = mutableListOf()


    inner class ImageViewHolder(private val binding: ItemDialogAmenitiesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AmenititesResponse.Data) {

            amenitiesName.add("")
            amenitiesId.add("")

            binding.amenitiesBox.setText(item.name)

            binding.amenitiesBox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener{view, id ->

                if (view.isChecked){

                    amenitiesName.set(adapterPosition, item.name)
                    amenitiesId.set(adapterPosition, item.id.toString())

                }else{

                    amenitiesName.set(adapterPosition, "")
                    amenitiesId.set(adapterPosition, "")

                }

            })

        }

    }

    private object DiffCallback : DiffUtil.ItemCallback<AmenititesResponse.Data>() {
        override fun areItemsTheSame(oldItem: AmenititesResponse.Data, newItem: AmenititesResponse.Data) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: AmenititesResponse.Data, newItem: AmenititesResponse.Data) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemDialogAmenitiesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        val item = getItem(position)

        holder.bind(item)
    }


}