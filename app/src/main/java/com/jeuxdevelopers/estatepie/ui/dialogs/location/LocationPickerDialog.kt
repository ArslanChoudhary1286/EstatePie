package com.jeuxdevelopers.estatepie.ui.dialogs.location

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.DialogPickLocationBinding
import com.jeuxdevelopers.estatepie.ui.dialogs.progress.ProgressDialog
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import com.jeuxdevelopers.estatepie.utils.format.LocationFormat.getLatLng
import com.jeuxdevelopers.estatepie.utils.location.LocationRequest
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import java.util.concurrent.Executors

@AndroidEntryPoint
class LocationPickerDialog(

    private val locationResultListener: LocationResultListener
) : DialogFragment() {

    companion object LocationPickerDialog {
        const val TAG: String = "LocationPickerDialog"
    }

    private var _binding: DialogPickLocationBinding? = null

    private val binding get() = _binding!!

    private var googleMap: GoogleMap? = null

    private var googleMarker: Marker? = null

    private lateinit var progressDialog: ProgressDialog

    private val pickerViewModel: LocationPickerViewModel by viewModels()

    private var placesLauncher: ActivityResultLauncher<Intent>? = null

    override fun getTheme(): Int {

        return R.style.DialogFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?
    ): View {

        _binding = DialogPickLocationBinding
            .inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListener()

        initProgressDialog()

        initResultLauncher()

        initGooglePlacesSdk()

        initGoogleMapFragment()

    }

    private fun initClickListener() {

        binding.nextButton.setOnClickListener {
            sendLocationResult()
        }

        binding.backButton.setOnClickListener {
            dismiss()
        }

        binding.locationButton.setOnClickListener {
            getCurrentLocation()
        }

        binding.searchButton.setOnClickListener {
            openPlacesActivity()
        }
    }

    private fun initResultLauncher() {

        placesLauncher = registerForActivityResult(StartActivityForResult()) { result ->

            result?.data?.let { intent ->

                try {

                    val place = Autocomplete
                        .getPlaceFromIntent(intent)

                    // Saving the Location Fetched in ViewModel.

                    pickerViewModel.latLng = place.latLng

                    pickerViewModel.latLng?.let { latLng ->

                        geocodeLocation(latLng)

                        displayMapMarker(latLng)

                    }

                } catch (exception: Exception) {

                    Timber.d("Error -> %s", exception.message)
                }

            }
        }

    }

    private fun sendLocationResult() {

        // If address and latLng is not null.

        pickerViewModel.address?.let { address ->

            pickerViewModel.latLng?.let { latLng ->

                locationResultListener.onLocationReceived(
                    address, latLng
                )
                // Dismissing the location dialog.

                dismiss()
            }
        }
    }

    private fun openPlacesActivity() {

        val fields: List<Place.Field> = listOf(
            Place.Field.ID, Place.Field.NAME,
            Place.Field.ADDRESS, Place.Field.LAT_LNG
        )

        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.OVERLAY, fields
        ).build(requireContext())

        placesLauncher?.launch(intent)
    }

    private fun initProgressDialog() {

        progressDialog = ProgressDialog(requireContext())

    }


    private fun getCurrentLocation() {

        Dexter.withContext(requireContext()).withPermissions(

            ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION

        ).withListener(object : MultiplePermissionsListener {

            override fun onPermissionsChecked(report: MultiplePermissionsReport) {

                if (report.areAllPermissionsGranted()) {

                    progressDialog.showDialogue()

                    LocationRequest.getCurrentLocation(
                        requireContext(),
                        object : LocationRequest.FetchLocationListener {

                            override fun onLocationNotAvailable() {

                                ToastUtility.errorToast(
                                    requireContext(), R.string.current_location_not_available
                                )

                            }

                            override fun onLocationRequestComplete() {

                                progressDialog.hideDialogue()
                            }

                            override fun onLocationPermissionError() {

                                ToastUtility.errorToast(
                                    requireContext(), R.string.access_location_permission_required
                                )

                            }

                            override fun onLocationProviderNotEnable() {

                                ToastUtility.errorToast(
                                    requireContext(), R.string.location_services_are_not_enabled
                                )

                            }

                            override fun onLocationAvailable(latLng: LatLng) {

                                pickerViewModel.latLng = latLng

                                geocodeLocation(latLng)

                                displayMapMarker(latLng)

                            }

                        })

                } else {

                    ToastUtility.errorToast(
                        requireContext(), getString(R.string.access_location_permission_required)
                    )

                }

            }

            override fun onPermissionRationaleShouldBeShown(
                list: MutableList<PermissionRequest>,
                permissionToken: PermissionToken
            ) {

                permissionToken.continuePermissionRequest()
            }

        }).check()

    }


    private fun initGooglePlacesSdk() {

        if (!Places.isInitialized()) {

            Places.initialize(
                requireContext(), getString(R.string.google_map_key)
            )
        }
    }

    private fun initGoogleMapFragment() {

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment

        mapFragment.getMapAsync { googleMap ->

            this.googleMap = googleMap

            getCurrentLocation()

        }

    }


    private fun geocodeLocation(latLng: LatLng) {

        progressDialog.showDialogue()

        val executor = Executors.newSingleThreadExecutor()

        val handler = Handler(Looper.getMainLooper())

        executor.execute {

            try {

                val geocoder = Geocoder(requireContext(), Locale.getDefault())

                val addressList: List<Address> = geocoder.getFromLocation(
                    latLng.latitude, latLng.longitude, 1
                )

                // Saving the Address Fetched in ViewModel.

                pickerViewModel.address = addressList[0]

            } catch (exception: Exception) {

                Timber.d("Error -> %s", exception.message)
            }

            handler.post {

                progressDialog.hideDialogue()
            }
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    private fun displayMapMarker(latLng: LatLng) {

        googleMarker?.remove()

        val markerImage: Bitmap = BitmapFactory
            .decodeResource(resources, R.drawable.ic_map_marker)

        val scaleMarker: Bitmap = Bitmap.createScaledBitmap(
            markerImage, 96, 96, false
        )

        googleMap?.let { googleMap ->

            googleMarker = googleMap.addMarker(

                MarkerOptions().position(latLng).title(getLatLng(latLng))
                    .icon(fromBitmap(scaleMarker)).draggable(true)
            )

            googleMap.moveCamera(newLatLngZoom(latLng, 18f))

            googleMap.setOnMarkerDragListener(object : OnMarkerDragListener {

                override fun onMarkerDrag(marker: Marker) = Unit

                override fun onMarkerDragEnd(marker: Marker) {

                    pickerViewModel.latLng = marker.position

                    marker.title = getLatLng(marker.position)

                    pickerViewModel.latLng?.let { latLng ->

                        geocodeLocation(latLng)
                    }

                }

                override fun onMarkerDragStart(marker: Marker) = Unit

            })

        }

    }

    interface LocationResultListener {

        fun onLocationReceived(address: Address, latLng: LatLng)

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null

    }

}