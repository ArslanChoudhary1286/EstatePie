package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.requests.maintenance

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentMaintenanceRequestsBinding
import com.jeuxdevelopers.estatepie.network.responses.management.properties.MultiListingPropertyResponse
import com.jeuxdevelopers.estatepie.network.responses.management.requestReports.MaintenanceWithFilterResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.requests.maintenance.adapter.MaintenanceWithFilterAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.DateUtils
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MaintenanceRequestsFragment : Fragment() {

    private lateinit var binding: FragmentMaintenanceRequestsBinding

    private val viewModel: MaintenanceRequestsViewModel by viewModels()

    var adapter = MaintenanceWithFilterAdapter(::onItemClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMaintenanceRequestsBinding.inflate(inflater, container, false)

        viewModel.getMaintenanceWithFilter()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.startTime = DateUtils.getSimpleDateStringFromMillis(System.currentTimeMillis() - AppConsts.ONE_YEAR_OLD)

        viewModel.endTime = DateUtils.getSimpleDateStringFromMillis(System.currentTimeMillis())

        viewModel.getMultiListingProperty()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

        setUpViews()

    }

    override fun onResume() {
        super.onResume()

        initCollectors()

        initAdapter()

    }

    private fun initAdapter() {

        binding.recyclerMaintenanceRequest.adapter = adapter

        val azAdapter1 = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_string_adapter, R.id.adapterTxtView, resources.getStringArray(R.array.all_incident_report)
        )
        binding.etAll.setAdapter(azAdapter1)

        binding.etAll.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                when(position){
                    0 -> viewModel.request.status = ""
                    1 -> viewModel.request.status = "0"
                    2 -> viewModel.request.status = "1"
                }

                viewModel.getMaintenanceWithFilter()

            }

    }

    private fun initClickListeners() {

        binding.tvStartDate.setOnClickListener {
            setStartDate()
        }

        binding.tvEndDate.setOnClickListener {
            setEndDate()
        }

    }

    private fun setUpViews() {

        binding.tvStartDate.setText(DateUtils.getDateStringFromMillis(System.currentTimeMillis() - AppConsts.ONE_YEAR_OLD))
        binding.tvEndDate.setText(DateUtils.getDateStringFromMillis(System.currentTimeMillis()))

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

            viewModel.getMaintenanceWithFilter()

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

            viewModel.getMaintenanceWithFilter()

        }

        datePicker.show(childFragmentManager, AppConsts.DATE_PICKER_TAG)
    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.maintenanceWithFilterUiState){
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

                    adapter.submitList(it.result?.data?.maintenance_request)

                    if (it.result?.data?.maintenance_request?.isEmpty() == false){

                        binding.emptyData.visibility = View.GONE
                        binding.progress.visibility = View.VISIBLE

                    }else{
                        binding.emptyData.visibility = View.VISIBLE
                        binding.progress.visibility = View.GONE

                    }

                    val inProgress: Int? = it.result?.data?.inprogress
                    val completed: Int? = it.result?.data?.completed

                    val total : Int? = inProgress?.plus(completed!!)

                    if (total != null){

                        val percentage = (inProgress.toDouble().div( total.toDouble())).times(100)
                        binding.progress.progress = percentage.toInt()

                    }

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.propertyMultiListingUiState){
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

                    setPropertyNameAdapter(it.result?.data)

                }
            }
        }

    }

    private fun setPropertyNameAdapter(properties: List<MultiListingPropertyResponse.Data>?) {

        val array = Array<String>(properties!!.size){""}

        properties.forEachIndexed { index, element ->
            array[index] = element.name
        }

        val propertyAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_string_adapter,
            R.id.adapterTxtView, array)

        binding.etAllProperties.setAdapter(propertyAdapter)

        binding.etAllProperties.setOnItemClickListener{parent, view, position, id ->

//            val keyword = binding.etAllProperties.text.toString()
//            viewModel.request.keyword = properties[position].name

            viewModel.getMaintenanceWithFilter()

        }

    }

    private fun onItemClicked(item : MaintenanceWithFilterResponse.Data.MaintenanceRequest){

        val bundle = Bundle()
        bundle.putString(AppConsts.INTENT_ID, item.id.toString())
        findNavController().navigate(R.id.action_requestsFragment_to_maintenanceRequestDetailFragment, bundle)

    }



}