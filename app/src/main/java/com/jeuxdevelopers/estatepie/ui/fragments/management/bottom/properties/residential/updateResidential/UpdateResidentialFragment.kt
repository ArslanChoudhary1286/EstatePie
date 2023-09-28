package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.residential.updateResidential

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.location.Address
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.options
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jeuxdevelopers.estatepie.BuildConfig
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentUpdateResidentialBinding
import com.jeuxdevelopers.estatepie.databinding.SheetOptionsChoiceBinding
import com.jeuxdevelopers.estatepie.network.responses.management.properties.PropertiesDetailsResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.location.LocationPickerDialog
import com.jeuxdevelopers.estatepie.ui.dialogs.progress.ProgressDialog
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.galleryAdapter.GalleryAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
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
class UpdateResidentialFragment : Fragment() {

    private lateinit var binding: FragmentUpdateResidentialBinding

    private val viewModel: UpdateResidentialViewModel by viewModels()

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
        binding = FragmentUpdateResidentialBinding.inflate(inflater, container, false)

        setBundleData()

        initCollectors()

        initProgressDialog()

        initRecycler()

        initResultLauncher()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.propertiesDetailsUiState){
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
                    Timber.d("Loading...")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()

                    setUi(it.result?.data)

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.updateResidentialUiState){
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

    private fun initRecycler() {

        binding.uploadPhotoRecycler.adapter = adapter

    }

    private fun initProgressDialog() {

        progressDialog = ProgressDialog(requireContext())

    }

    private fun setUi(data: PropertiesDetailsResponse.Data?) {

        data.let {
            binding.etBuildingName.setText(it?.name)
            binding.etBuildingNo.setText(it?.unit)
            binding.etBuildingNo.isEnabled = false
            binding.etAddress.setText(it?.address)
            binding.etNotes.setText(it?.description)

            it?.property_images?.forEachIndexed{index, image ->

                Glide.with(requireContext()).asBitmap().load(image.image).into(object : SimpleTarget<Bitmap>(){
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {

                       viewModel.imageBitmapList.add(index, resource)
                        adapter.notifyItemInserted(index)


                    }
                })

            }

            adapter.submitList(viewModel.imageBitmapList)

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setBundleData() {

        if (arguments?.getInt(AppConsts.PROPERTY_ID) != null){

            viewModel.id = arguments?.getInt(AppConsts.PROPERTY_ID)!!
            viewModel.propertiesDetailsRequest.id = viewModel.id.toString()

            viewModel.getPropertiesDetails()

        }

    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener{ findNavController().navigateUp() }

        binding.etAddress.setOnClickListener { pickPlaceFromMap() }

        binding.btnSave.setOnClickListener { invalidateInputFields() }

    }

    private fun invalidateInputFields() {

        val buildingName: String = binding.etBuildingName.text.toString()

        val buildingNo: String = binding.etBuildingNo.text.toString()

        val notes: String = binding.etNotes.text.toString()

        val location: String = binding.etAddress.text.toString()

        if (TextUtils.isEmpty(buildingName)){

            binding.etBuildingName.error = getString(R.string.enter_building_name)

        }else if (TextUtils.isEmpty(notes)){

            binding.etNotes.error = getString(R.string.enter_notes)

        }else if (TextUtils.isEmpty(location)){

            binding.etAddress.error = getString(R.string.enter_building_location)

        }else{

            viewModel.updateResidentialRequest.id = viewModel.id

            viewModel.updateResidentialRequest.name = buildingName
            viewModel.updateResidentialRequest.category_id = "1"
            //    category id 1 = Residential, 2 = commercial
            viewModel.updateResidentialRequest.unit = buildingNo
            viewModel.updateResidentialRequest.address = location
            viewModel.updateResidentialRequest.latitude = viewModel.placeLocation?.latitude.toString()
            viewModel.updateResidentialRequest.longitude = viewModel.placeLocation?.longitude.toString()
            viewModel.updateResidentialRequest.notes = notes
//            viewModel.updateResidentialRequest.status = "10"

            val fileUtils =  FileUtils()
            viewModel.imageBitmapList.forEachIndexed{index, bitmap ->

                viewModel.updateResidentialRequest.images
                    .add(fileUtils.convertBitmapToFile(bitmap, requireContext(), "multiImage${index}.jpeg"))

            }

            viewModel.updateResidentialProperty()

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

                AddressFormat.getCityCountry(

                    address.locality, address.countryName

                ).also {

                    binding.etAddress.setText(it)
                }
            }

        }).show(childFragmentManager, LocationPickerDialog.TAG)

    }

    private fun onCrossImageClick(position: Int) {

        viewModel.imageBitmapList.removeAt(position)
        updateRecycler()

    }

    private fun onAddImageClicked(){

        openMediaChoiceSheet()

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

}
