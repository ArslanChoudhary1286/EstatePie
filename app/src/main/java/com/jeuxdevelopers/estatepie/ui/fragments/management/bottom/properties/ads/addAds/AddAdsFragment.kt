package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.ads.addAds

import android.Manifest
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.options
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.jeuxdevelopers.estatepie.BuildConfig
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentAddAdsBinding
import com.jeuxdevelopers.estatepie.databinding.SheetOptionsChoiceBinding
import com.jeuxdevelopers.estatepie.network.responses.management.properties.AmenititesResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.adapter.DialogAdapter
import com.jeuxdevelopers.estatepie.ui.dialogs.adapter.DialogAmenitiesAdapter
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
class AddAdsFragment : Fragment() {

    private lateinit var binding: FragmentAddAdsBinding

    private val viewModel: AddAdsViewModel by viewModels()

    private lateinit var progressDialog: ProgressDialog

    // Image and Cropper Result Launcher for Picking and Cropping Image

    private var captureImage: ActivityResultLauncher<Uri>? = null

    private var pickImage: ActivityResultLauncher<String>? = null

    private var cropImage:
            ActivityResultLauncher<CropImageContractOptions>? = null

    var adapter = GalleryAdapter(::onAddImageClicked, ::onCrossImageClick)

    var dialogAdapter = DialogAdapter(::onDialogItemClick)

    var amenitiesAdapter = DialogAmenitiesAdapter()

    private var dialogBinding: View? = null

    private var amenitieDialogBinding: View? = null

    private var dialog: Dialog? = null

    private var amenitieDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddAdsBinding.inflate(inflater, container, false)

        initCollectors()

        initDialog()

        initAmenityDialog()

        initTextWatchers()

        initRecycler()

        initAdapter()

        initProgressDialog()

        initResultLauncher()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getAllAmenities()

