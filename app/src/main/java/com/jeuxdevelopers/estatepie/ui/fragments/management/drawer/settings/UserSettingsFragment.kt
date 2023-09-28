package com.jeuxdevelopers.estatepie.ui.fragments.management.drawer.settings

import android.Manifest
import android.graphics.Bitmap
import android.location.Address
import android.net.Uri
import android.os.Bundle
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
import com.jeuxdevelopers.estatepie.databinding.FragmentUserSettingsBinding
import com.jeuxdevelopers.estatepie.databinding.SheetOptionsChoiceBinding
import com.jeuxdevelopers.estatepie.network.responses.management.settings.UserProfileResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.location.LocationPickerDialog
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.CommonFuncs
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
class UserSettingsFragment : Fragment() {

    private lateinit var binding: FragmentUserSettingsBinding

    private val viewModel: UserSettingsViewModel by viewModels()
    // Image and Cropper Result Launcher for Picking and Cropping Image.

    private var captureImage: ActivityResultLauncher<Uri>? = null

    private var pickImage: ActivityResultLauncher<String>? = null

    private var cropImage:
            ActivityResultLauncher<CropImageContractOptions>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserSettingsBinding.inflate(inflater, container, false)

        initCollectors()

        initResultLauncher()

        viewModel.getUserProfile()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

    }


    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.userProfileState){
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

                    updateUi(it.result?.data)

                }
            }
        }

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

    private fun updateUi(data: UserProfileResponse.Data?) {

        if (data != null){

            val (firstName, lastName) = CommonFuncs().splitName(data.name)
            binding.etFirstName.setText(firstName)
            binding.etLastName.setText(lastName)
            binding.etEmailAddress.setText(data.email)
            binding.etBusinessName.setText(viewModel.user?.details?.business_name)
            binding.etBusinessLocation.setText(data.address)
            binding.etPhoneNo.setText(data.phone)

            Glide.with(requireContext()).asBitmap().load(data.image).into(object : SimpleTarget<Bitmap>(){
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {

                    viewModel.imageBitmap = resource
                    binding.profileImage.setImageBitmap(resource)

                }
            })

        }

    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.etChangePassword.setOnClickListener {
            findNavController().navigate(R.id.action_userSettingsFragment_to_changePasswordFragment)
        }

        binding.profileImage.setOnClickListener {

            openMediaChoiceSheet()
        }

        binding.etBusinessLocation.setOnClickListener { pickPlaceFromMap() }

        binding.btnSaveEdit.setOnClickListener {

            invalidateInputs()

        }

    }

    private fun invalidateInputs() {


        if (binding.etFirstName.text.toString().isEmpty()) {
            binding.etFirstName.error = "Please enter your first name."
        } else if (binding.etLastName.text.toString().isEmpty()) {
            binding.etLastName.error = "Please enter your last name."
        }else if (binding.etPhoneNo.text.toString().isEmpty()) {
            binding.etPhoneNo.error = "Please enter your phone number"
        } else {
            binding.etFirstName.error = null
            binding.etLastName.error = null
            binding.etPhoneNo.error = null

            viewModel.updateUserProfileRequest.first_name = binding.etFirstName.text.toString()
            viewModel.updateUserProfileRequest.last_name = binding.etLastName.text.toString()
            viewModel.updateUserProfileRequest.business_name =
                binding.etBusinessName.text.toString()
            viewModel.updateUserProfileRequest.address = binding.etBusinessLocation.text.toString()
            viewModel.updateUserProfileRequest.email = binding.etEmailAddress.text.toString()
            viewModel.updateUserProfileRequest.phone = binding.etPhoneNo.text.toString()
            viewModel.updateUserProfileRequest.latitude =
                viewModel.placeLocation?.latitude.toString()
            viewModel.updateUserProfileRequest.longitude =
                viewModel.placeLocation?.longitude.toString()

            if (viewModel.imageBitmap != null) {
                viewModel.updateUserProfileRequest.image = FileUtils()
                    .convertBitmapToFile(
                        viewModel.imageBitmap!!,
                        requireContext(),
                        "multiImage.jpeg"
                    )
            }
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

                    binding.etBusinessLocation.setText(it)
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
                    .into(binding.profileImage)
            }
        }

    }


}