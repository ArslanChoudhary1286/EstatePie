package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.profile.edit

import android.Manifest
import android.graphics.Bitmap
import android.location.Address
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
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
import com.jeuxdevelopers.estatepie.databinding.FragmentTenantsEditProfileBinding
import com.jeuxdevelopers.estatepie.databinding.SheetOptionsChoiceBinding
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.location.LocationPickerDialog
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.validateOnChange
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
class TenantsEditProfileFragment : Fragment() {

    private lateinit var binding: FragmentTenantsEditProfileBinding

    private val viewModel: TenantsEditProfileViewModel by viewModels()
    // Image and Cropper Result Launcher for Picking and Cropping Image.

    private var captureImage: ActivityResultLauncher<Uri>? = null

    private var pickImage: ActivityResultLauncher<String>? = null

    private var cropImage:
            ActivityResultLauncher<CropImageContractOptions>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTenantsEditProfileBinding.inflate(inflater, container, false)

        initCollectors()

        initResultLauncher()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initViews()

        initClickListeners()

        initTextChanges()

    }


    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.updateUserProfileState){
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
                    Timber.d("user profile ")
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

    private fun initTextChanges() {

        binding.inputFirstName.validateOnChange {
            viewModel.user?.details?.first_name = it
        }
        binding.inputLastName.validateOnChange {
            viewModel.user?.details?.last_name = it
        }
        binding.inputPhone.validateOnChange {
            viewModel.user?.details?.phone = it
        }
        binding.inputAddress.validateOnChange {
            viewModel.user?.details?.address = it
        }

    }

    private fun initViews() {

        viewModel.getCurrentUser()?.apply {
            binding.textView7.text = name
            Glide.with(requireContext()).asBitmap().load(details.image_url).into(object : SimpleTarget<Bitmap>(){
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {

                    Glide.with(requireContext()).load(details.image_url)                .centerCrop()
                        .placeholder(R.drawable.ic_house_gray)
                        .error(R.drawable.ic_house_gray)
                        .dontAnimate()
                        .into(binding.imgProfile)

                    viewModel.imageBitmap = resource

                }
            })
            binding.textView8.text = email
            binding.inputFirstName.setText(details.first_name)
            binding.inputLastName.setText(details.last_name)
            binding.inputPhone.setText(details.phone)
            binding.inputAddress.setText(details.address)
        }

    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSave.setOnClickListener {
            validateInputs()
        }

        binding.imgProfile.setOnClickListener {

            openMediaChoiceSheet()
        }

        binding.inputAddress.setOnClickListener { pickPlaceFromMap() }

    }

    private fun validateInputs() {

        val firstName : String = binding.inputFirstName.text.toString()
        val lastName : String = binding.inputLastName.text.toString()
        val phoneNo : String = binding.inputPhone.text.toString()
        val address : String = binding.inputAddress.text.toString()

        if (firstName.isEmpty()){

            binding.inputFirstName.error = "First Name required."

        }else if (lastName.isEmpty()){

            binding.inputFirstName.error = null
            binding.inputLastName.error = "Last Name required."

        }else if (phoneNo.isEmpty()){

            binding.inputLastName.error = null
            binding.inputPhone.error = "Phone Number required."

        }else{

            viewModel.updateUserProfileRequest.first_name = firstName
            viewModel.updateUserProfileRequest.last_name = lastName
            viewModel.updateUserProfileRequest.phone = phoneNo
            viewModel.updateUserProfileRequest.address = address
            viewModel.updateUserProfileRequest.latitude = viewModel.placeLocation?.latitude.toString()
            viewModel.updateUserProfileRequest.longitude = viewModel.placeLocation?.longitude.toString()
            viewModel.updateUserProfileRequest.image = FileUtils()
                .convertBitmapToFile(viewModel.imageBitmap!!, requireContext(), "multiImage.jpeg")
//            ToastUtility.errorToast(requireContext(), "Server Error.")

            viewModel.updateUserProfile()

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

                    binding.inputAddress.setText(it)
                }
            }

        }).show(childFragmentManager, LocationPickerDialog.TAG)

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
                    .into(binding.imgProfile)
            }
        }

    }

}