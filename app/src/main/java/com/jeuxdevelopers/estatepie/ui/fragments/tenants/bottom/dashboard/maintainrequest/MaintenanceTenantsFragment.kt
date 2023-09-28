package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.dashboard.maintainrequest

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
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentMaintenanceTenantsBinding
import com.jeuxdevelopers.estatepie.databinding.FragmentTenantsDashboardBinding
import com.jeuxdevelopers.estatepie.network.responses.management.billing.PaidBillByTenantIdResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.MaintenanceRequestResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.billing.adapters.PaidBillsByTenantIdAdapter
import com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.dashboard.maintainrequest.adapter.TenantMaintenanceAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.DateUtils
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MaintenanceTenantsFragment : Fragment() {

    private lateinit var binding: FragmentMaintenanceTenantsBinding

    private val viewModel: MaintenanceTenantsViewModel by viewModels()

    var adapter = TenantMaintenanceAdapter(::onItemClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMaintenanceTenantsBinding.inflate(inflater, container, false)

        initCollectors()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.startTime = DateUtils.getSimpleDateStringFromMillis(System.currentTimeMillis() - 31556952000)

        viewModel.endTime = DateUtils.getSimpleDateStringFromMillis(System.currentTimeMillis())

        viewModel.getMaintenanceRequests()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

        setUpViews()

        setAdapter()

    }

    private fun setUpViews() {

        binding.tvStartDate.setText(DateUtils.getDateStringFromMillis(System.currentTimeMillis() - AppConsts.ONE_YEAR_OLD))
        binding.tvEndDate.setText(DateUtils.getDateStringFromMillis(System.currentTimeMillis()))

    }

    private fun initClickListeners() {

        binding.tvStartDate.setOnClickListener {
            setStartDate()
        }

        binding.tvEndDate.setOnClickListener {
            setEndDate()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setEndDate() {

        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now())
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTheme(R.style.MaterialCalendarTheme)
                .setCalendarConstraints(constraintsBuilder.build())
                .setTitleText("Select Date")
                .build()

        datePicker.addOnPositiveButtonClickListener {
            binding.tvEndDate.setText(DateUtils.getDateStringFromMillis(it))

            viewModel.endTime = DateUtils.getSimpleDateStringFromMillis(it)

            viewModel.getMaintenanceRequests()

        }

        datePicker.show(childFragmentManager, AppConsts.DATE_PICKER_TAG)
    }

    @SuppressLint("SetTextI18n")
    private fun setStartDate() {
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now())
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTheme(R.style.MaterialCalendarTheme)
                .setCalendarConstraints(constraintsBuilder.build())
                .setTitleText("Select Date")
                .build()

        datePicker.addOnPositiveButtonClickListener {
            binding.tvStartDate.setText(DateUtils.getDateStringFromMillis(it))

            viewModel.startTime = DateUtils.getSimpleDateStringFromMillis(it)

            viewModel.getMaintenanceRequests()

        }

        datePicker.show(childFragmentManager, AppConsts.DATE_PICKER_TAG)
    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.maintenanceRequestsIdUiState){
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

                    adapter.submitList(it.result?.data?.maintenance_requests)

                    if (it.result?.data?.maintenance_requests?.isEmpty() == false){

                        binding.emptyData.visibility = View.GONE

                    }else{
                        binding.emptyData.visibility = View.VISIBLE
                    }

                }
            }
        }
    }

    private fun setAdapter() {

        binding.recyclerMaintenanceRequest.adapter = adapter

    }

    private fun onItemClicked(item : MaintenanceRequestResponse.Data.MaintenanceRequest){

        val bundle = Bundle()
//        bundle.putString(AppConsts.DETAIL_ARGUMENTS, Gson().toJson(item).toString())
        bundle.putString(AppConsts.INTENT_ID, item.id.toString())
        findNavController().navigate(R.id.action_tenantsDashboardFragment_to_maintenanceTenantsDetailFragment, bundle)

    }

}