package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.dashboard.upcomingbills.detail

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
import com.jeuxdevelopers.estatepie.databinding.FragmentUnPaidBillDetailBinding
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.ViewBillDetailsResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UnPaidBillDetailFragment : Fragment(){

    private lateinit var binding: FragmentUnPaidBillDetailBinding

    private val viewModel: UnPaidBillDetailViewModel by viewModels()

//    var dropInClient : DropInClient? = null
//
//    var dropInRequest : DropInRequest? = null
//
//    var REQUEST_CODE  = 999

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUnPaidBillDetailBinding.inflate(inflater, container, false)

        initCollectors()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.viewBillDetail(arguments?.getInt(AppConsts.INTENT_ID)!!)

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.billDetailUiState){
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

                    updateUi(it.result?.data)

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.payBillUiState){
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
                    binding.billStatus.text = "Paid"
                    binding.btnPayBill.visibility = View.GONE

                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(data: ViewBillDetailsResponse.Data?) {

        data?.let {

            viewModel.request.bill_id = it.id.toString()

            if (it.status.equals("Paid")){
                binding.btnPayBill.visibility = View.GONE
            }

            binding.billName.text = it.bill_type
            binding.billStatus.text = it.status
            binding.billDate.text = it.date
            binding.billAmount.text = "$"+it.amount.toString()
            Glide.with(binding.root).load(it.image)
                .centerCrop()
                .placeholder(R.drawable.ic_house_gray)
                .error(R.drawable.ic_house_gray)
                .dontAnimate()
                .into(binding.billImage);

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()


//        dropInRequest = DropInRequest()
//        dropInClient = DropInClient(requireActivity(),dropInRequest, "")
//        dropInClient!!.setListener(this)

    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        binding.btnPayBill.setOnClickListener {

            viewModel.payBill()

//            dropInClient?.launchDropInForResult(requireActivity(), REQUEST_CODE)
        }

    }

//    override fun onDropInSuccess(dropInResult: DropInResult) {
//        var token = dropInResult.paymentDescription.toString()
//        ToastUtility.successToast(requireContext(), token)
//    }
//
//    override fun onDropInFailure(error: Exception) {
//        ToastUtility.errorToast(requireContext(), error.toString())
//    }

}