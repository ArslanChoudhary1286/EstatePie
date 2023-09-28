package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.billing.sendinvoice

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.options
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_12H
import com.jeuxdevelopers.estatepie.BuildConfig
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentSendInvoiceBinding
import com.jeuxdevelopers.estatepie.databinding.SheetOptionsChoiceBinding
import com.jeuxdevelopers.estatepie.network.responses.management.billing.PropertiesWithBillTypeResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.progress.ProgressDialog
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.AppConsts.DATE_PICKER_TAG
import com.jeuxdevelopers.estatepie.utils.AppConsts.TIME_PICKER_TAG
import com.jeuxdevelopers.estatepie.utils.DateUtils
import com.jeuxdevelopers.estatepie.utils.DateUtils.getDateStringFromMillis
import com.jeuxdevelopers.estatepie.utils.DateUtils.getDateTimeFromMillis
import com.jeuxdevelopers.estatepie.utils.DateUtils.getSimpleDateStringFromMillis
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.FileUtils
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import java.util.*

@AndroidEntryPoint
class SendInvoiceFragment : Fragment() {

    private lateinit var binding: FragmentSendInvoiceBinding

    private val viewModel: SendInvoiceViewModel by viewModels()

    private lateinit var progressDialog: ProgressDialog

    // Image and Cropper Result Launcher for Picking and Cropping Image.

    private var captureImage: ActivityResultLauncher<Uri>? = null

    private var pickImage: ActivityResultLauncher<String>? = null

    private var cropImage:
            ActivityResultLauncher<CropImageContractOptions>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSendInvoiceBinding.inflate(inflater, container, false)

        initCollectors()

        initTextWatchers()

        initProgressDialog()

        viewModel.getPropertiesWithBillTypes()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initClickListeners()

