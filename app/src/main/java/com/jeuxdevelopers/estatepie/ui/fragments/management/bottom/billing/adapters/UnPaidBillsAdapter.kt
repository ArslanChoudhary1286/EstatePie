package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.billing.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemUnpaidBillsBinding
import com.jeuxdevelopers.estatepie.network.responses.management.billing.PaidBillsResponse
import com.jeuxdevelopers.estatepie.network.responses.management.billing.UnPaidBillsResponse

class UnPaidBillsAdapter(
    private val onItemClicked: (UnPaidBillsResponse.Data.Bill) -> Unit,
) :
    ListAdapter<UnPaidBillsResponse.Data.Bill, UnPaidBillsAdapter.AdsGridViewHolder>(DiffCallback) {


    inner class AdsGridViewHolder(private val binding: ItemUnpaidBillsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UnPaidBillsResponse.Data.Bill) {

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


    private object DiffCallback : DiffUtil.ItemCallback<UnPaidBillsResponse.Data.Bill>() {
        override fun areItemsTheSame(oldItem: UnPaidBillsResponse.Data.Bill, newItem: UnPaidBillsResponse.Data.Bill) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: UnPaidBillsResponse.Data.Bill, newItem: UnPaidBillsResponse.Data.Bill) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsGridViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_unpaid_bills, parent, false)
        return AdsGridViewHolder(
            ItemUnpaidBillsBinding.bind(view)
        )
    }

    override fun onBindViewHolder(holder: AdsGridViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
    }


}