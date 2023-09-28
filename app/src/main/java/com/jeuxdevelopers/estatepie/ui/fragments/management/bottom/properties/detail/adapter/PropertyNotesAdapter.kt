package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemNotesBinding
import com.jeuxdevelopers.estatepie.databinding.ItemTenantsBinding
import com.jeuxdevelopers.estatepie.network.responses.management.properties.PropertiesDetailsResponse
import com.jeuxdevelopers.estatepie.network.responses.management.tenants.MyTenantsResponse
import com.jeuxdevelopers.estatepie.network.responses.management.tenants.TenantsByIdResponse

class PropertyNotesAdapter(
    private val onItemClicked: (PropertiesDetailsResponse.Data.Note, pos:Int) -> Unit,
) :
    ListAdapter<PropertiesDetailsResponse.Data.Note, PropertyNotesAdapter.AdsGridViewHolder>(DiffCallback) {


    inner class AdsGridViewHolder(private val binding: ItemNotesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PropertiesDetailsResponse.Data.Note) {

            binding.crossImage.setOnClickListener { onItemClicked(item, adapterPosition) }
            binding.adapterTxtView.text = item.message

        }
    }


    private object DiffCallback : DiffUtil.ItemCallback<PropertiesDetailsResponse.Data.Note>() {
        override fun areItemsTheSame(oldItem: PropertiesDetailsResponse.Data.Note, newItem: PropertiesDetailsResponse.Data.Note) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: PropertiesDetailsResponse.Data.Note, newItem: PropertiesDetailsResponse.Data.Note) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsGridViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_notes, parent, false)
        return AdsGridViewHolder(
            ItemNotesBinding.bind(view)
        )
    }

    override fun onBindViewHolder(holder: AdsGridViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
    }


}