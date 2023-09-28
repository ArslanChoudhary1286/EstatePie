package com.jeuxdevelopers.estatepie.ui.fragments.tenants.drawer.payment.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentPaymentScheduleBinding
import com.jeuxdevelopers.estatepie.network.responses.tenants.billing.SchedulesPaymentListResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.ListOfBillsResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.billing.unpaid.adapter.TenantUnPaidBillsAdapter
import com.jeuxdevelopers.estatepie.ui.fragments.tenants.drawer.payment.schedule.adapter.PaymentScheduleAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PaymentScheduleFragment : Fragment() {

    private lateinit var binding: FragmentPaymentScheduleBinding

    private val viewModel: PaymentScheduleViewModel by viewModels()

    var adapter = PaymentScheduleAdapter(::onEditClicked,::onDeleteClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentScheduleBinding.inflate(inflater, container, false)

        viewModel.getPaymentScheduleList()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

    }

    override fun onResume() {
        super.onResume()

        initCollectors()

        setAdapters()

    }

    private fun setAdapters() {

        binding.recyclerSchedule.adapter = adapter

    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.addImage.setOnClickListener{
            findNavController().navigate(R.id.action_paymentScheduleFragment_to_addPayScheduleFragment)
        }

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.paymentScheduleUiState){
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

                    adapter.submitList(it.result?.data?.schedules)

                    if (it.result?.data?.schedules?.isEmpty() == false){

                        binding.emptyData.visibility = View.GONE

                    }else{
                        binding.emptyData.visibility = View.VISIBLE
                    }

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.deletePaymentScheduleUiState){
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
                    viewModel.getPaymentScheduleList()

                }
            }
        }

    }

    private fun onEditClicked(item : SchedulesPaymentListResponse.Data.Schedule){

        val bundle = Bundle()
        bundle.putString(AppConsts.DETAIL_ARGUMENTS, Gson().toJson(item).toString())
        findNavController().navigate(R.id.action_paymentScheduleFragment_to_editPaymentFragment, bundle)

    }

    private fun onDeleteClicked(item : SchedulesPaymentListResponse.Data.Schedule){

        viewModel.deletePaymentSchedule(item.id)

    }

}