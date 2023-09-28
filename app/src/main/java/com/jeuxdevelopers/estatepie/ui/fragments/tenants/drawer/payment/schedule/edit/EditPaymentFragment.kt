package com.jeuxdevelopers.estatepie.ui.fragments.tenants.drawer.payment.schedule.edit

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentEditPaymentBinding
import com.jeuxdevelopers.estatepie.databinding.FragmentPaymentScheduleBinding
import com.jeuxdevelopers.estatepie.network.responses.tenants.billing.SchedulesPaymentListResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.GetBillTypesResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.MaintenanceRequestResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.DateUtils
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class EditPaymentFragment : Fragment() {

    private lateinit var binding: FragmentEditPaymentBinding

    private val viewModel: EditPaymentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getBillTypes()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

        setBundleData()

    }

    override fun onResume() {
        super.onResume()

        initCollectors()

    }

    private fun setBundleData() {

        val json = arguments?.getString(AppConsts.DETAIL_ARGUMENTS)
        Gson().fromJson(json, SchedulesPaymentListResponse.Data.Schedule::class.java)
            .let {

                viewModel.request.title = it.title
                binding.etBillType.setText(it.title)
                binding.etDateTime.text = it.pick_date
                binding.swith.isChecked = it.is_schedule
                viewModel.id = it.id

            }
    }

    private fun initClickListeners() {

        binding.etDateTime.setOnClickListener {
            openCalendar()
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnAddSchedule.setOnClickListener {
            invalidateInputFields()
        }

    }

    private fun invalidateInputFields() {

        val dateTime : String = binding.etDateTime.text.toString()

        if (viewModel.request.title.isEmpty()){
            ToastUtility.errorToast(requireContext(), getString(R.string.empty_field))
        }else if (dateTime.isEmpty()){
            ToastUtility.errorToast(requireContext(), getString(R.string.empty_field))

        }else{

            viewModel.request.pick_date = dateTime

            viewModel.updatePaymentSchedule(viewModel.id)

        }

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.updatePaymentScheduleUiState){
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
                    findNavController().navigateUp()

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.billTypesUiState){
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

                    setBillTypesAdapter(it.result?.data)

                }
            }
        }
    }

    private fun setBillTypesAdapter(data: List<GetBillTypesResponse.Data>?) {

        val billsTypeList = ArrayList<String>()

        data?.forEachIndexed{index, billName ->

            if (index == 0)
                billsTypeList.add(getString(R.string.all_bills))

            billsTypeList.add(billName.name)

        }

        // All bills adapter
        val billAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_string_adapter,
            R.id.adapterTxtView, billsTypeList
        )

        binding.etBillType.setAdapter(billAdapter)

        binding.etBillType.setOnItemClickListener{parent, view, position, id ->

            if (position == 0)
                viewModel.request.title = ""
            else
                viewModel.request.title = data!![position-1].name

        }

    }

    @SuppressLint("SetTextI18n")
    private fun openCalendar() {

        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTheme(R.style.MaterialCalendarTheme)
                .setCalendarConstraints(constraintsBuilder.build())
                .setTitleText("Select Date").build()

        datePicker.addOnPositiveButtonClickListener {

            datePicker.dismiss()
            val selectDateTime = Calendar.getInstance()
            selectDateTime.timeInMillis = it

            val timePicker =
                MaterialTimePicker.Builder()
                    .setTitleText("Select Time")
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
                    .setMinute(Calendar.getInstance().get(Calendar.MINUTE)).build()

            timePicker.addOnPositiveButtonClickListener {

                selectDateTime.set(Calendar.HOUR, timePicker.hour)
                selectDateTime.set(Calendar.MINUTE, timePicker.minute)

                if (selectDateTime.timeInMillis > Calendar.getInstance().timeInMillis)

                    binding.etDateTime.setText(
                        DateUtils.getDateTimeFromMillis(
                            selectDateTime.timeInMillis
                        )
                    )

                else

                    ToastUtility.errorToast(requireContext(), "Please select valid time.")

            }
            timePicker.show(childFragmentManager, AppConsts.TIME_PICKER_TAG)

        }
        datePicker.show(childFragmentManager, AppConsts.DATE_PICKER_TAG)

    }
}