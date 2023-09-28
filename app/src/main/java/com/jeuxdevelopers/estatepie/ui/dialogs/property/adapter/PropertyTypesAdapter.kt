package com.jeuxdevelopers.estatepie.ui.dialogs.property.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.jeuxdevelopers.estatepie.databinding.ItemPropertyTypeBinding
import com.jeuxdevelopers.estatepie.network.responses.management.properties.PropertyTypeResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.others.PropertyTypesResponse

class PropertyTypesAdapter(
    private val onTypeSelect: (type: PropertyTypeResponse.Data) -> Unit
) :
    ListAdapter<PropertyTypeResponse.Data, PropertyTypesAdapter.PropertyTypesViewHolder>(
        DiffCallback
    ) {


    inner class PropertyTypesViewHolder(val binding: ItemPropertyTypeBinding) :
        ViewHolder(binding.root) {
        fun bind(item: PropertyTypeResponse.Data) {
            binding.apply {
                tvBungalow.text = item.name
            }

            binding.root.setOnClickListener {
                onTypeSelect.invoke(item)
            }
        }
    }


    private object DiffCallback : DiffUtil.ItemCallback<PropertyTypeResponse.Data>() {
        override fun areItemsTheSame(
            oldItem: PropertyTypeResponse.Data,
            newItem: PropertyTypeResponse.Data
        ) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: PropertyTypeResponse.Data,
            newItem: PropertyTypeResponse.Data
        ) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyTypesViewHolder {
        return PropertyTypesViewHolder(
            ItemPropertyTypeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PropertyTypesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}