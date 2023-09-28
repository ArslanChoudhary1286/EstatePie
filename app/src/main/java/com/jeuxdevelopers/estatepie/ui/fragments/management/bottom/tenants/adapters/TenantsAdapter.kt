package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.tenants.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemTenantsBinding
import com.jeuxdevelopers.estatepie.network.responses.management.tenants.MyTenantsResponse

class TenantsAdapter(
    private val onItemClicked: (MyTenantsResponse.Data.Tenant) -> Unit,
) :
    ListAdapter<MyTenantsResponse.Data.Tenant, TenantsAdapter.AdsGridViewHolder>(DiffCallback) {


    inner class AdsGridViewHolder(private val binding: ItemTenantsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyTenantsResponse.Data.Tenant) {

            binding.chipTime.setOnClickListener { onItemClicked(item) }
            binding.tvAddress.text = item.address
            binding.tvUserName.text = item.name
            val item1 = item.name
//            binding.tvHouseNumber.text = item1.substring(2, 5)
//            binding.tvHouseNumber.text = item1.substring(item1.indexOf("(") + 1, item1.indexOf(")"))
//            binding.tvAmount.text = item.price.toString()
//            binding.chipTime.text = item.created_on
            Glide.with(binding.root).load(item.image)
                .centerCrop()
                .placeholder(R.drawable.ic_house_gray)
                .error(R.drawable.ic_house_gray)
                .dontAnimate()
                .into(binding.houseImage);

        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<MyTenantsResponse.Data.Tenant>() {
        override fun areItemsTheSame(oldItem: MyTenantsResponse.Data.Tenant, newItem: MyTenantsResponse.Data.Tenant) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: MyTenantsResponse.Data.Tenant, newItem: MyTenantsResponse.Data.Tenant) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsGridViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_tenants, parent, false)
        return AdsGridViewHolder(
            ItemTenantsBinding.bind(view)
        )
    }

    override fun onBindViewHolder(holder: AdsGridViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
    }


}