package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.residential.addResidential

import android.Manifest
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
import com.jeuxdevelopers.estatepie.BuildConfig
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentAddResidentialBinding
import com.jeuxdevelopers.estatepie.databinding.SheetOptionsChoiceBinding
import com.jeuxdevelopers.estatepie.network.responses.management.properties.ResidentialPropertiesResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.location.LocationPickerDialog
import com.jeuxdevelopers.estatepie.ui.dialogs.progress.ProgressDialog
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.galleryAdapter.GalleryAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts.JPG_EXT
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.FileUtils
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import com.jeuxdevelopers.estatepie.utils.format.AddressFormat.getCityCountry
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
class AddResidentialFragment : Fragment() {

    private lateinit var binding: FragmentAddResidentialBinding

    private val viewModel: AddResidentialViewModel by viewModels()

    private lateinit var progressDialog: ProgressDialog

    // Image and Cropper Result Launcher for Picking and Cropping Image.

    private var captureImage: ActivityResultLauncher<Uri>? = null

    private var pickImage: ActivityResultLauncher<String>? = null

    private var cropImage:
            ActivityResultLauncher<CropImageContractOptions>? = null

    var adapter = GalleryAdapter(::onAddImageClicked, ::onCrossImageClick)

    private var item : ResidentialPropertiesResponse.Data.Property? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddResidentialBinding.inflate(inflater, container, false)

        initCollectors()

        initTextWatchers()

        initRecycler()

        initAdapter()

        initProgressDialog()