        viewModel.getPropertyTypes()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())

        collectLatestLifecycleFlow(viewModel.addAdsUiState){
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

        collectLatestLifecycleFlow(viewModel.propertyTypesUiState){
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

                    val sortedList = it.result!!.data.sortedBy { it.name }

                    sortedList.forEach { data ->
                        if (data.category_id == 1) {
                            viewModel.propertyTypesResidential.add(data)
                        } else {
                            viewModel.propertyTypesCommercial.add(data)
                        }
                    }

//                    it.result!!.data.forEachIndexed{index, data ->
//
//                        if (data.category_id == 1){
//
//                            viewModel.propertyTypesResidential.add(data)
//
//                        }else{
//
//                            viewModel.propertyTypesCommercial.add(data)
//
//                        }
//
//                    }

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.allAmenitiesUiState){
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

                    it.result!!.data.forEachIndexed{index, data ->

                        viewModel.amenitiesList.add(data)

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

    private fun initAmenityDialog(){

        amenitieDialog = Dialog(requireContext())
        amenitieDialogBinding = layoutInflater.inflate(R.layout.dialog_amenities, null)
        amenitieDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        amenitieDialog?.setContentView(amenitieDialogBinding!!)
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        amenitieDialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        amenitieDialog?.setCancelable(false)
        val crossBtn = amenitieDialogBinding?.findViewById<ImageView>(R.id.dialog_cross)
        val okBtn = amenitieDialogBinding?.findViewById<MaterialButton>(R.id.btnOk)
        dialogAdapter?.setHasStableIds(true)
        crossBtn?.setOnClickListener { amenitieDialog?.dismiss() }
        okBtn?.setOnClickListener {

            amenitiesAdapter.amenitiesName.forEachIndexed{index, data ->

                if (data.isNotEmpty()){
                    if (viewModel.amenitiesName.isEmpty()){
                        viewModel.amenitiesName = data
                    }else{
                        viewModel.amenitiesName = viewModel.amenitiesName + ", " + data
                    }
                }

            }

            amenitiesAdapter.amenitiesId.forEachIndexed{index, data ->

                if (data.isNotEmpty()){

                    if (viewModel.amenitiesId.isEmpty()){
                        viewModel.amenitiesId = data
                    }else{
                        viewModel.amenitiesId = viewModel.amenitiesId + ", " + data
                    }
                }

            }

            binding.tvAmenities.text = viewModel.amenitiesName

            amenitieDialog?.dismiss()
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

        binding.btnBack.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.etAddress.setOnClickListener { pickPlaceFromMap() }

        binding.btnPost.setOnClickListener { invalidateInputFields() }

        binding.tvSelectCategory.setOnClickListener{

            //  to analyze dialog click
            viewModel.dialogId = 1

            showPropertyDialog(
                resources.getStringArray(R.array.category).toMutableList(),
                getString(R.string.select_category_title)
            )

            binding.tvPropertyType.text = ""

        }

        binding.tvPropertyType.setOnClickListener{

            viewModel.dialogId = 2

            val list : MutableList<String> = mutableListOf()

            if (binding.tvSelectCategory.text.toString().equals(getString(R.string.residential))){

                viewModel.propertyTypesResidential.forEachIndexed{index, data -> list.add(data.name) }

            }else{

                viewModel.propertyTypesCommercial.forEachIndexed{index, data -> list.add(data.name) }

            }

            showPropertyDialog(list, getString(R.string.property_type))

        }

        binding.tvForSale.setOnClickListener{

            viewModel.dialogId = 3

            showPropertyDialog(
                resources.getStringArray(R.array.for_sale_for_rent).toMutableList(),
                getString(R.string.for_sale_for_rent_title)
            )

        }

        binding.tvLeaseDuration.setOnClickListener{

            viewModel.dialogId = 4

            showPropertyDialog(
                resources.getStringArray(R.array.lease_duration_array).toMutableList(),
                getString(R.string.lease_duration)
            )

        }

        binding.tvUnit.setOnClickListener{

            viewModel.dialogId = 5

            showPropertyDialog(
                resources.getStringArray(R.array.unit_array).toMutableList(),
                getString(R.string.unit)
            )

        }

        binding.tvFees.setOnClickListener{

            viewModel.dialogId = 6

            showPropertyDialog(
                resources.getStringArray(R.array.fees).toMutableList(),
                getString(R.string.fees_title)
            )

        }

        binding.tvBroker.setOnClickListener{

            viewModel.dialogId = 7

            showPropertyDialog(
                resources.getStringArray(R.array.broker).toMutableList(),
                getString(R.string.broker_title)
            )

        }

        binding.tvSmoking.setOnClickListener{

            viewModel.dialogId = 8

            showPropertyDialog(
                resources.getStringArray(R.array.fees).toMutableList(),
                getString(R.string.smoking_title)
            )

        }

        binding.tvPets.setOnClickListener{

            viewModel.dialogId = 9

            showPropertyDialog(
                resources.getStringArray(R.array.fees).toMutableList(),
                getString(R.string.pets_title)
            )

        }

        binding.tvAmenities.setOnClickListener{

            viewModel.amenitiesName = ""
            viewModel.amenitiesId = ""

            amenitiesAdapter.amenitiesName.clear()
            amenitiesAdapter.amenitiesId.clear()

            showAmenitiesDialog(
                viewModel.amenitiesList,
                getString(R.string.amenities_title)
            )

        }

    }

    private fun showPropertyDialog(adapterList: MutableList<String>?, title: String) {

        val recyclerView = dialogBinding?.findViewById<RecyclerView>(R.id.dialog_recycler)
        val titleTextView = dialogBinding?.findViewById<TextView>(R.id.dialog_title)
        dialogAdapter.submitList(adapterList)
        recyclerView?.adapter = dialogAdapter
        titleTextView?.text = title
        dialog?.show()

    }

    private fun showAmenitiesDialog(adapterList: MutableList<AmenititesResponse.Data>, title: String) {

        val recyclerView = amenitieDialogBinding?.findViewById<RecyclerView>(R.id.dialog_recycler)
        val titleTextView = amenitieDialogBinding?.findViewById<TextView>(R.id.dialog_title)
        val btnOk = amenitieDialogBinding?.findViewById<TextView>(R.id.btnOk)
        amenitiesAdapter.submitList(adapterList)
        recyclerView?.adapter = amenitiesAdapter
        titleTextView?.text = title
        amenitieDialog?.show()

    }

    private fun setFeeWithBroker(){

        if (binding.tvBroker.text == resources.getString(R.string.no)){

            binding.tvBroker.alpha = 0.5f
            binding.tvBroker.isEnabled = false

        }else{

            binding.tvBroker.alpha = 1f
            binding.tvBroker.isEnabled = true
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

                uris.forEachIndexed { index, uri ->

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

//                viewModel.imageUri = result.uriContent
//
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

    private fun invalidateInputFields() {

        val buildingName: String = binding.etBuildingName.text.toString()
        val category: String = binding.tvSelectCategory.text.toString()
        val propertType: String = binding.tvPropertyType.text.toString()
        val noOfBeds: String = binding.etNoOfBad.text.toString()
        val forSale: String = binding.tvForSale.text.toString()
        val unit: String = binding.tvUnit.text.toString()
        val size: String = binding.etSize.text.toString()
        val fees: String = binding.tvFees.text.toString()
        val broker: String = binding.tvBroker.text.toString()
        val price: String = binding.etPrice.text.toString()
        val address: String = binding.etAddress.text.toString()
        val description: String = binding.etDescription.text.toString()
        val leasingTerms: String = binding.etLeasingTerms.text.toString()
        val smoking: String = binding.tvSmoking.text.toString()
        val pets: String = binding.tvPets.text.toString()
        val bathroom: String = binding.etBathrooms.text.toString()
        var multiUnitsAvailable: Boolean = false
        var available: Boolean = false
        if (binding.multiUnitCheck.isChecked)
            multiUnitsAvailable = true
        if (binding.availableCheck.isChecked)
            available = true


        if (TextUtils.isEmpty(buildingName)){

            binding.etBuildingName.error = getString(R.string.enter_building_name)

        }else if (TextUtils.isEmpty(category)){

            binding.tvSelectCategory.error = getString(R.string.select_category)

        }else if (TextUtils.isEmpty(propertType)){

            binding.tvPropertyType.error = getString(R.string.empty_field)

        }else if (TextUtils.isEmpty(forSale)){

            binding.tvForSale.error = getString(R.string.empty_field)

        }else if (TextUtils.isEmpty(unit)){

            binding.tvUnit.error = getString(R.string.empty_field)

        }else if (TextUtils.isEmpty(size)){

            binding.etSize.error = getString(R.string.empty_field)

        }else if (TextUtils.isEmpty(fees)){

            binding.tvFees.error = getString(R.string.empty_field)

        }else if (TextUtils.isEmpty(broker)){

            binding.tvBroker.error = getString(R.string.empty_field)

        }else if (TextUtils.isEmpty(price)){

            binding.etPrice.error = getString(R.string.empty_field)

        }else if (TextUtils.isEmpty(smoking)){

            binding.tvSmoking.error = getString(R.string.empty_field)

        }else if (TextUtils.isEmpty(pets)){

            binding.tvPets.error = getString(R.string.empty_field)

        }else if (TextUtils.isEmpty(bathroom)){

            binding.etBathrooms.error = getString(R.string.empty_field)

        }else if (TextUtils.isEmpty(address)){

            binding.etAddress.error = getString(R.string.enter_building_location)

        }else if (TextUtils.isEmpty(description)){

            binding.etDescription.error = getString(R.string.empty_field)

        }else if (TextUtils.isEmpty(leasingTerms)){

            binding.etLeasingTerms.error = getString(R.string.empty_field)

        }else{

            viewModel.addAdsRequest.name = buildingName
            viewModel.addAdsRequest.category = category
            viewModel.addAdsRequest.property_type = viewModel.property_type_id.toString()
            viewModel.addAdsRequest.beds = noOfBeds
            viewModel.addAdsRequest.purpose = forSale
            viewModel.addAdsRequest.lease = viewModel.lease.toString()
            viewModel.addAdsRequest.amenities = viewModel.amenitiesId
            viewModel.addAdsRequest.unit = unit
            viewModel.addAdsRequest.size = size
            viewModel.addAdsRequest.fees = fees
            viewModel.addAdsRequest.broker = broker
            viewModel.addAdsRequest.price = price
            viewModel.addAdsRequest.address = address
            viewModel.addAdsRequest.description = description
            viewModel.addAdsRequest.lease_terms = leasingTerms
            viewModel.addAdsRequest.smoking = smoking
            viewModel.addAdsRequest.pets = pets
            viewModel.addAdsRequest.bathroom = bathroom
            if (multiUnitsAvailable)
                viewModel.addAdsRequest.multiunits = "1"
            else
                viewModel.addAdsRequest.multiunits = "0"
            if (available)
                viewModel.addAdsRequest.available = "1"
            else
                viewModel.addAdsRequest.available = "0"
            viewModel.addAdsRequest.latitude = viewModel.placeLocation?.latitude.toString()
            viewModel.addAdsRequest.longitude = viewModel.placeLocation?.longitude.toString()

            val fileUtils =  FileUtils()
            viewModel.imageBitmapList.forEachIndexed{index, bitmap ->

                viewModel.addAdsRequest.images
                    .add(fileUtils.convertBitmapToFile(bitmap, requireContext(), "multiImage${index}.jpeg"))

            }

            viewModel.addAdsProperty()

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

    private fun onDialogItemClick(data: String, position: Int) {

        when(viewModel.dialogId){
            1 -> {
                binding.tvSelectCategory.text = data
                if (data.equals(getString(R.string.commercial))){
                    binding.etNoOfBad.text = null
                    binding.etNoOfBad.isEnabled = false
                }else{
                    binding.etNoOfBad.isEnabled = true
                }
            }
            2 -> {
                binding.tvPropertyType.text = data

                if (binding.tvSelectCategory.text.equals(getString(R.string.residential)))
                    viewModel.property_type_id = viewModel.propertyTypesResidential[position].id
                else
                    viewModel.property_type_id = viewModel.propertyTypesCommercial[position].id
            }
            3 -> {
                binding.tvForSale.text = data

                if (data.equals(getString(R.string.for_sale))){
                    binding.tvLeaseDuration.text = null
                    viewModel.lease = 0
                    binding.tvLeaseDuration.isEnabled = false
                }else{
                    viewModel.lease = 1
                    binding.tvLeaseDuration.isEnabled = true
                }

            }
            4 -> {
                binding.tvLeaseDuration.text = data
                viewModel.lease = position+1
            }
            5 -> {
                binding.tvUnit.text = data
            }
            6 -> {
                binding.tvFees.text = data
            }
            7 -> {
                binding.tvBroker.text = data
            }
            8 -> {
                binding.tvSmoking.text = data
            }
            9 -> {
                binding.tvPets.text = data
            }

        }
        dialog?.dismiss()
    }


}