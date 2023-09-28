package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.tenants.assign

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
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
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentAssignPropertyToTenantBinding
import com.jeuxdevelopers.estatepie.network.responses.management.properties.MultiListingPropertyResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.progress.ProgressDialog
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.DateUtils
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AssignPropertyToTenantFragment : Fragment() {

    private lateinit var binding: FragmentAssignPropertyToTenantBinding

    private val viewModel: AssignPropertyToTenantViewModel by viewModels()

    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAssignPropertyToTenantBinding.inflate(inflater, container, false)

        initCollectors()

        initTextWatchers()

        initProgressDialog()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getBundleData()

        viewModel.getMultiListingProperty()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

        setTimeDurationAdapter()

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.propertyMultiListingUiState){
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

                    setPropertyNameAdapter(it.result?.data)

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.assignPropertyUiState){
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

    }

    private fun initTextWatchers() {

        binding.etSelectProperty.addTextChangedListener(object : TextWatcher {

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

                binding.etSelectProperty.error = null
            }

        })

        binding.etLeaseDueDate.addTextChangedListener(object : TextWatcher {

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

                binding.etLeaseDueDate.error = null
            }

        })

        binding.etLeaseDuration.addTextChangedListener(object : TextWatcher {

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

                binding.etLeaseDuration.error = null

            }

        })

        binding.etLeaseAmount.addTextChangedListener(object : TextWatcher {

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

                binding.etLeaseAmount.error = null


            }

        })

        binding.etMoveInDate.addTextChangedListener(object : TextWatcher {

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

                binding.etMoveInDate.error = null


            }

        })

        binding.etNotes.addTextChangedListener(object : TextWatcher {

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

                binding.etNotes.error = null


            }

        })

    }

    private fun initProgressDialog() {

        progressDialog = ProgressDialog(requireContext())

    }

    @SuppressLint("SuspiciousIndentation")
    private fun getBundleData() {

        if (arguments != null){
            viewModel.userId= arguments?.getString(AppConsts.PROFILE_ID, "").toString()
        }

    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.btnSave.setOnClickListener {
            invalidateInputFields()
        }

        binding.etLeaseDueDate.setOnClickListener {
            setLeaseDueDate()
        }

        binding.etMoveInDate.setOnClickListener {
            setMoveInDate()
        }

    }

    private fun invalidateInputFields() {

        val propertyName: String = binding.etSelectProperty.text.toString()

        val leaseDueDate: String = binding.etLeaseDueDate.text.toString()

        val leaseDuration: String = binding.etLeaseDuration.text.toString().substring(0, 2)

        val leaseAmount: String = binding.etLeaseAmount.text.toString()

        val moveInDate: String = binding.etMoveInDate.text.toString()

        val notes: String = binding.etNotes.text.toString()

        if (TextUtils.isEmpty(propertyName)){

            binding.etSelectProperty.error = getString(R.string.select_property)

        }else if (TextUtils.isEmpty(leaseDueDate)){

            binding.etLeaseDueDate.error = getString(R.string.enter_date_time)

        }else if (TextUtils.isEmpty(leaseDuration)){

            binding.etLeaseDuration.error = getString(R.string.enter_lease_duration)

        }else if (TextUtils.isEmpty(leaseAmount)){

            binding.etLeaseAmount.error = getString(R.string.enter_lease_amount)

        }else if (TextUtils.isEmpty(moveInDate)){

            binding.etMoveInDate.error = getString(R.string.enter_date_time)

        }else if (TextUtils.isEmpty(notes)){

            binding.etNotes.error = getString(R.string.enter_notes)

        }else{

            viewModel.assignPropertyRequest.user_id = viewModel.userId
            viewModel.assignPropertyRequest.property_id = viewModel.propertyId
            viewModel.assignPropertyRequest.lease_due_date = leaseDueDate
            viewModel.assignPropertyRequest.lease_duration = leaseDuration
            viewModel.assignPropertyRequest.lease_amount = leaseAmount
            viewModel.assignPropertyRequest.move_in_date = moveInDate
            viewModel.assignPropertyRequest.notes = notes

            viewModel.assignPropertyToTenant()

        }

    }

    private fun setTimeDurationAdapter() {

        val monthDurationAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_string_adapter,
            R.id.adapterTxtView, resources.getStringArray(R.array.lease_duration_array))

        binding.etLeaseDuration.setAdapter(monthDurationAdapter)

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

        binding.etSelectProperty.setAdapter(propertyAdapter)

        binding.etSelectProperty.setOnItemClickListener{parent, view, position, id ->

            viewModel.propertyId = properties[position].id.toString()

        }

    }

    @SuppressLint("SetTextI18n")
    private fun setMoveInDate() {
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTheme(R.style.MaterialCalendarTheme)
                .setCalendarConstraints(constraintsBuilder.build())
                .setTitleText("Select Date")
                .build()
        datePicker.addOnPositiveButtonClickListener {
            binding.etMoveInDate.setText(DateUtils.getSimpleDateStringFromMillis(it))
        }
        datePicker.show(childFragmentManager, AppConsts.DATE_PICKER_TAG)
    }

    @SuppressLint("SetTextI18n")
    private fun setLeaseDueDate() {
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTheme(R.style.MaterialCalendarTheme)
                .setCalendarConstraints(constraintsBuilder.build())
                .setTitleText("Select Date")
                .build()

        datePicker.addOnPositiveButtonClickListener {
            binding.etLeaseDueDate.setText(DateUtils.getSimpleDateStringFromMillis(it))

        }

        datePicker.show(childFragmentManager, AppConsts.DATE_PICKER_TAG)
    }

}