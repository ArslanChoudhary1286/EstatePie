package com.jeuxdevelopers.estatepie.ui.fragments.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentNotificationBinding
import com.jeuxdevelopers.estatepie.network.responses.notification.GetNotificationsResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.inbox.adapters.InboxAdapter
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.residential.adapters.ResidentialAdapter
import com.jeuxdevelopers.estatepie.ui.fragments.notification.adapter.NotificationAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private lateinit var binding: FragmentNotificationBinding

    private val viewModel: NotificationViewModel by viewModels()

    var adapter = NotificationAdapter(::onItemClicked, ::onReadClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)

        initCollectors()

        initNotificationRecycler()

        viewModel.getAllNotifications()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.notificationUiState){
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
                    waitingDialog.show(status = "Loading ...")
                    Timber.d("explore ads")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

                    adapter.submitList(it.result?.data?.notifications)

                    if (it.result?.data?.notifications?.isEmpty() == false){

                        binding.emptyData.visibility = View.GONE

                    }else{

                        binding.emptyData.visibility = View.VISIBLE

                    }

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.readNotificationUiState){
            when(it){
                is NetworkResult.Error -> {
//                    waitingDialog.dismiss()
                    Timber.d("Error -> ${it.message}, ${it.result.toString()}")
                    ToastUtility.errorToast(requireContext(), "Error : ${it.message}, ${it.result.toString()}")

                }
                is NetworkResult.Idle -> {
                    //nothing to do this is just idle state
                }
                is NetworkResult.Loading -> {
//                    waitingDialog.show(status = "Loading...")
                    Timber.d("Loading...")
                }
                is NetworkResult.Success -> {
//                    waitingDialog.dismiss()

//                    ToastUtility.successToast(requireContext(), it.result?.message)
//                    viewModel.getAllNotifications()

                }
            }
        }

    }

    private fun initClickListeners() {
        binding.btnBack.setOnClickListener{
            findNavController().navigateUp()
        }
    }


    private fun initNotificationRecycler() {

        binding.recyclerNotification.adapter = adapter

    }

    private fun onReadClicked(item: GetNotificationsResponse.Data.Notification) {

        viewModel.readNotificationRequest.id = item.id
        viewModel.readNotification()

    }

    private fun onItemClicked(item: GetNotificationsResponse.Data.Notification) {

        val bundle = Bundle()
        bundle.putString(AppConsts.INTENT_ID, item.ref_id.toString())

        // action 40 -> tenant to maintenance
        // action 50 -> tenant to incident
        // action 80 -> management to maintenance

        when(item.action_type){

            80 -> findNavController().navigate(R.id.action_notificationFragment_to_maintenanceRequestDetailFragment, bundle)
            40 -> findNavController().navigate(R.id.action_notificationFragment2_to_maintenanceTenantsDetailFragment, bundle)
            50 -> findNavController().navigate(R.id.action_notificationFragment2_to_incidentTenantsDetailFragment, bundle)

        }

    }

}