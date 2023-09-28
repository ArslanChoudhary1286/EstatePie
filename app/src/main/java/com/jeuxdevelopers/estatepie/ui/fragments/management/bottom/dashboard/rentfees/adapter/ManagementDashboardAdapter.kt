package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.rentfees.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemManagementDashboardBinding
import com.jeuxdevelopers.estatepie.models.management.ManagementDashboardModel
import com.jeuxdevelopers.estatepie.utils.ToastUtility


class ManagementDashboardAdapter(
    private val onItemClicked: (ManagementDashboardModel, Int) -> Unit,
) :
    ListAdapter<ManagementDashboardModel, ManagementDashboardAdapter.AdsGridViewHolder>(DiffCallback) {

    val viewList = ArrayList<View>()

    inner class AdsGridViewHolder(private val binding: ItemManagementDashboardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ManagementDashboardModel) {

            binding.root.setOnClickListener {
                onItemClicked(item, adapterPosition)

                updateView(adapterPosition, binding.root.context)

            }

            binding.textView.text = item.title

            Glide.with(binding.root).load(item.image)
                .centerCrop()
                .placeholder(R.drawable.ic_house_gray)
                .error(R.drawable.ic_house_gray)
                .dontAnimate()
                .into(binding.rentsFeesImg)

        }
    }

    fun updateView(adapterPosition : Int, contex: Context) {

        for(n in 0 until viewList.size){

            if (n == adapterPosition){

                viewList[n].setBackgroundResource(R.drawable.ic_ripple_blue_12)

            }else{

                viewList[n].setBackgroundResource(R.drawable.ic_ripple_white_12)

            }
        }

    }

    private object DiffCallback : DiffUtil.ItemCallback<ManagementDashboardModel>() {
        override fun areItemsTheSame(oldItem: ManagementDashboardModel, newItem: ManagementDashboardModel) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: ManagementDashboardModel, newItem: ManagementDashboardModel) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsGridViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_management_dashboard, parent, false)
        return AdsGridViewHolder(
            ItemManagementDashboardBinding.bind(view)
        )
    }

    override fun onBindViewHolder(holder: AdsGridViewHolder, position: Int) {

        viewList.add(holder.itemView)
        val item = getItem(position)
        holder.bind(item)
    }


}