        initResultLauncher()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.addResidentialPropertyUiState){
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
    }

    private fun initTextWatchers() {

        binding.etBuildingName.addTextChangedListener(object : TextWatcher {

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

                binding.etBuildingName.error = null
            }

        })

        binding.etBuildingNo.addTextChangedListener(object : TextWatcher {

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

                binding.etBuildingNo.error = null

                if (!sequence.isNullOrEmpty())
                    updateViews(1)
                else
                    updateViews(4)

            }

        })

        binding.etStartingNoRange.addTextChangedListener(object : TextWatcher {

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

                binding.etStartingNoRange.error = null

                if (!sequence.isNullOrEmpty())
                    updateViews(2)
                else
                    updateViews(4)
            }

        })

        binding.etEndingNoRange.addTextChangedListener(object : TextWatcher {

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

                binding.etEndingNoRange.error = null

                if (!sequence.isNullOrEmpty())
                    updateViews(2)

            }

        })

        binding.etStatingAlphRange.addTextChangedListener(object : TextWatcher {

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

                binding.etStartingNoRange.error = null
                if (!sequence.isNullOrEmpty())
                    updateViews(3)
                else
                    updateViews(4)
            }

        })

        binding.etEndingAlphaRange.addTextChangedListener(object : TextWatcher {

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

                binding.etEndingNoRange.error = null
                if (!sequence.isNullOrEmpty())
                    updateViews(3)

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

    private fun initRecycler() {

        binding.uploadPhotoRecycler.adapter = adapter

    }

    private fun initAdapter() {

        adapter.submitList(viewModel.imageBitmapList)

    }

    private fun initProgressDialog() {

        progressDialog = ProgressDialog(requireContext())

    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener{ findNavController().navigateUp() }

        binding.etAddress.setOnClickListener { pickPlaceFromMap() }

        binding.btnSave.setOnClickListener { invalidateInputFields() }

    }

    private fun updateViews(input: Int) {
        when(input){
            1 -> {

                binding.etBuildingNo.alpha = 1f
                binding.etStartingNoRange.alpha = 0.5f
                binding.etEndingNoRange.alpha = 0.5f
                binding.etStatingAlphRange.alpha = 0.5f
                binding.etEndingAlphaRange.alpha = 0.5f

                binding.etBuildingNo.isEnabled = true
                binding.etStartingNoRange.isEnabled = false
                binding.etEndingNoRange.isEnabled = false
                binding.etStatingAlphRange.isEnabled = false
                binding.etEndingAlphaRange.isEnabled = false

            }2 -> {

            binding.etBuildingNo.alpha = 0.5f
            binding.etStartingNoRange.alpha = 1f
            binding.etEndingNoRange.alpha = 1f
            binding.etStatingAlphRange.alpha = 0.5f
            binding.etEndingAlphaRange.alpha = 0.5f

            binding.etBuildingNo.isEnabled = false
            binding.etStartingNoRange.isEnabled = true
            binding.etEndingNoRange.isEnabled = true
            binding.etStatingAlphRange.isEnabled = false
            binding.etEndingAlphaRange.isEnabled = false

        }3 -> {

            binding.etBuildingNo.alpha = 0.5f
            binding.etStartingNoRange.alpha = 0.5f
            binding.etEndingNoRange.alpha = 0.5f
            binding.etStatingAlphRange.alpha = 1f
            binding.etEndingAlphaRange.alpha = 1f

            binding.etBuildingNo.isEnabled = false
            binding.etStartingNoRange.isEnabled = false
            binding.etEndingNoRange.isEnabled = false
            binding.etStatingAlphRange.isEnabled = true
            binding.etEndingAlphaRange.isEnabled = true

        }else -> {

            binding.etBuildingNo.alpha = 1f
            binding.etStartingNoRange.alpha = 1f
            binding.etEndingNoRange.alpha = 1f
            binding.etStatingAlphRange.alpha = 1f
            binding.etEndingAlphaRange.alpha = 1f

            binding.etBuildingNo.isEnabled = true
            binding.etStartingNoRange.isEnabled = true
            binding.etEndingNoRange.isEnabled = true
            binding.etStatingAlphRange.isEnabled = true
            binding.etEndingAlphaRange.isEnabled = true

            binding.etEndingNoRange.text = null
            binding.etEndingAlphaRange.text = null

        }

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
                            UUID.randomUUID().toString(), JPG_EXT, requireContext().cacheDir
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

                val filePath = result.getUriFilePath(requireContext(), true).toString()

                viewModel.imageBitmapList
                    .add(viewModel.imageBitmapList.size,  FileUtils().uriToBitmap(result.uriContent!!, requireContext()))

                updateRecycler()

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

    private fun invalidateInputFields() {

        val buildingName: String = binding.etBuildingName.text.toString()

        val buildingNo: String = binding.etBuildingNo.text.toString()

        val startNoRange: String = binding.etStartingNoRange.text.toString()

        val endNoRange: String = binding.etEndingNoRange.text.toString()

        val startAlphaRange: String = binding.etStatingAlphRange.text.toString()

        val endAlphaRange: String = binding.etEndingAlphaRange.text.toString()

        val notes: String = binding.etNotes.text.toString()

        val location: String = binding.etAddress.text.toString()

        if (TextUtils.isEmpty(buildingName)){

            binding.etBuildingName.error = getString(R.string.enter_building_name)

        }else if (TextUtils.isEmpty(buildingNo)
            && (TextUtils.isEmpty(startNoRange) && TextUtils.isEmpty(endNoRange))
            && (TextUtils.isEmpty(startAlphaRange) && TextUtils.isEmpty(endAlphaRange))){


            if (TextUtils.isEmpty(buildingNo)){

                    binding.etBuildingNo.error = getString(R.string.enter_building_number)

            }else if (TextUtils.isEmpty(startNoRange)){

                binding.etStartingNoRange.error = getString(R.string.enter_start_no_range)

            }else if (TextUtils.isEmpty(endNoRange)){

                binding.etEndingNoRange.error = getString(R.string.enter_end_no_range)

            }else if (TextUtils.isEmpty(startAlphaRange)){

                binding.etStatingAlphRange.error = getString(R.string.enter_start_alpha_range)

            }else if (TextUtils.isEmpty(endAlphaRange)){

                binding.etEndingAlphaRange.error = getString(R.string.enter_end_alpha_range)

            }

        }else if (TextUtils.isEmpty(notes)){

            binding.etNotes.error = getString(R.string.enter_notes)

        }else if (viewModel.imageBitmapList.size == 0){

            ToastUtility.errorToast(requireContext(), R.string.upload_image)

        }else if (TextUtils.isEmpty(location)){

            binding.etAddress.error = getString(R.string.enter_building_location)

        }else{

            viewModel.addResidentialRequest.name = buildingName
            viewModel.addResidentialRequest.category_id = "1"
            //    category id 1 = Residential, 2 = commercial
            viewModel.addResidentialRequest.apt_number = buildingNo
            viewModel.addResidentialRequest.number_range_from = startNoRange
            viewModel.addResidentialRequest.number_range_to = endNoRange
            viewModel.addResidentialRequest.range_alpha_from = startAlphaRange
            viewModel.addResidentialRequest.range_alpha_to = endAlphaRange
            viewModel.addResidentialRequest.address = location
            viewModel.addResidentialRequest.latitude = viewModel.placeLocation?.latitude.toString()
            viewModel.addResidentialRequest.longitude = viewModel.placeLocation?.longitude.toString()
            viewModel.addResidentialRequest.notes = notes
            viewModel.addResidentialRequest.status = "10"

            val fileUtils =  FileUtils()
            viewModel.imageBitmapList.forEachIndexed{index, bitmap ->

                viewModel.addResidentialRequest.images
                    .add(fileUtils.convertBitmapToFile(bitmap, requireContext(), "multiImage${index}.jpeg"))

            }

            viewModel.addResidentialProperty()

        }


    }

    private fun pickPlaceFromMap() {

        LocationPickerDialog(object : LocationPickerDialog.LocationResultListener {

            override fun onLocationReceived(
                address: Address, latLng: LatLng
            ) {

                viewModel.placeLocation = latLng

                viewModel.placeCity = address.locality
                viewModel.placeCountry = address.countryName

                getCityCountry(

                    address.locality, address.countryName

                ).also {

                    binding.etAddress.setText(it)
                }
            }

        }).show(childFragmentManager, LocationPickerDialog.TAG)

    }

    private fun onDeleteImageClick(propertyImage: ResidentialPropertiesResponse.Data.Property.PropertyImage) {

        ToastUtility.successToast(requireContext(), "image deleted")
    }


}