package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.requests.event

import android.annotation.SuppressLint
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
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentEventUpdatesBinding
import com.jeuxdevelopers.estatepie.enums.Plans
import com.jeuxdevelopers.estatepie.network.responses.management.properties.MultiListingPropertyResponse
import com.jeuxdevelopers.estatepie.network.responses.management.requestReports.AllEventListResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.subscription.SubscriptionDialog
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.requests.event.adapter.EventsUpdatesAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.DateUtils
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class EventUpdatesFragment : Fragment() {

    private lateinit var binding: FragmentEventUpdatesBinding

    private val viewModel: EventUpdatesViewModel by viewModels()

    var adapter = EventsUpdatesAdapter(::onItemClicked, ::onDeleteClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventUpdatesBinding.inflate(inflater, container, false)

        if (viewModel.userPlan() == Plans.STANDARD
            || viewModel.userPlan() == Plans.PREMIUM){

            viewModel.getEventsList()
            binding.recyclerMaintenanceRequest.visibility = View.VISIBLE
            binding.emptyData.visibility = View.VISIBLE
            binding.freeView.visibility = View.GONE

        }else{

            binding.recyclerMaintenanceRequest.visibility = View.GONE
            binding.emptyData.visibility = View.GONE
            binding.freeView.visibility = View.VISIBLE

        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.startTime = DateUtils.getSimpleDateStringFromMillis(System.currentTimeMillis() - AppConsts.ONE_YEAR_OLD)

        viewModel.endTime = DateUtils.getSimpleDateStringFromMillis(System.currentTimeMillis())

        if (viewModel.userPlan() == Plans.STANDARD
            || viewModel.userPlan() == Plans.PREMIUM){

            viewModel.getMultiListingProperty()

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (viewModel.userPlan() == Plans.STANDARD
            || viewModel.userPlan() == Plans.PREMIUM){

            initClickListeners()

        }else{

            binding.btnSubscribe.setOnClickListener{
                activity?.let { it1 -> SubscriptionDialog().show(it1.supportFragmentManager, "MyCustomFragment") }
            }

        }


        setUpViews()

    }

    override fun onResume() {
        super.onResume()

        if (viewModel.userPlan() == Plans.STANDARD
            || viewModel.userPlan() == Plans.PREMIUM){

            initCollectors()

            initAdapter()

        }

    }

    private fun initAdapter() {

        binding.recyclerMaintenanceRequest.adapter = adapter

        val azAdapter1 = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_string_adapter, R.id.adapterTxtView, resources.getStringArray(R.array.all_event_array)
        )
        binding.etAll.setAdapter(azAdapter1)

        binding.etAll.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                when(position){
                    0 -> viewModel.request.status = ""
                    1 -> viewModel.request.status = "1"
                    2 -> viewModel.request.status = "2"
                }

                viewModel.getEventsList()

            }

    }

    private fun initClickListeners() {

        binding.tvStartDate.setOnClickListener {
            setStartDate()
        }

        binding.tvEndDate.setOnClickListener {
            setEndDate()
        }


        binding.btnSubscribe.setOnClickListener{
            activity?.let { it1 -> SubscriptionDialog().show(it1.supportFragmentManager, "MyCustomFragment") }
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

            viewModel.getEventsList()

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

            viewModel.getEventsList()

        }

        datePicker.show(childFragmentManager, AppConsts.DATE_PICKER_TAG)
    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.eventUpdatesUiState){
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

                    adapter.submitList(it.result?.data?.events)

                    if (it.result?.data?.events?.isEmpty() == false){

                        binding.emptyData.visibility = View.GONE

                    }else{
                        binding.emptyData.visibility = View.VISIBLE
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

        collectLatestLifecycleFlow(viewModel.deleteEventUiState){
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


                    ToastUtility.successToast(requireContext(), it.result?.message)
                    viewModel.getEventsList()

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

            viewModel.getEventsList()

        }

    }

    private fun onDeleteClicked(item : AllEventListResponse.Data.Event){

        viewModel.deleteEvent(item.id)
    }

    private fun onItemClicked(item : AllEventListResponse.Data.Event){

        val bundle = Bundle()
        bundle.putString(AppConsts.INTENT_ID, item.id.toString())
        findNavController().navigate(R.id.action_requestsFragment_to_eventsUpdatesDetailFragment, bundle)
    }

}