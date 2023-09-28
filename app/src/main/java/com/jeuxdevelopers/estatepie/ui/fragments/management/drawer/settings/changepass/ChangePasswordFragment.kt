package com.jeuxdevelopers.estatepie.ui.fragments.management.drawer.settings.changepass

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jeuxdevelopers.estatepie.databinding.FragmentChangePasswordBinding
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {

    private lateinit var binding: FragmentChangePasswordBinding

    private val viewModel: ChangePasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)

        initCollectors()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.changePasswordState) {
            when (it) {
                is NetworkResult.Error -> {
                    waitingDialog.dismiss()
                    Timber.d("change Password: Error -> ${it.message}")
                    ToastUtility.errorToast(requireContext(), it.message)
                }

                is NetworkResult.Idle -> {}

                is NetworkResult.Loading -> {
                    waitingDialog.show("changing")
                }

                is NetworkResult.Success -> {
                    waitingDialog.dismiss()

                    Timber.d("change Password: success -> ${it.result.toString()}")
                    ToastUtility.successToast(requireContext(), "Password updated successfully")

                    findNavController().navigateUp()

                }
            }
        }

    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnUpdate.setOnClickListener {
            validateInputs()
        }

    }

    private fun validateInputs() {

        binding.apply {
            val oldPassword = etOldPassword.text.toString()
            val newPassword = etNewPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            if (oldPassword.isEmpty()) {
                inputOldPass.error = "Please enter your old password"
            } else if (newPassword.isEmpty() || newPassword.length < 6) {
                inputOldPass.error = null
                inputNewPass.error = "Please enter new password atleast 6 characters"
            } else if (confirmPassword.isEmpty()) {
                inputOldPass.error = null
                inputNewPass.error = null
                inputConfirmPass.error = "Please confirm new password"
            } else if (confirmPassword != newPassword) {
                inputOldPass.error = null
                inputNewPass.error = null
                inputConfirmPass.error = null
                inputConfirmPass.error = "Password does not match!, please check"
            } else {
                inputOldPass.error = null
                inputNewPass.error = null
                inputConfirmPass.error = null

                viewModel.changePasswordRequest.current_password = oldPassword
                viewModel.changePasswordRequest.password = newPassword
                viewModel.changePasswordRequest.password_confirmation = confirmPassword

                viewModel.updatePassword()

            }
        }

    }

}