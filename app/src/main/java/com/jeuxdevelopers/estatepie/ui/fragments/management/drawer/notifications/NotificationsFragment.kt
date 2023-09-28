package com.jeuxdevelopers.estatepie.ui.fragments.management.drawer.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jeuxdevelopers.estatepie.databinding.FragmentNotificationsBinding
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding

    private val viewModel: NotificationsViewModel by viewModels()

    private val waitingDialog: WaitingDialog by lazy { WaitingDialog(requireContext()) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initCollectors()
        initClickListeners()
    }

    private fun initCollectors() {
        collectLatestLifecycleFlow(viewModel.toggleSwitchState) {
            when (it) {
                is NetworkResult.Error -> {
                    waitingDialog.dismiss()
                    Timber.d("Toggle error -> ${it.message.toString()}")
                    ToastUtility.errorToast(requireContext(), it.message.toString())
                }
                is NetworkResult.Idle -> {}
                is NetworkResult.Loading -> {
                    waitingDialog.show("Toggling")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    ToastUtility.successToast(requireContext(), it.result?.message)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        initViews()
    }

    private fun initViews() {
        viewModel.getCurrentUser()?.apply {
            Timber.d("CurrentUser -> Name: ${details.first_name} email -> ${details.email_updates} push -> ${details.notifications}")
            binding.switchPush.isChecked = details.notifications != 0
            binding.switchEmail.isChecked = details.email_updates != 0
        }
    }

    private fun initClickListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.switchEmail.setOnCheckedChangeListener { _, _ ->
            viewModel.toggleEmailNotification()
        }
        binding.switchPush.setOnCheckedChangeListener { _, _ ->
            viewModel.togglePushNotification()
        }
    }

}