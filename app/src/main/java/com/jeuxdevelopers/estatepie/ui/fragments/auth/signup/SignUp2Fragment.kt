package com.jeuxdevelopers.estatepie.ui.fragments.auth.signup

import android.location.Address
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentSignUp2Binding
import com.jeuxdevelopers.estatepie.enums.DeviceType
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.location.LocationPickerDialog
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import com.jeuxdevelopers.estatepie.utils.format.AddressFormat
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class SignUp2Fragment : Fragment() {

    private lateinit var binding: FragmentSignUp2Binding

    private val viewModel: signUp2ViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUp2Binding.inflate(inflater, container, false)

        initCollectors()

        initTextWatchers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

    }

    private fun initTextWatchers() {

        binding.etBusinessName.addTextChangedListener(object : TextWatcher {

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

                binding.etBusinessName.error = null
            }

        })

        binding.etBusinessLocation.addTextChangedListener(object : TextWatcher {

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

                binding.etBusinessLocation.error = null
            }

        })


    }
    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.singUpUiState){
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
                    waitingDialog.show(status = "Signing Up")
                    Timber.d("Signing up")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")
//                    findNavController().navigate(R.id.action_signUp2Fragment_to_plansFragment)
                    ToastUtility.successToast(requireContext(), "Account Created successfully.")
                    gotoLoginScreen()

                }
            }
        }
    }

    private fun gotoLoginScreen() {

        if (findNavController().previousBackStackEntry?.destination?.id == R.id.loginFragment) {
            findNavController().navigateUp()
        } else {
            findNavController().navigate(R.id.action_signUp2Fragment_to_loginFragment)
        }

    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        binding.btnNext.setOnClickListener { validateInputs() }

        binding.etBusinessLocation.setOnClickListener { pickPlaceFromMap() }

    }

    private fun validateInputs() {

        val firstName = requireArguments().getString("first_name", "")
        val lastName = requireArguments().getString("last_name", "")
        val email = requireArguments().getString("email", "")
        val password = requireArguments().getString("password","")
        val confirmPass = requireArguments().getString("confirm_password", "")
        val businessName = binding.etBusinessName.text.toString()
        val businessLocation = binding.etBusinessLocation.text.toString()

        if (businessName.isEmpty()) {
            binding.etBusinessName.error = "Please enter your business name."
        } else if (businessLocation.isEmpty()) {
            binding.etBusinessLocation.error = "Please enter your business location."
        } else {
            binding.etBusinessName.error = null
            binding.etBusinessLocation.error = null


            viewModel.request.email = email
            viewModel.request.name = "$firstName $lastName"
            viewModel.request.device_token = "abctokenefgh"//AppConsts.DEVICE_TOKEN
            viewModel.request.first_name = firstName
            viewModel.request.last_name = lastName
            viewModel.request.device_type = DeviceType.android.name
            viewModel.request.password = password
            viewModel.request.password_confirmation = confirmPass
            viewModel.request.business_name = businessName
            viewModel.request.address = businessLocation

            viewModel.signUpUser()


        }
    }

    private fun pickPlaceFromMap() {

        LocationPickerDialog(object : LocationPickerDialog.LocationResultListener {

            override fun onLocationReceived(
                address: Address, latLng: LatLng
            ) {

                viewModel.placeLocation = latLng

                viewModel.placeCity = address.locality
                viewModel.placeCountry = address.countryName

                AddressFormat.getCityCountry(

                    address.locality, address.countryName

                ).also {

                    binding.etBusinessLocation.setText(it)
                }
            }

        }).show(childFragmentManager, LocationPickerDialog.TAG)

    }

}