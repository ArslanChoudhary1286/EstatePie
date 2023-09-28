package com.jeuxdevelopers.estatepie.ui.fragments.tenants.drawer.payment.setting

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentPaymentScheduleBinding
import com.jeuxdevelopers.estatepie.databinding.FragmentPaymentSettingsBinding
import com.jeuxdevelopers.estatepie.network.responses.plans.CardDetailResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.DateUtils
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PaymentSettingsFragment : Fragment() {

    private lateinit var binding: FragmentPaymentSettingsBinding

    private val viewModel: PaymentSettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentSettingsBinding.inflate(inflater, container, false)

        initCollectors()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getCardDetail()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.etValidity.setOnClickListener {
            setExpiryDate()
        }
        binding.btnUpdateCard.setOnClickListener {

            if(binding.btnUpdateCard.text == getString(R.string.update_card)){

                if (fieldsValidate()){
                    viewModel.updateCustomerCard()
                }

            }else{

                if (fieldsValidate()){
                    viewModel.addCustomerCard()
                }

            }

        }

    }

    @SuppressLint("SetTextI18n")
    private fun setExpiryDate() {
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTheme(R.style.MaterialCalendarTheme)
                .setCalendarConstraints(constraintsBuilder.build())
                .setTitleText("Expiry Date")
                .build()

        datePicker.addOnPositiveButtonClickListener {

            val date:String = DateUtils.getExpiryDateStringFromMillis(it)
            binding.etValidity.setText(date)

        }

        datePicker.addOnNegativeButtonClickListener{

        }

        datePicker.show(childFragmentManager, AppConsts.DATE_PICKER_TAG)
    }

    private fun fieldsValidate(): Boolean {

        val cardName = binding.etCardName.text.toString()
        val cardNo = binding.etCardNo.text.toString()
        val cardExpiry = binding.etValidity.text.toString()
        val cvv = binding.etCvv.text.toString()

        if (cardName.isEmpty() || cardNo.isEmpty() || cardExpiry.isEmpty() || cvv.isEmpty()){
            ToastUtility.successToast(requireContext(), getString(R.string.empty_field))
            return false
        }

        return true
    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.cardDetailUiState){
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
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

                    if (it.result?.data != null){

                        updateViews(it.result.data)

                    }else{

                        binding.customerCard.cardLayout.visibility = View.GONE
                        binding.btnUpdateCard.text = getString(R.string.add_card)

                    }

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.addCardUiState){
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
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

                    ToastUtility.successToast(requireContext(), it.result?.message)
                    viewModel.getCardDetail()

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.updateCardUiState){
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
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.toString()}")

                    ToastUtility.successToast(requireContext(), it.result?.message)
                    viewModel.getCardDetail()

                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun updateViews(data: CardDetailResponse.Data) {

        binding.customerCard.cardLayout.visibility = View.VISIBLE
        binding.btnUpdateCard.text = getString(R.string.update_card)

        binding.customerCard.let {
            it.cardType.text = data.card_brand
            it.cardNumber.text = " " + data.card_last_four
            it.cardExpiry.text = "${data.card_month}/${data.card_year}"
        }

    }

}