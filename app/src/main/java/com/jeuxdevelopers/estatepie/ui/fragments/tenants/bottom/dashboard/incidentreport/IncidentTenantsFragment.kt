package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.dashboard.incidentreport

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentIncidentTenantsBinding
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.IncidentReportResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.dashboard.incidentreport.adapter.TenantIncidentReportAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.DateUtils
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class IncidentTenantsFragment : Fragment() {

    private lateinit var binding: FragmentIncidentTenantsBinding

    private val viewModel: IncidentTenantsViewModel by viewModels()

    var adapter = TenantIncidentReportAdapter(::onItemClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIncidentTenantsBinding.inflate(inflater, container, false)

        initCollectors()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getIncidentReports()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

    }

    override fun onResume() {
        super.onResume()

        setAdapters()

    }

    private fun initClickListeners() {

        binding.etSelectDate.setOnClickListener{

            setStartDate()
        }

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.incidentReportsUiState){
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

                    it.result?.data?.incident_requests?.forEachIndexed{index, data ->
                        if (index == 0)
                            viewModel.reportList.clear()

                        viewModel.reportList.add(data)
                    }

                    adapter.submitList( viewModel.reportList)

                    if (it.result?.data?.incident_requests?.isEmpty() == false){

                        binding.emptyData.visibility = View.GONE

                    }else{
                        binding.emptyData.visibility = View.VISIBLE
                    }

                }
            }
        }

    }

    private fun onItemClicked(item : IncidentReportResponse.Data.IncidentRequest){

        val bundle = Bundle()
        bundle.putString(AppConsts.INTENT_ID, item.id.toString())
        findNavController().navigate(R.id.action_tenantsDashboardFragment_to_incidentTenantsDetailFragment, bundle)

    }

    private fun setAdapters() {

        binding.recyclerIncidentReport.adapter = adapter

        if (viewModel.reportList.isNotEmpty()){

            binding.emptyData.visibility = View.GONE

        }

        // reports item adapter
        val reportsAdapter = ArrayAdapter<String>(
            requireActivity(),
            R.layout.item_string_adapter_border, R.id.adapterTxtView, resources.getStringArray(R.array.all_reports)
        )

        binding.etAllReports.setAdapter(reportsAdapter)


        binding.etAllReports.setOnItemClickListener{parent, view, position, id ->

            var filterdList = ArrayList<IncidentReportResponse.Data.IncidentRequest>()

            if (position == 0){

                adapter.submitList(viewModel.reportList)

                if (viewModel.reportList.isEmpty() == false){

                    binding.emptyData.visibility = View.GONE

                }else{
                    binding.emptyData.visibility = View.VISIBLE
                }

            } else if (position == 1){

                viewModel.reportList.forEachIndexed{index, data ->

                    if (data.status.equals("Under Review")){
                        filterdList.add(data)
                    }

                }
                adapter.submitList(filterdList)

                if (filterdList.isEmpty() == false){

                    binding.emptyData.visibility = View.GONE

                }else{
                    binding.emptyData.visibility = View.VISIBLE
                }

            }else{

                viewModel.reportList.forEachIndexed{index, data ->

                    if (data.status.equals("Resolved")){
                        filterdList.add(data)
                    }

                }

                adapter.submitList(filterdList)

                if (filterdList.isEmpty() == false){

                    binding.emptyData.visibility = View.GONE

                }else{
                    binding.emptyData.visibility = View.VISIBLE
                }

            }

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
                .setTitleText("End Date")
                .build()

        datePicker.addOnPositiveButtonClickListener {

            viewModel.endTime = DateUtils.getSimpleDateStringFromMillis(it)

            viewModel.getIncidentReports()

        }
        datePicker.addOnNegativeButtonClickListener{

            viewModel.startTime = ""
            viewModel.endTime = ""

            viewModel.getIncidentReports()

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
                .setTitleText("Start Date")
                .build()

        datePicker.addOnPositiveButtonClickListener {

            viewModel.startTime = DateUtils.getSimpleDateStringFromMillis(it)

            setEndDate()
        }

        datePicker.addOnNegativeButtonClickListener{

            viewModel.startTime = ""
            viewModel.endTime = ""

            viewModel.getIncidentReports()

        }

        datePicker.show(childFragmentManager, AppConsts.DATE_PICKER_TAG)
    }

}