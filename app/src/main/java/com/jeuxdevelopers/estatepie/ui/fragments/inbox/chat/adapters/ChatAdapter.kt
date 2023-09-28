package com.jeuxdevelopers.estatepie.ui.fragments.inbox.chat.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemLeftChatLayoutBinding
import com.jeuxdevelopers.estatepie.databinding.ItemRightChatLayoutBinding
import com.jeuxdevelopers.estatepie.models.chat.message.MessageModel
import com.jeuxdevelopers.estatepie.utils.DateUtils

class ChatAdapter(
    private val currentUserId: Int
//    private val onItemClick:() -> Unit
) :
    ListAdapter<MessageModel, RecyclerView.ViewHolder>(DiffCallback) {

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }
//    private val list = ArrayList<Int>()

    inner class LeftViewHolder(private val binding: ItemLeftChatLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MessageModel){
            binding.root.setOnClickListener {
//                onItemClick()
            }
            binding.messageTxt.text = item.message
            binding.timeTxt.text = DateUtils.getDateTimeFromMillis(item.created_at)
        }
    }

    inner class RightViewHolder(private val binding: ItemRightChatLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MessageModel){
            binding.root.setOnClickListener {
//                onItemClick()
            }
            binding.messageTxt.text = item.message
            binding.timeTxt.text = DateUtils.getDateTimeFromMillis(item.created_at)
        }
    }


    private object DiffCallback : DiffUtil.ItemCallback<MessageModel>() {
        override fun areItemsTheSame(oldItem: MessageModel, newItem: MessageModel) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: MessageModel, newItem: MessageModel) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == VIEW_TYPE_ONE) {
            return LeftViewHolder(
                ItemLeftChatLayoutBinding.bind(LayoutInflater.from(parent.context).inflate(R.layout.item_left_chat_layout, parent, false))
            )
        }else{
            return RightViewHolder(
                ItemRightChatLayoutBinding.bind(LayoutInflater.from(parent.context).inflate(R.layout.item_right_chat_layout, parent, false))
            )
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        if (item.sender_id == currentUserId) {
            (holder as RightViewHolder).bind(item)

        } else {
            (holder as LeftViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {

        val item = getItem(position)
        if (item.sender_id == currentUserId)
            return VIEW_TYPE_TWO
        else
            return VIEW_TYPE_ONE

    }


}