package com.jeuxdevelopers.estatepie.ui.fragments.auth.signup.tenants

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentTenantsSignUpBinding
import com.jeuxdevelopers.estatepie.enums.DeviceType
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.activities.tenants.MainTenantsActivity
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.toDeviceToken
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import com.jeuxdevelopers.estatepie.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TenantsSignUpFragment : Fragment() {

    private lateinit var binding: FragmentTenantsSignUpBinding

    private val viewModel: TenantsSignUpViewModel by viewModels()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTenantsSignUpBinding.inflate(inflater, container, false)

        initCollectors()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.singUpUiState){
            when(it){
                is NetworkResult.Error -> {
                    waitingDialog.dismiss()

                    Timber.d("Error -> ${it.message}, ${it.result?.message}")
                    ToastUtility.errorToast(requireContext(),it.message)

                }
                is NetworkResult.Idle -> {
                    //nothing to do this is just idle state
                }
                is NetworkResult.Loading -> {
                    waitingDialog.show(status = "Signing Up")
                    Timber.d("Signing up")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

                    ToastUtility.successToast(requireContext(), "Account Created successfully.")

                    gotoLoginScreen()

                }
            }
        }
    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        binding.btnLogin.setOnClickListener { gotoLoginScreen() }

        binding.btnSignUp.setOnClickListener {
//            Intent(requireContext(), MainTenantsActivity::class.java).apply
//                startActivity(this)

            validateInputs()
        }
    }

    private fun validateInputs() {

        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val phoneNo = binding.etPhoneNo.text.toString()
        val email = binding.etEmailAddress.text.toString()
        val password = binding.etPassword.text.toString()
        val confirmPass = binding.etConfirmPassword.text.toString()

        if (firstName.isEmpty()) {
            binding.etFirstName.error = "Please enter your first name."
        } else if (lastName.isEmpty()) {
            binding.etLastName.error = "Please enter your last name."
        } else if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etFirstName.error = null
            binding.etLastName.error = null
            binding.etEmailAddress.error = "Email is empty or invalid."

        } else if (phoneNo.isEmpty()) {
            binding.etFirstName.error = null
            binding.etLastName.error = null
            binding.etEmailAddress.error = null
            binding.etPhoneNo.error = "Please enter your phone number."
        } else if (password.isEmpty() || password.length < 6) {
            binding.etFirstName.error = null
            binding.etLastName.error = null
            binding.etEmailAddress.error = null
            binding.etPhoneNo.error = null
            binding.etPassword.error = "Password is too short or empty."
        } else if (confirmPass.isEmpty() || confirmPass != password) {
            binding.etFirstName.error = null
            binding.etLastName.error = null
            binding.etEmailAddress.error = null
            binding.etPhoneNo.error = null
            binding.etPassword.error = null
            binding.etConfirmPassword.error = "Password does not match."
        } else {
            binding.etFirstName.error = null
            binding.etLastName.error = null
            binding.etEmailAddress.error = null
            binding.etPhoneNo.error = null
            binding.etPassword.error = null
            binding.etConfirmPassword.error = null



            viewModel.request.email = email
            viewModel.request.name = "$firstName $lastName"
            viewModel.request.device_token = requireContext().toDeviceToken()
            viewModel.request.first_name = firstName
            viewModel.request.last_name = lastName
            viewModel.request.device_type = DeviceType.android.name
            viewModel.request.password = password
            viewModel.request.password_confirmation = password
            viewModel.request.phone = phoneNo


            viewModel.signUpUser()

        }
    }

    private fun gotoTenantsScreen() {

        Intent(requireContext(), MainTenantsActivity::class.java).apply {
            startActivity(this)
        }

    }

    private fun gotoLoginScreen() {

        if (findNavController().previousBackStackEntry?.destination?.id == R.id.loginFragment) {
            findNavController().navigateUp()
        } else {
            findNavController().navigate(R.id.action_tenantsSignUpFragment_to_loginFragment)
        }

    }


}