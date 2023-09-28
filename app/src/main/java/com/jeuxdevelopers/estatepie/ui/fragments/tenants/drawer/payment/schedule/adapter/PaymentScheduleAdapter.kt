package com.jeuxdevelopers.estatepie.ui.fragments.tenants.drawer.payment.schedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemBillsByTenantIdBinding
import com.jeuxdevelopers.estatepie.databinding.ItemSchedulePaymentBinding
import com.jeuxdevelopers.estatepie.databinding.ItemUpcomingBillsBinding
import com.jeuxdevelopers.estatepie.network.responses.tenants.billing.SchedulesPaymentListResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.ListOfBillsResponse
import com.jeuxdevelopers.estatepie.utils.ToastUtility

class PaymentScheduleAdapter(
    private val onEditClicked: (SchedulesPaymentListResponse.Data.Schedule) -> Unit,
    private val onDeleteClicked: (SchedulesPaymentListResponse.Data.Schedule) -> Unit,
) :
    ListAdapter<SchedulesPaymentListResponse.Data.Schedule, PaymentScheduleAdapter.AdsGridViewHolder>(DiffCallback) {

    inner class AdsGridViewHolder(private val binding: ItemSchedulePaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SchedulesPaymentListResponse.Data.Schedule) {

            binding.etEdit.setOnClickListener { onEditClicked(item) }
            binding.imageDelete.setOnClickListener { onDeleteClicked(item) }
            binding.tvTitle2.text = item.title
            binding.tvStartDate.text = item.pick_date

        }
    }


    private object DiffCallback : DiffUtil.ItemCallback<SchedulesPaymentListResponse.Data.Schedule>() {
        override fun areItemsTheSame(oldItem: SchedulesPaymentListResponse.Data.Schedule, newItem: SchedulesPaymentListResponse.Data.Schedule) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: SchedulesPaymentListResponse.Data.Schedule, newItem: SchedulesPaymentListResponse.Data.Schedule) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsGridViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_schedule_payment, parent, false)
        return AdsGridViewHolder(
            ItemSchedulePaymentBinding.bind(view)
        )
    }

    override fun onBindViewHolder(holder: AdsGridViewHolder, position: Int) {

        val item = getItem(position)
        holder.bind(item)

    }


}