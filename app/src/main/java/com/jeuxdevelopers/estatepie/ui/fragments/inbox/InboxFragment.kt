package com.jeuxdevelopers.estatepie.ui.fragments.inbox

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentInboxBinding
import com.jeuxdevelopers.estatepie.models.chat.inbox.InboxModel
import com.jeuxdevelopers.estatepie.ui.dialogs.location.LocationPickerDialog.LocationPickerDialog.TAG
import com.jeuxdevelopers.estatepie.ui.fragments.inbox.adapters.InboxAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class InboxFragment : Fragment() {

    private lateinit var binding: FragmentInboxBinding

    private val viewModel: InboxViewModel by viewModels()

    private lateinit var adapter: InboxAdapter

    private val inboxRefrencec: CollectionReference =
        Firebase.firestore.collection(AppConsts.CHAT_COLLECTION)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInboxBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

        initCollectors()

        initInboxRecycler()

        setInboxRecycler()

        initTextWatchers()

    }

    private fun initCollectors() {

        if(viewModel.getCurrentUser()?.role_id == 5){

            viewModel.userTypeIdField = "tenant_id"
            viewModel.userType = AppConsts.TENANT

        }else if (viewModel.getCurrentUser()?.role_id == 4){

            viewModel.userTypeIdField = "landlord_id"
            viewModel.userType = AppConsts.LANDLORD

        }
    }

    fun setInboxRecycler() {

        inboxRefrencec
            .orderBy(AppConsts.MESSAGE_TIME, Query.Direction.DESCENDING)
            .whereEqualTo(viewModel.userTypeIdField, viewModel.getCurrentUser()!!.id)
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    Timber.d("setInboxRecycler: error -> ${error.message}")
                } else {
                    if (querySnapshot != null && !querySnapshot.isEmpty) {

                        Timber.d("setInboxRecycler: data -> ${querySnapshot.documents.toString()}")

                        viewModel.inboxList.clear()
                        viewModel.inboxList.addAll(querySnapshot.toObjects(InboxModel::class.java))
                        adapter.submitList(viewModel.inboxList)
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
                        Log.d(TAG, "setInboxRecycler: snapshots are empty")
                    }
                }
            }
    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener{ findNavController().navigateUp() }

    }

    private fun initTextWatchers() {

        binding.etSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) = Unit

            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) = Unit

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {

                if (count > 0){
                    viewModel.inboxFilteredList.clear()

                    viewModel.inboxList.forEachIndexed{index, data ->

                        if (viewModel.userType == AppConsts.TENANT){

                            if (data.property_name.lowercase().contains(sequence.toString().lowercase()) ||
                                data.landlord_name.lowercase().contains(sequence.toString().lowercase())){

                                viewModel.inboxFilteredList.add(data)

                            }

                        }else{

                            if (data.property_name.lowercase().contains(sequence.toString().lowercase()) ||
                                data.tenant_name.lowercase().contains(sequence.toString().lowercase())){

                                viewModel.inboxFilteredList.add(data)

                            }

                        }

                    }
                    adapter.submitList(viewModel.inboxFilteredList)

                }else{

                    adapter.submitList(viewModel.inboxList)

                }

                adapter.notifyDataSetChanged()

            }

        })
    }

    private fun initInboxRecycler() {

        adapter = InboxAdapter(viewModel.userType,::onItemClicked)

        binding.recyclerInbox.adapter = adapter

    }

    private fun onItemClicked(item: InboxModel) {

        val bundle = Bundle()
        bundle.putString(AppConsts.DOCS_ID, item.Id)
        if (viewModel.userType == AppConsts.LANDLORD){
            bundle.putString(AppConsts.USER_NAME, item.tenant_name)
            bundle.putString(AppConsts.USER_IMAGE, item.tenant_image)
        }else{
            bundle.putString(AppConsts.USER_NAME, item.landlord_name)
            bundle.putString(AppConsts.USER_IMAGE, item.landlord_image)

        }
        bundle.putString(AppConsts.PROPERTY_NAME, item.property_name)

        if (requireArguments().getString("data","") == "tenants"){

            findNavController().navigate(R.id.action_inboxFragment2_to_chatFragment2, bundle)

        }
        else{

            findNavController().navigate(R.id.action_inboxFragment_to_chatFragment, bundle)

        }

//        val bundle = Bundle()
//        bundle.putString(AppConsts.INTENT_ID, item.ref_id.toString())

    }

}