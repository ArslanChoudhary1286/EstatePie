package com.jeuxdevelopers.estatepie.ui.fragments.inbox.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemInboxBinding
import com.jeuxdevelopers.estatepie.models.chat.inbox.InboxModel
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.DateUtils.getTimeAgo

class InboxAdapter(
    private val data: String,
    private val onItemClick:(InboxModel) -> Unit
) :
    ListAdapter<InboxModel, InboxAdapter.AdsGridViewHolder>(DiffCallback) {

    inner class AdsGridViewHolder(private val binding: ItemInboxBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: InboxModel){
            binding.root.setOnClickListener {
                onItemClick(item)
            }

            binding.onlineStatusTxt.text = getTimeAgo(item.created_at)

            if (data.equals(AppConsts.LANDLORD)){

                binding.lastMsg.text = item.last_msg
                binding.nameTxt.text = item.tenant_name

                Glide.with(binding.root).load(item.tenant_image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_house_gray)
                    .error(R.drawable.ic_house_gray)
                    .dontAnimate()
                    .into(binding.circleImageView)

            }else{

                binding.lastMsg.text = item.last_msg
                binding.nameTxt.text = item.landlord_name

                Glide.with(binding.root).load(item.landlord_image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_house_gray)
                    .error(R.drawable.ic_house_gray)
                    .dontAnimate()
                    .into(binding.circleImageView)

            }

        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<InboxModel>() {
        override fun areItemsTheSame(oldItem: InboxModel, newItem: InboxModel) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: InboxModel, newItem: InboxModel) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsGridViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_inbox, parent, false)
        return AdsGridViewHolder(
            ItemInboxBinding.bind(view)
        )
    }

    override fun onBindViewHolder(holder: AdsGridViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}