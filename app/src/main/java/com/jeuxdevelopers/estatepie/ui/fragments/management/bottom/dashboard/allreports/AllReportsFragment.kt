package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.allreports

import android.annotation.SuppressLint
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
import com.jeuxdevelopers.estatepie.databinding.FragmentAllReportsBinding
import com.jeuxdevelopers.estatepie.network.responses.management.dashboard.AllReportsResponse
import com.jeuxdevelopers.estatepie.network.responses.management.dashboard.MangeUtilitiesResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.allreports.adapter.AllReportsAdapter
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.billspayment.adapter.UtilityBillsAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.DateUtils
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AllReportsFragment : Fragment() {

    private lateinit var binding: FragmentAllReportsBinding

    private val viewModel: AllReportsViewModel by viewModels()

    var adapter = AllReportsAdapter(::onItemClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllReportsBinding.inflate(inflater,container,false)

        initCollectors()

        initAdapter()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.request.report_type = "All"

        viewModel.getAllReports()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViews()

        initClickListeners()

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

            viewModel.endDate = DateUtils.getSimpleDateStringFromMillis(it)

            viewModel.getAllReports()

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

            viewModel.startDate = DateUtils.getSimpleDateStringFromMillis(it)

            viewModel.getAllReports()

        }

        datePicker.show(childFragmentManager, AppConsts.DATE_PICKER_TAG)
    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.allReportsUiState){
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

                    adapter.submitList(it.result?.data?.requests)

                    if (it.result?.data?.requests?.isEmpty() == false){

                        binding.emptyData.visibility = View.GONE

                    }else{

                        binding.emptyData.visibility = View.VISIBLE

                    }

                }
            }
        }


    }

    private fun initAdapter() {

        binding.recyclerAllReports.adapter = adapter

    }

    private fun onItemClicked(item: AllReportsResponse.Data.Request) {


        val bundle = Bundle()
        bundle.putString(AppConsts.INTENT_ID, item.property_id.toString())
        bundle.putString(AppConsts.TITLE, item.name)
//        bundle.putString(AppConsts.BILL_MODEL, Gson().toJson(item))
        findNavController().navigate(R.id.action_managementDashboardFragment_to_requestsByPropertyIdFragment, bundle)


    }


}