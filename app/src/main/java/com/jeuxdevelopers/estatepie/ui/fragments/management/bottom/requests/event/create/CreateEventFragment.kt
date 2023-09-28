package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.requests.event.create

import android.Manifest
import android.annotation.SuppressLint
import android.location.Address
import android.net.Uri
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
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.options
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.jeuxdevelopers.estatepie.BuildConfig
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentCreateEventBinding
import com.jeuxdevelopers.estatepie.databinding.SheetOptionsChoiceBinding
import com.jeuxdevelopers.estatepie.network.responses.management.properties.MultiListingPropertyResponse
import com.jeuxdevelopers.estatepie.network.responses.management.requestReports.AllEventTypesResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.location.LocationPickerDialog
import com.jeuxdevelopers.estatepie.ui.dialogs.progress.ProgressDialog
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.galleryAdapter.GalleryAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.DateUtils
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.FileUtils
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import com.jeuxdevelopers.estatepie.utils.format.AddressFormat
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
class CreateEventFragment : Fragment() {

    private lateinit var binding: FragmentCreateEventBinding

    private val viewModel: CreateEventViewModel by viewModels()

    private lateinit var progressDialog: ProgressDialog

    // Image and Cropper Result Launcher for Picking and Cropping Image.

    private var captureImage: ActivityResultLauncher<Uri>? = null

    private var pickImage: ActivityResultLauncher<String>? = null

    private var cropImage:
            ActivityResultLauncher<CropImageContractOptions>? = null

    var adapter = GalleryAdapter(::onAddImageClicked, ::onCrossImageClick)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateEventBinding.inflate(inflater, container, false)

        initCollectors()

        initRecycler()

        initAdapter()

        initProgressDialog()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getEventsTypes()

        viewModel.getMultiListingProperty()

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTextWatchers()

        initClickListeners()

