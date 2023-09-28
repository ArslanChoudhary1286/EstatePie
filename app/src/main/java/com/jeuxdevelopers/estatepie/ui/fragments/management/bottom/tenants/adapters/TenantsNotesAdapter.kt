package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.tenants.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemNotesBinding
import com.jeuxdevelopers.estatepie.databinding.ItemTenantsBinding
import com.jeuxdevelopers.estatepie.network.responses.management.tenants.MyTenantsResponse
import com.jeuxdevelopers.estatepie.network.responses.management.tenants.TenantsByIdResponse

class TenantsNotesAdapter(
    private val onItemClicked: (TenantsByIdResponse.Data.Note, pos:Int) -> Unit,
) :
    ListAdapter<TenantsByIdResponse.Data.Note, TenantsNotesAdapter.AdsGridViewHolder>(DiffCallback) {


    inner class AdsGridViewHolder(private val binding: ItemNotesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TenantsByIdResponse.Data.Note) {

            binding.crossImage.setOnClickListener { onItemClicked(item, adapterPosition) }
            binding.adapterTxtView.text = item.message

        }
    }


    private object DiffCallback : DiffUtil.ItemCallback<TenantsByIdResponse.Data.Note>() {
        override fun areItemsTheSame(oldItem: TenantsByIdResponse.Data.Note, newItem: TenantsByIdResponse.Data.Note) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: TenantsByIdResponse.Data.Note, newItem: TenantsByIdResponse.Data.Note) =
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