package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.profile.vehicle.updateVehicle

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.BuildConfig
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentUpdateVehicleBinding
import com.jeuxdevelopers.estatepie.databinding.SheetOptionsChoiceBinding
import com.jeuxdevelopers.estatepie.network.responses.explore.ExploreRecommendedResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.profile.GetVehicleResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.progress.ProgressDialog
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.galleryAdapter.GalleryAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.DateUtils
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
class UpdateVehicleFragment : Fragment() {


    private lateinit var binding: FragmentUpdateVehicleBinding

    private val viewModel: UpdateVehicleViewModel by viewModels()

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
        binding = FragmentUpdateVehicleBinding.inflate(inflater, container, false)

        setBundleData()

        initCollectors()

        initProgressDialog()

        initAdapter()

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
        collectLatestLifecycleFlow(viewModel.updateVehicleUiState){
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

    private fun initAdapter() {

        val vehicleMakeAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_string_adapter, R.id.adapterTxtView, resources.getStringArray(R.array.vehicle_make_array)
        )
        binding.etVehicleMake.setAdapter(vehicleMakeAdapter)

        binding.etVehicleMake.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

            }

        val modelAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_string_adapter, R.id.adapterTxtView, resources.getStringArray(R.array.vehicle_model_array)
        )
        binding.etModel.setAdapter(modelAdapter)

        val colorAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_string_adapter, R.id.adapterTxtView, resources.getStringArray(R.array.vehicle_color_array)
        )
        binding.etColor.setAdapter(colorAdapter)

    }

    private fun initProgressDialog() {

        progressDialog = ProgressDialog(requireContext())

    }

    @SuppressLint("SetTextI18n")
    private fun setBundleData() {

        var item : GetVehicleResponse.Data.Vehicle

        val json : String = arguments?.getString(AppConsts.DETAIL_ARGUMENTS).toString()

        if (json.isNotEmpty()){

            item = Gson().fromJson(json, GetVehicleResponse.Data.Vehicle::class.java)

            viewModel.id = item.id

            updateUi(item)

        }

    }

    private fun updateUi(item: GetVehicleResponse.Data.Vehicle?) {

        binding.etVehicleMake.setText(item?.vehicle_type)
        binding.etModel.setText(item?.model)
        binding.etColor.setText(item?.color)
        binding.etLicence.setText(item?.licence_plate)

        item?.vehicle_images?.forEachIndexed{index, image ->

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

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener{ findNavController().navigateUp() }

        binding.btnSave.setOnClickListener { invalidateInputFields() }

    }


    private fun invalidateInputFields() {

        val vehicleMake: String = binding.etVehicleMake.text.toString()

        val model: String = binding.etModel.text.toString()

        val color: String = binding.etColor.text.toString()

        val licence: String = binding.etLicence.text.toString()

        if (TextUtils.isEmpty(vehicleMake)){

            binding.etVehicleMake.error = "The vehicle type field is required."

        }else if (TextUtils.isEmpty(model)){

            binding.etModel.error = "The model field is required."

        }else if (TextUtils.isEmpty(color)){

            binding.etColor.error = "The color field is required."

        } else if (TextUtils.isEmpty(licence)){

            binding.etLicence.error = "The licence plate field is required."

        }else{

            viewModel.updateVehicleRequest.id = viewModel.id
            viewModel.updateVehicleRequest.vehicle_type = vehicleMake
            viewModel.updateVehicleRequest.model = model
            viewModel.updateVehicleRequest.color = color
            viewModel.updateVehicleRequest.licence_plate = licence
            viewModel.updateVehicleRequest.registration_date =
                DateUtils.getDateTimeFromMillis(System.currentTimeMillis())

            val fileUtils =  FileUtils()
            viewModel.imageBitmapList.forEachIndexed{index, bitmap ->

                viewModel.updateVehicleRequest.images
                    .add(fileUtils.convertBitmapToFile(bitmap, requireContext(), "multiImage${index}.jpeg"))

            }

            viewModel.updateVehicle()

        }


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