        initResultLauncher()

    }

    private fun initAdapter() {

        adapter.submitList(viewModel.imageBitmapList)

    }

    private fun initRecycler() {

        binding.uploadPhotoRecycler.adapter = adapter

    }

    private fun initTextWatchers() {

        binding.etUserName.addTextChangedListener(object : TextWatcher {

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

                binding.etUserName.error = null
            }

        })

        binding.etEventType.addTextChangedListener(object : TextWatcher {

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

                binding.etEventType.error = null

            }

        })

        binding.etProperty.addTextChangedListener(object : TextWatcher {

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

                binding.etProperty.error = null
            }

        })

        binding.etDate.addTextChangedListener(object : TextWatcher {

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

                binding.etDate.error = null
            }

        })

        binding.etStartTime.addTextChangedListener(object : TextWatcher {

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

                binding.etStartTime.error = null
            }

        })

        binding.etEndTime.addTextChangedListener(object : TextWatcher {

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

                binding.etEndTime.error = null
            }

        })

        binding.etAddress.addTextChangedListener(object : TextWatcher {

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

                binding.etAddress.error = null
            }

        })

        binding.etDescription.addTextChangedListener(object : TextWatcher {

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

                binding.etDescription.error = null
            }

        })

    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener{ findNavController().navigateUp() }

        binding.etAddress.setOnClickListener { pickPlaceFromMap() }

        binding.btnCreateEvent.setOnClickListener { invalidateInputFields() }

        binding.etDate.setOnClickListener { setStartDate() }

        binding.etStartTime.setOnClickListener { showStartTime() }

        binding.etEndTime.setOnClickListener { showEndTime() }

    }

    @SuppressLint("SetTextI18n")
    private fun showStartTime() {

        val selectDateTime = Calendar.getInstance()
        selectDateTime.timeInMillis = System.currentTimeMillis()

        val timePicker =
            MaterialTimePicker.Builder()
                .setTitleText("Start Time")
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
                .setMinute(Calendar.getInstance().get(Calendar.MINUTE)).build()

        timePicker.addOnPositiveButtonClickListener {

            selectDateTime.set(Calendar.HOUR, timePicker.hour)
            selectDateTime.set(Calendar.MINUTE, timePicker.minute)

            viewModel.startTime = selectDateTime.timeInMillis
            binding.etEndTime.text = null

            binding.etStartTime.setText(
                DateUtils.getTimeFromMillis(
                    selectDateTime.timeInMillis
                )
            )

        }
        timePicker.show(childFragmentManager, AppConsts.TIME_PICKER_TAG)

    }

    @SuppressLint("SetTextI18n")
    private fun showEndTime() {

        val selectDateTime = Calendar.getInstance()
        selectDateTime.timeInMillis = viewModel.startTime

        val timePicker =
            MaterialTimePicker.Builder()
                .setTitleText("End Time")
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
                .setMinute(Calendar.getInstance().get(Calendar.MINUTE)).build()

        timePicker.addOnPositiveButtonClickListener {

            selectDateTime.set(Calendar.HOUR, timePicker.hour)
            selectDateTime.set(Calendar.MINUTE, timePicker.minute)

            if (selectDateTime.timeInMillis > viewModel.startTime)

                binding.etEndTime.setText(
                    DateUtils.getTimeFromMillis(
                        selectDateTime.timeInMillis
                    )
                )

            else

                ToastUtility.errorToast(requireContext(), "Please select valid time.")

        }
        timePicker.show(childFragmentManager, AppConsts.TIME_PICKER_TAG)

    }

    private fun invalidateInputFields() {

        val eventName: String = binding.etUserName.text.toString()

        val eventType: String = binding.etEventType.text.toString()

        val property: String = binding.etProperty.text.toString()

        val date: String = binding.etDate.text.toString()

        val startTime: String = binding.etStartTime.text.toString()

        val endTime: String = binding.etEndTime.text.toString()

        val address: String = binding.etAddress.text.toString()

        val description: String = binding.etDescription.text.toString()

        if (TextUtils.isEmpty(eventName)){

            binding.etUserName.error = getString(R.string.empty_field)

        }else if (TextUtils.isEmpty(eventType)){

            binding.etEventType.error = getString(R.string.empty_field)

        }else if (TextUtils.isEmpty(property)){

            binding.etProperty.error = getString(R.string.empty_field)

        }else if (TextUtils.isEmpty(date)){

            binding.etDate.error = getString(R.string.empty_field)

        }else if (TextUtils.isEmpty(startTime)){

            binding.etStartTime.error = getString(R.string.empty_field)

        }else if (TextUtils.isEmpty(endTime)){

            binding.etEndTime.error = getString(R.string.empty_field)

        }else if (TextUtils.isEmpty(address)){

            binding.etAddress.error = getString(R.string.empty_field)

        }else if (TextUtils.isEmpty(description)){

            binding.etDescription.error = getString(R.string.empty_field)

        }else{

//            ToastUtility.successToast(requireContext(), "id = " + viewModel.propertyId)
//            ToastUtility.successToast(requireContext(), "id = " + viewModel.eventId)

            viewModel.addNewEventRequest.title = eventName
            viewModel.addNewEventRequest.event_type_id = viewModel.eventId
            viewModel.addNewEventRequest.property_id = viewModel.propertyId
            viewModel.addNewEventRequest.date = viewModel.date
            viewModel.addNewEventRequest.time = startTime +" - " +endTime
            viewModel.addNewEventRequest.address = address
            viewModel.addNewEventRequest.latitude = viewModel.placeLocation?.latitude.toString()
            viewModel.addNewEventRequest.longitude = viewModel.placeLocation?.longitude.toString()
            viewModel.addNewEventRequest.description = description

            val fileUtils =  FileUtils()
            viewModel.imageBitmapList.forEachIndexed{index, bitmap ->

                viewModel.addNewEventRequest.images
                    .add(fileUtils.convertBitmapToFile(bitmap, requireContext(), "multiImage${index}.jpeg"))

            }

            viewModel.addNewEvent()

        }

    }

    @SuppressLint("SetTextI18n")
    private fun setStartDate() {
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
            binding.etDate.setText(DateUtils.getDateStringFromMillis(it))

            viewModel.date = DateUtils.getDateTimeWithSecondFromMillis(it)

        }

        datePicker.show(childFragmentManager, AppConsts.DATE_PICKER_TAG)
    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.addNewEventUiState){
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
                    findNavController().navigateUp()

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

        collectLatestLifecycleFlow(viewModel.eventsTypesUiState){
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

                    setEventTypesAdapter(it.result?.data)

                }
            }
        }

    }

    private fun setEventTypesAdapter(data: List<AllEventTypesResponse.Data>?) {

        val array = Array<String>(data!!.size){""}

        data.forEachIndexed { index, element ->
            array[index] = element.name
        }

        val propertyAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_string_adapter,
            R.id.adapterTxtView, array)

        binding.etEventType.setAdapter(propertyAdapter)

        binding.etEventType.setOnItemClickListener{parent, view, position, id ->

            viewModel.eventId = data[position].id.toString()

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

        binding.etProperty.setAdapter(propertyAdapter)

        binding.etProperty.setOnItemClickListener{parent, view, position, id ->

            viewModel.propertyId = properties[position].id.toString()

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

    private fun initResultLauncher() {

        pickImage = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { result ->

//            result?.let { uri -> openImageCropper(uri) }

            result?.let { uris ->

                uris.forEachIndexed {index, uri ->

                    viewModel.imageBitmapList
                        .add(viewModel.imageBitmapList.size, FileUtils().uriToBitmap(uri, requireContext()))

                }

                updateRecycler()

            }

        }

        captureImage = registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->

            result?.let { capture ->

                if (capture) {

                    openImageCropper(viewModel.imageUri!!)

                }
            }
        }

        cropImage = registerForActivityResult(CropImageContract()) { result ->

            if (result.isSuccessful) {

                // Storing Cropped Image and Loading into ImageView.

                viewModel.imageBitmapList
                    .add(viewModel.imageBitmapList.size,  FileUtils().uriToBitmap(result.uriContent!!, requireContext()))

                updateRecycler()

//                Glide.with(requireContext())
//                    .load(result.uriContent)
//                    .into(binding.uploadPhoto)
            }
        }

    }

    private fun updateRecycler() {

        adapter.notifyDataSetChanged()
        if (viewModel.imageBitmapList.size > 0)
            binding.uploadPhotoRecycler.smoothScrollToPosition(viewModel.imageBitmapList.size)

    }

    private fun onAddImageClicked(){

        openMediaChoiceSheet()

    }

    private fun onCrossImageClick(position : Int){

        viewModel.imageBitmapList.removeAt(position)
        updateRecycler()

    }

    private fun initProgressDialog() {

        progressDialog = ProgressDialog(requireContext())

    }

    private fun pickPlaceFromMap() {

        LocationPickerDialog(object : LocationPickerDialog.LocationResultListener {

            override fun onLocationReceived(
                address: Address, latLng: LatLng
            ) {

                viewModel.placeLocation = latLng

                viewModel.placeCity = address.locality
                viewModel.placeCountry = address.countryName

                AddressFormat.getCityCountry(

                    address.locality, address.countryName

                ).also {

                    binding.etAddress.setText(it)
                }
            }

        }).show(childFragmentManager, LocationPickerDialog.TAG)

    }

}