        initResultLauncher()

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.propertiesWithBillTypesUiState){
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

//                    adapter.submitList(it.result?.data?.properties)
                    setPropertyNameAdapter(it.result?.data?.properties)
                    setBillTypeAdapter(it.result?.data?.bill_types)

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.sendInvoiceUiState){
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

        binding.etPropertyName.addTextChangedListener(object : TextWatcher {

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

                binding.etPropertyName.error = null
            }

        })

        binding.etDateTime.addTextChangedListener(object : TextWatcher {

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

                binding.etDateTime.error = null
            }

        })

        binding.etInvoiceAmount.addTextChangedListener(object : TextWatcher {

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

                binding.etInvoiceAmount.error = null

            }

        })

        binding.etBillType.addTextChangedListener(object : TextWatcher {

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

                binding.etBillType.error = null


            }

        })

    }

    private fun initProgressDialog() {

        progressDialog = ProgressDialog(requireContext())

    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.btnSend.setOnClickListener {
            invalidateInputFields()
        }

        binding.etDateTime.setOnClickListener {
            openCalendar()
        }

        binding.invoiceImage.setOnClickListener { openMediaChoiceSheet() }

    }

    private fun invalidateInputFields() {

        val buildingName: String = binding.etPropertyName.text.toString()

        val dateTime: String = binding.etDateTime.text.toString()

        val amount: String = binding.etInvoiceAmount.text.toString()

        val billType: String = binding.etBillType.text.toString()


        if (TextUtils.isEmpty(buildingName)){

            binding.etPropertyName.error = getString(R.string.enter_property_name)

        }else if (TextUtils.isEmpty(dateTime)){

            binding.etDateTime.error = getString(R.string.enter_date_time)

        }else if (viewModel.imageUri == null){

            ToastUtility.errorToast(requireContext(), R.string.upload_image)

        }else if (TextUtils.isEmpty(amount)){

            binding.etInvoiceAmount.error = getString(R.string.enter_invoice_amount)

        }
        else if (TextUtils.isEmpty(billType)){

            binding.etBillType.error = getString(R.string.enter_bill_type)

        }else{

            viewModel.sendInvoiceRequest.amount = amount
            viewModel.sendInvoiceRequest.date = dateTime
            viewModel.sendInvoiceRequest.image = FileUtils()
                .convertBitmapToFile(viewModel.imageBitmap!!, requireContext(), "multiImage.jpeg")
            viewModel.sendInvoiceRequest.units = viewModel.units
            viewModel.sendInvoiceRequest.property_id = viewModel.propertyId
            viewModel.sendInvoiceRequest.bill_type_id = viewModel.billTypeId
            viewModel.sendInvoiceRequest.user_id = ""

            Timber.d("Bill data -> " + viewModel.sendInvoiceRequest)

            viewModel.sendInvoice()

        }

    }

    private fun setBillTypeAdapter(billTypes: List<PropertiesWithBillTypeResponse.Data.BillType>?) {

        val array = Array<String>(billTypes!!.size){""}

        billTypes.forEachIndexed { index, element ->
            array[index] = element.name
        }

        val statusAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_string_adapter,
            R.id.adapterTxtView, array)

        binding.etBillType.setAdapter(statusAdapter)

        binding.etBillType.setOnItemClickListener{parent, view, position, id ->

            viewModel.billTypeId = billTypes[position].id.toString()
        }

    }

    private fun setPropertyNameAdapter(properties: List<PropertiesWithBillTypeResponse.Data.Property>?) {

        val array = Array<String>(properties!!.size){""}

        properties.forEachIndexed { index, element ->
            array[index] = element.name
        }

        val statusAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_string_adapter,
            R.id.adapterTxtView, array)

        binding.etPropertyName.setAdapter(statusAdapter)

        binding.etPropertyName.setOnItemClickListener{parent, view, position, id ->

            viewModel.propertyId = properties[position].id.toString()
            val item: String = properties[position].name
            viewModel.units = item.substring(item.indexOf("(") + 1, item.indexOf(")"))

        }

    }

    private fun openMediaChoiceSheet() {

        val binding = SheetOptionsChoiceBinding.inflate(layoutInflater)

        val sheetDialog = BottomSheetDialog(requireContext())

        sheetDialog.setContentView(binding.root)

        with(binding) {

            cameraOption.setOnClickListener {
                sheetDialog.dismiss()
                takeImageFromCamera()
            }

            galleryOption.setOnClickListener {
                sheetDialog.dismiss()
                pickImageFromStorage()
            }
        }

        sheetDialog.show()
    }

    private fun takeImageFromCamera() {

        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {

                override fun onPermissionGranted(response: PermissionGrantedResponse) {

                    try {

                        val tempFile = File.createTempFile(
                            UUID.randomUUID().toString(), AppConsts.JPG_EXT, requireContext().cacheDir
                        )

                        val uriForImage = FileProvider.getUriForFile(
                            requireContext(), "${BuildConfig.APPLICATION_ID}.provider", tempFile
                        )

                        viewModel.imageUri = uriForImage

                        captureImage?.launch(uriForImage)

                    } catch (exception: Exception) {

                        Timber.d("Error -> %s", exception.message)

                    }

                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {

                    if (response.isPermanentlyDenied) {

                        ToastUtility.errorToast(
                            requireContext(), getString(R.string.enable_camera_permission)
                        )

                    } else {

                        ToastUtility.errorToast(
                            requireContext(), getString(R.string.camera_permission_required)
                        )
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    request: PermissionRequest, permissionToken: PermissionToken
                ) {
                    permissionToken.continuePermissionRequest()
                }

            }).check()

    }

    private fun pickImageFromStorage() {

        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {

                override fun onPermissionGranted(response: PermissionGrantedResponse?) {

                    pickImage?.launch("image/*")

                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {

                    if (response.isPermanentlyDenied) {

                        ToastUtility.errorToast(
                            requireContext(), getString(R.string.enable_read_storage_permission)
                        )

                    } else {

                        ToastUtility.errorToast(
                            requireContext(), getString(R.string.read_storage_permission_required)
                        )
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    request: PermissionRequest, permissionToken: PermissionToken
                ) {
                    permissionToken.continuePermissionRequest()
                }

            }).check()

    }

    private fun openImageCropper(uri: Uri) {

        cropImage?.launch(options(uri = uri))

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
                    .setTimeFormat(CLOCK_12H)
                    .setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
                    .setMinute(Calendar.getInstance().get(Calendar.MINUTE)).build()

            timePicker.addOnPositiveButtonClickListener {

                selectDateTime.set(Calendar.HOUR, timePicker.hour)
                selectDateTime.set(Calendar.MINUTE, timePicker.minute)

                if (selectDateTime.timeInMillis > Calendar.getInstance().timeInMillis)

                    binding.etDateTime.setText(getSimpleDateStringFromMillis(selectDateTime.timeInMillis))

                else

                    ToastUtility.errorToast(requireContext(), "Please select valid time.")

            }
            timePicker.show(childFragmentManager, TIME_PICKER_TAG)

        }
        datePicker.show(childFragmentManager, DATE_PICKER_TAG)

    }

    private fun onItemClicked(item : PropertiesWithBillTypeResponse.Data.Property){

//        binding.etPropertyName.text = item.name

//        val bundle = Bundle()
//        bundle.putString(AppConsts.DETAIL_ARGUMENTS, Gson().toJson(item))
//        bundle.putString(AppConsts.PROFILE_ID, item.id.toString())
//        findNavController().navigate(R.id.action_tenantsFragment_to_tenantsProfileFragment, bundle)

    }

    private fun initResultLauncher() {

        pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { result ->

            result?.let { uri -> openImageCropper(uri) }

        }

        captureImage = registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->

            result?.let { capture ->

                if (capture) {

                    viewModel.imageUri?.let { uri ->
                        openImageCropper(uri)
                    }

                }
            }
        }

        cropImage = registerForActivityResult(CropImageContract()) { result ->

            if (result.isSuccessful) {

                viewModel.imageUri = result.uriContent

                val filePath = result.getUriFilePath(requireContext(), true).toString()

                viewModel.imageBitmap =  FileUtils().uriToBitmap(result.uriContent!!, requireContext())

                Glide.with(requireContext())
                    .load(result.uriContent)
                    .centerCrop()
                    .placeholder(R.drawable.ic_house_gray)
                    .error(R.drawable.ic_house_gray)
                    .dontAnimate()
                    .into(binding.invoiceImage)
            }
        }

    }


}