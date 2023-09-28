package com.jeuxdevelopers.estatepie.ui.fragments.notification.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemNotificationBinding
import com.jeuxdevelopers.estatepie.network.responses.management.properties.ResidentialPropertiesResponse
import com.jeuxdevelopers.estatepie.network.responses.notification.GetNotificationsResponse

class NotificationAdapter(
    private val onItemClicked: (GetNotificationsResponse.Data.Notification) -> Unit,
    private val onReadClicked: (GetNotificationsResponse.Data.Notification) -> Unit,
) :
    ListAdapter<GetNotificationsResponse.Data.Notification, NotificationAdapter.AdsGridViewHolder>(DiffCallback) {


    inner class AdsGridViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(item: GetNotificationsResponse.Data.Notification) {

            binding.root.setOnClickListener {

                if (item.is_read == 0){
                    binding.constraintLayout.setBackgroundResource(R.drawable.notification_bg)
                    onReadClicked(item)
                }

                onItemClicked(item)

            }

            if (item.is_read == 0){
                binding.constraintLayout.setBackgroundResource(R.drawable.notification_bg)
            }else{
                binding.constraintLayout.setBackgroundResource(0)
            }

            Glide.with(binding.root).load(item.image)
                .centerCrop()
                .placeholder(R.drawable.ic_house_gray)
                .error(R.drawable.ic_house_gray)
                .dontAnimate()
                .into(binding.notificationImage)

            binding.notificationName.text = item.message
            binding.notificationTime.text = item.created_at

        }
    }


    private object DiffCallback : DiffUtil.ItemCallback<GetNotificationsResponse.Data.Notification>() {
        override fun areItemsTheSame(oldItem: GetNotificationsResponse.Data.Notification, newItem: GetNotificationsResponse.Data.Notification) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: GetNotificationsResponse.Data.Notification, newItem: GetNotificationsResponse.Data.Notification) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsGridViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return AdsGridViewHolder(
            ItemNotificationBinding.bind(view)
        )
    }

    override fun onBindViewHolder(holder: AdsGridViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


}