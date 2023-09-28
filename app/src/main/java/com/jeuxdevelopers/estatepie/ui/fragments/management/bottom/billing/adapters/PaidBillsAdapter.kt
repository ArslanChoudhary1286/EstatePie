package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.billing.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemPaidBillsBinding
import com.jeuxdevelopers.estatepie.network.responses.management.billing.PaidBillsResponse
import com.jeuxdevelopers.estatepie.network.responses.management.tenants.MyTenantsResponse

class PaidBillsAdapter(
    private val onItemClicked: (PaidBillsResponse.Data.Bill) -> Unit,
) :
    ListAdapter<PaidBillsResponse.Data.Bill, PaidBillsAdapter.AdsGridViewHolder>(DiffCallback) {


    inner class AdsGridViewHolder(private val binding: ItemPaidBillsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PaidBillsResponse.Data.Bill) {

            binding.chipTime.setOnClickListener { onItemClicked(item) }
            binding.tvAddress.text = item.address
            binding.tvUserName.text = item.name
            Glide.with(binding.root).load(item.image)
                .centerCrop()
                .placeholder(R.drawable.ic_house_gray)
                .error(R.drawable.ic_house_gray)
                .dontAnimate()
                .into(binding.houseImage);

        }
    }


    private object DiffCallback : DiffUtil.ItemCallback<PaidBillsResponse.Data.Bill>() {
        override fun areItemsTheSame(oldItem: PaidBillsResponse.Data.Bill, newItem: PaidBillsResponse.Data.Bill) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: PaidBillsResponse.Data.Bill, newItem: PaidBillsResponse.Data.Bill) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsGridViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_paid_bills, parent, false)
        return AdsGridViewHolder(
            ItemPaidBillsBinding.bind(view)
        )
    }

    override fun onBindViewHolder(holder: AdsGridViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
    }


}