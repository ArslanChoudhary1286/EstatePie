package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.tenants.profile

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentTenantsProfileBinding
import com.jeuxdevelopers.estatepie.network.responses.management.tenants.TenantsByIdResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.tenants.adapters.TenantsNotesAdapter
import com.jeuxdevelopers.estatepie.ui.listeners.ManagementDrawerListener
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.notify
import timber.log.Timber

@AndroidEntryPoint
class TenantsProfileFragment : Fragment() {

    private lateinit var binding: FragmentTenantsProfileBinding

    private val viewModel: TenantsProfileViewModel  by viewModels()

    var adapter = TenantsNotesAdapter(::onItemClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTenantsProfileBinding.inflate(inflater, container, false)

        initCollectors()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getTenantProfileData()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.tenantsByIdUiState){
            when(it){
                is NetworkResult.Error -> {
                    waitingDialog.dismiss()
                    Timber.d("Error -> ${it.message}, ${it.result.toString()}")
                    ToastUtility.errorToast(requireContext(), it.message)
                }
                is NetworkResult.Idle -> {
                    //nothing to do this is just idle state
                }
                is NetworkResult.Loading -> {
                    waitingDialog.show(status = "Loading...")
                    Timber.d("explore ads")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

//                    adapter.submitList(it.result?.data?.tenants)
                    setDataToUi(it.result?.data)

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.unAssigntenantsUiState){
            when(it){
                is NetworkResult.Error -> {
                    waitingDialog.dismiss()
                    Timber.d("Error -> ${it.message}, ${it.result.toString()}")
                    ToastUtility.errorToast(requireContext(), it.message)
                }
                is NetworkResult.Idle -> {
                    //nothing to do this is just idle state
                }
                is NetworkResult.Loading -> {
                    waitingDialog.show(status = "Loading...")
                    Timber.d("explore ads")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

                    ToastUtility.successToast(requireContext(), it.result?.message)
                    findNavController().navigateUp()

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.leaseReminderUiState){
            when(it){
                is NetworkResult.Error -> {
                    waitingDialog.dismiss()
                    Timber.d("Error -> ${it.message}, ${it.result.toString()}")
                    ToastUtility.errorToast(requireContext(), it.message)
                }
                is NetworkResult.Idle -> {
                    //nothing to do this is just idle state
                }
                is NetworkResult.Loading -> {
                    waitingDialog.show(status = "Loading...")
                    Timber.d("explore ads")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

                    ToastUtility.successToast(requireContext(), it.result?.message)

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.addNoteToTenantsUiState){
            when(it){
                is NetworkResult.Error -> {
                    waitingDialog.dismiss()
                    Timber.d("Error -> ${it.message}, ${it.result.toString()}")
                    ToastUtility.errorToast(requireContext(), it.message)
                }
                is NetworkResult.Idle -> {
                    //nothing to do this is just idle state
                }
                is NetworkResult.Loading -> {
                    waitingDialog.show(status = "Loading...")
                    Timber.d("explore ads")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

                    ToastUtility.successToast(requireContext(), it.result?.message)
                    Timber.d("Note Data -> ${it.result?.data}")
                    getTenantProfileData()

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.deleteNotesByIdUiState){
            when(it){
                is NetworkResult.Error -> {
                    waitingDialog.dismiss()
                    Timber.d("Error -> ${it.message}, ${it.result.toString()}")
                    ToastUtility.errorToast(requireContext(), "Error : ${it.message}, ${it.result.toString()}")

                }
                is NetworkResult.Idle -> {
                    //nothing to do this is just idle state
                }
                is NetworkResult.Loading -> {
                    waitingDialog.show(status = "Loading...")
                    Timber.d("explore ads")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

                    getTenantProfileData()
                    ToastUtility.successToast(requireContext(), it.result?.message)

                }
            }
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun getTenantProfileData() {

        if (arguments != null){
            viewModel.getTenantsByIdRequest.id = arguments?.getString(AppConsts.PROFILE_ID, "").toString()
            viewModel.getTenantsById()
        }

    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener{ findNavController().navigateUp() }

        binding.btnLeaseReminder.setOnClickListener { leaseReminder() }

        binding.btnUnAssignProperty.setOnClickListener { unAssignProperty() }

        binding.btnAddNotes.setOnClickListener { addNotes(binding.etNotes.text.toString()) }

    }

    private fun setDataToUi(data: TenantsByIdResponse.Data?) {

        Glide.with(requireContext()).load(data?.image_url)
            .centerCrop()
            .placeholder(R.drawable.ic_house_gray)
            .error(R.drawable.ic_house_gray)
            .dontAnimate()
            .into(binding.profileImage)
        binding.tvUserName.text = data?.name
        binding.tvHouseNo.text = data?.property_name
        binding.tvDueDate.text = data?.lease_info
        binding.tvMoveDate.text = data?.move_in_date
//        binding.tvNotes.text = data?.notes!![0].message
        adapter.submitList(data?.notes)
        binding.notesRecycler.adapter = adapter

        viewModel.userId = data?.id.toString()
        viewModel.propertyId = data?.property_id.toString()

    }

    private fun addNotes(notes: String) {

        viewModel.addNoteToTenantsRequest.tenant_id = viewModel.userId
//        viewModel.addNoteToTenantsRequest.tenant_id = viewModel.getTenantsByIdRequest.id.toInt()
        viewModel.addNoteToTenantsRequest.property_id = viewModel.propertyId
        viewModel.addNoteToTenantsRequest.message = notes
        viewModel.addNoteToTenantsRequest.type = "property"
        Timber.d("Notes -----> " + viewModel.addNoteToTenantsRequest)
        viewModel.addNotes()
        binding.etNotes.text = null

    }

    private fun unAssignProperty() {

        viewModel.unassignPropertyRequest.user_id = viewModel.userId
        viewModel.unassignPropertyRequest.property_id = viewModel.propertyId
        viewModel.unAssignTenantProperty()

    }

    private fun leaseReminder() {

        viewModel.leaseReminderRequest.user_id = viewModel.userId
        viewModel.leaseReminderRequest.property_id = viewModel.propertyId
        viewModel.leaseReminder()

    }

    private fun onItemClicked(item : TenantsByIdResponse.Data.Note, position: Int){

        deleteNote(item.id)

    }

    private fun deleteNote(id: Int) {

        viewModel.delNotesByIdRequest.id = id.toString()
        viewModel.deleteNotes()

    }

}