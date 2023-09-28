package com.jeuxdevelopers.estatepie.ui.fragments.inbox.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentChatBinding
import com.jeuxdevelopers.estatepie.models.chat.inbox.InboxModel
import com.jeuxdevelopers.estatepie.models.chat.message.MessageModel
import com.jeuxdevelopers.estatepie.ui.dialogs.location.LocationPickerDialog
import com.jeuxdevelopers.estatepie.ui.fragments.inbox.adapters.InboxAdapter
import com.jeuxdevelopers.estatepie.ui.fragments.inbox.chat.adapters.ChatAdapter
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.tenants.adapters.TenantsAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Objects

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    private val viewModel: ChatViewModel by viewModels()

    private val chatReference: CollectionReference =
        Firebase.firestore.collection(AppConsts.CHAT_COLLECTION)

    private lateinit var adapter: ChatAdapter

    var docsId: String = ""

        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

        initTenantsRecycler()

        setChatRecycler()

        setBundleData()

    }

    private fun setBundleData() {

        val userName = arguments?.getString(AppConsts.USER_NAME, "")
        val userImage = arguments?.getString(AppConsts.USER_IMAGE, "")
        val propertyName = arguments?.getString(AppConsts.PROPERTY_NAME, "")

        binding.tvName.text = userName
        binding.tvUserStatus.text = propertyName

        Glide.with(requireContext()).load(userImage)
            .centerCrop()
            .placeholder(R.drawable.ic_house_gray)
            .error(R.drawable.ic_house_gray)
            .dontAnimate()
            .into(binding.profileImage)
    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener{ findNavController().navigateUp() }

        binding.sendImg.setOnClickListener { sendTextMessage() }

    }

    private fun sendTextMessage() {

        val message:String = binding.etSendMsg.text.toString()

        if (message.isNotEmpty()){

            viewModel.messageModel.message = message
            viewModel.messageModel.chat_uid = "/"+AppConsts.CHAT_COLLECTION+"/" + message
            viewModel.messageModel.created_at = System.currentTimeMillis()
            viewModel.messageModel.deleted_at = false
            viewModel.messageModel.sender_id = viewModel.getCurrentUser()!!.id
            viewModel.messageModel.sender_name = viewModel.getCurrentUser()!!.name
            viewModel.messageModel.sender_image = viewModel.getCurrentUser()!!.details.image
            viewModel.messageModel.type = "message"
//            viewModel.messageModel.is_read = false

            chatReference
                .document(docsId).collection(AppConsts.MESSAGES_COLLECTION)
                .add(viewModel.messageModel).isSuccessful

            // update last message

            chatReference.document(docsId).update(AppConsts.MESSAGE_TIME, System.currentTimeMillis())
            chatReference.document(docsId).update(AppConsts.LAST_MESSAGE, message)

            binding.etSendMsg.text = null

        }else{
            ToastUtility.errorToast(requireContext(), "Message required.")
        }

    }

    private fun setChatRecycler() {

        docsId = arguments?.getString(AppConsts.DOCS_ID).toString()

        chatReference
            .document(docsId).collection(AppConsts.MESSAGES_COLLECTION)
            .orderBy(AppConsts.MESSAGE_TIME, Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    Timber.d("setChatRecycler: error -> ${error.message}")
                } else {
                    if (querySnapshot != null && !querySnapshot.isEmpty) {

                        Timber.d("setChatRecycler: data -> ${querySnapshot.documents.toString()}")

//                        viewModel.chatList.clear()
//                        viewModel.chatList.addAll(querySnapshot.toObjects(MessageModel::class.java))
                        adapter.submitList(querySnapshot.toObjects())
                        binding.recyclerChat.smoothScrollToPosition(0)

                        // inboxList.clear()
                        // inboxAdapter.notifyDataSetChanged()
//                        for (documentChange in querySnapshot.documentChanges) {
//                            val oldIndex: Int = documentChange.oldIndex
//                            val newIndex: Int = documentChange.newIndex
//                            when (documentChange.type) {
//                                DocumentChange.Type.ADDED -> {
//                                    val newInbox: InboxModel =
//                                        documentChange.document
//                                            .toObject(InboxModel::class.java)
//                                    inboxList.add(newIndex, newInbox)
//                                    inboxAdapter.notifyItemInserted(newIndex)
//                                }
//                                DocumentChange.Type.MODIFIED -> {
//                                    val modifiedInbox: InboxModel =
//                                        documentChange.document.toObject(
//                                            InboxModel::class.java
//                                        )
//                                    inboxList[oldIndex] = modifiedInbox
//                                    inboxAdapter.notifyItemChanged(newIndex)
//                                }
//                                else -> {
//
//                                }
//                            }
//                        }
                    } else {
                        Log.d(LocationPickerDialog.TAG, "setInboxRecycler: snapshots are empty")
                    }
                }
            }
    }

    private fun initTenantsRecycler() {

        adapter = ChatAdapter(viewModel.getCurrentUser()!!.id)
        binding.recyclerChat.adapter = adapter

    }

}