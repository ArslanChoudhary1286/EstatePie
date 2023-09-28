package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.allreports.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemAllReportsBinding
import com.jeuxdevelopers.estatepie.databinding.ItemResidentialBinding
import com.jeuxdevelopers.estatepie.databinding.ItemUtilityBillsBinding
import com.jeuxdevelopers.estatepie.network.responses.management.dashboard.AllReportsResponse
import com.jeuxdevelopers.estatepie.network.responses.management.dashboard.MangeUtilitiesResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.AdsPropertiesResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.ResidentialPropertiesResponse

class AllReportsAdapter(
    private val onItemClicked: (AllReportsResponse.Data.Request) -> Unit,
) :
    ListAdapter<AllReportsResponse.Data.Request, AllReportsAdapter.AdsGridViewHolder>(DiffCallback) {

    inner class AdsGridViewHolder(private val binding: ItemAllReportsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: AllReportsResponse.Data.Request) {

            binding.root.setOnClickListener { onItemClicked(item) }

            binding.tvUserName.text = item.name
            binding.tvAddress.text = item.address
            binding.tvStatus.text = item.remaining.toString() + " " + item.status1
            binding.tvPercentage.text = item.percentage + "%"
            binding.progress.setProgress(item.completed)

            Glide.with(binding.root).load(item.image)
                .centerCrop()
                .placeholder(R.drawable.ic_house_gray)
                .error(R.drawable.ic_house_gray)
                .dontAnimate()
                .into(binding.houseImage)

        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<AllReportsResponse.Data.Request>() {
        override fun areItemsTheSame(oldItem: AllReportsResponse.Data.Request, newItem: AllReportsResponse.Data.Request) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: AllReportsResponse.Data.Request, newItem: AllReportsResponse.Data.Request) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsGridViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_all_reports, parent, false)
        return AdsGridViewHolder(
            ItemAllReportsBinding.bind(view)
        )
    }

    override fun onBindViewHolder(holder: AdsGridViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


}