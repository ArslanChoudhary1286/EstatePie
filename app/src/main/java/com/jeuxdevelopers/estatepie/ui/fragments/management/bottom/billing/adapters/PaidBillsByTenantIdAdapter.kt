package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.billing.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemBillsByTenantIdBinding
import com.jeuxdevelopers.estatepie.databinding.ItemPaidBillsBinding
import com.jeuxdevelopers.estatepie.network.responses.management.billing.PaidBillByTenantIdResponse
import com.jeuxdevelopers.estatepie.network.responses.management.billing.PaidBillsResponse
import com.jeuxdevelopers.estatepie.network.responses.management.billing.UnPaidBillByTenantIdResponse
import com.jeuxdevelopers.estatepie.network.responses.management.tenants.MyTenantsResponse

class PaidBillsByTenantIdAdapter(
    private val onItemClicked: (PaidBillByTenantIdResponse.Data.Bill) -> Unit,
    private val onDeleteClick: (PaidBillByTenantIdResponse.Data.Bill) -> Unit,
) :
    ListAdapter<PaidBillByTenantIdResponse.Data.Bill, PaidBillsByTenantIdAdapter.AdsGridViewHolder>(DiffCallback) {


    inner class AdsGridViewHolder(private val binding: ItemBillsByTenantIdBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PaidBillByTenantIdResponse.Data.Bill) {

            binding.dialogCross.setOnClickListener { onDeleteClick(item) }
            binding.root.setOnClickListener { onItemClicked(item) }
            binding.tvBillName.text = item.property_name
            binding.tvBillAmount.text =  "$" + item.amount.toString()
            binding.tvStatus.text = item.status
            Glide.with(binding.root).load(item.image)
                .centerCrop()
                .placeholder(R.drawable.ic_house_gray)
                .error(R.drawable.ic_house_gray)
                .dontAnimate()
                .into(binding.iconImage);

        }
    }


    private object DiffCallback : DiffUtil.ItemCallback<PaidBillByTenantIdResponse.Data.Bill>() {
        override fun areItemsTheSame(oldItem: PaidBillByTenantIdResponse.Data.Bill, newItem: PaidBillByTenantIdResponse.Data.Bill) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: PaidBillByTenantIdResponse.Data.Bill, newItem: PaidBillByTenantIdResponse.Data.Bill) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsGridViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_bills_by_tenant_id, parent, false)
        return AdsGridViewHolder(
            ItemBillsByTenantIdBinding.bind(view)
        )
    }

    override fun onBindViewHolder(holder: AdsGridViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
    }


}