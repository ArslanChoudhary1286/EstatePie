package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.request.addreports

import android.Manifest
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.options
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jeuxdevelopers.estatepie.BuildConfig
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentTenantsAddReportsBinding
import com.jeuxdevelopers.estatepie.databinding.SheetOptionsChoiceBinding
import com.jeuxdevelopers.estatepie.models.shared.SharedViewModel
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.adapter.DialogAdapter
import com.jeuxdevelopers.estatepie.ui.dialogs.adapter.DialogAmenitiesAdapter
import com.jeuxdevelopers.estatepie.ui.dialogs.progress.ProgressDialog
import com.jeuxdevelopers.estatepie.ui.dialogs.property.PropertyTypeDialog
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.galleryAdapter.GalleryAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
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
class TenantsAddReportsFragment : Fragment() {

    private lateinit var binding: FragmentTenantsAddReportsBinding

    private val viewModel: TenantsAddReportsViewModel by viewModels()

    private lateinit var progressDialog: ProgressDialog

    // Image and Cropper Result Launcher for Picking and Cropping Image.

    private var captureImage: ActivityResultLauncher<Uri>? = null

    private var pickImage: ActivityResultLauncher<String>? = null

    private var cropImage:
            ActivityResultLauncher<CropImageContractOptions>? = null

    var adapter = GalleryAdapter(::onAddImageClicked, ::onCrossImageClick)

    var dialogAdapter = DialogAdapter(::onDialogItemClick)

    private var dialogBinding: View? = null

    private var dialog: Dialog? = null

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTenantsAddReportsBinding.inflate(inflater, container, false)

        initCollectors()

        initDialog()

        initRecycler()

        initAdapter()

        initProgressDialog()

        initResultLauncher()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getAssignProperties()

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.sendReportUiState){
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
                    sharedViewModel.refreshView(true)
                    findNavController().navigateUp()

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.assignPropertiesUiState){
            when(it){
                is NetworkResult.Error -> {
                    waitingDialog.dismiss()
                    Timber.d("Error -> ${it.message}, ${it.result.toString()}")
                    ToastUtility.errorToast(requireContext(),it.message)

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

                    if (it.result!!.data.isEmpty()){

                        binding.tvPropertyType.isEnabled = false
                        binding.tvPropertyType.hint = "Property not Assigned"
                    }

                    it.result.data.forEachIndexed{index, data ->

                        viewModel.assignPropertyList.add(data)

                    }

                }
            }
        }

    }

    private fun initDialog(){

        dialog = Dialog(requireContext())
        dialogBinding = layoutInflater.inflate(R.layout.dialog_input_properties, null)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setContentView(dialogBinding!!)
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.setCancelable(false)
        val crossBtn = dialogBinding?.findViewById<ImageView>(R.id.dialog_cross)
        dialogAdapter?.setHasStableIds(true)
        crossBtn?.setOnClickListener { dialog?.dismiss() }

    }

    private fun showPropertyDialog(adapterList: MutableList<String>?, title: String) {

        val recyclerView = dialogBinding?.findViewById<RecyclerView>(R.id.dialog_recycler)
        val titleTextView = dialogBinding?.findViewById<TextView>(R.id.dialog_title)
        dialogAdapter.submitList(adapterList)
        recyclerView?.adapter = dialogAdapter
        titleTextView?.text = title
        dialog?.show()

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

        binding.btnSave.setOnClickListener { invalidateInputFields() }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvPropertyType.setOnClickListener {

            viewModel.dialogId = 2

            val list : MutableList<String> = mutableListOf()

            viewModel.assignPropertyList.forEachIndexed{index, data -> list.add(data.name) }

            showPropertyDialog(list, getString(R.string.property_type))
//            activity?.let { it1 ->
//                PropertyTypeDialog {
//                    binding.tvPropertyType.text = it.name
//                    viewModel.propertyType = it.id
//                }.show(it1.supportFragmentManager, "MyCustomFragment")
//            }
        }

        binding.tvRequestType.setOnClickListener {

            viewModel.dialogId = 1

            val list : MutableList<String> = mutableListOf()
            list.add("Maintenance")
            list.add("Incident")
            showPropertyDialog(list, getString(R.string.request_type))

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

        binding.apply {

            val title: String = etRequestTitle.text.toString()

            val requestType: String = tvRequestType.text.toString()

            val propertyType: String = tvPropertyType.text.toString()

            val description: String = etNotes.text.toString()

            if (title.isEmpty()) {

                etRequestTitle.error = "Please enter your title"

            } else if (viewModel.requestType <= 0) {

                ToastUtility.errorToast(requireContext(), "Please select request type")

            } else if (viewModel.propertyType <= 0) {

                ToastUtility.errorToast(requireContext(), "Please select property type")

            } else if (description.isEmpty()) {

                etNotes.error = "Please describe your request"

            } else {

                viewModel.sendReportRequest.title = title
                viewModel.sendReportRequest.request_type_id = viewModel.requestType.toString()
                viewModel.sendReportRequest.property_id = viewModel.propertyType.toString()
                viewModel.sendReportRequest.description = description

                val fileUtils =  FileUtils()
                viewModel.imageBitmapList.forEachIndexed{ index, bitmap ->

                    viewModel.sendReportRequest.images
                        .add(fileUtils.convertBitmapToFile(bitmap, requireContext(), "multiImage${index}.jpeg"))

                }

                viewModel.sendReport()

            }
        }

    }

    private fun onDialogItemClick(data: String, position: Int) {

        when(viewModel.dialogId){

            1 ->{

                binding.tvRequestType.text = data

                if (data.equals("Maintenance")){

                    viewModel.requestType = 1

                }else{

                    viewModel.requestType = 2
                }

            }
            2 ->{

                binding.tvPropertyType.text = data

                viewModel.propertyType = viewModel.assignPropertyList[position].id

            }

        }

        dialog?.dismiss()

    }

}