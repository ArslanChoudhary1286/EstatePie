package com.jeuxdevelopers.estatepie.ui.activities.splash

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ActivitySplashBinding
import com.jeuxdevelopers.estatepie.ui.activities.auth.AuthActivity
import com.jeuxdevelopers.estatepie.ui.activities.managment.MainManagementActivity
import com.jeuxdevelopers.estatepie.ui.activities.tenants.MainTenantsActivity
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import com.jeuxdevelopers.estatepie.utils.TokenManager
import com.jeuxdevelopers.estatepie.utils.location.LocationRequest
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity(), LocationListener{

    private lateinit var binding: ActivitySplashBinding

    @Inject
    lateinit var tokenManager: TokenManager

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)

        setContentView(binding.root)

//        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        val location: Location? = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//        val longitude: Double = location?.getLongitude()
//        val latitude: Double = location.getLatitude()

//        getCurrentLocation()
        
        initCollectors()

    }

    private fun getCurrentLocation() {

        Dexter.withContext(applicationContext).withPermissions(

            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION

        ).withListener(object : MultiplePermissionsListener {

            override fun onPermissionsChecked(report: MultiplePermissionsReport) {

                if (report.areAllPermissionsGranted()) {


                    LocationRequest.getCurrentLocation(
                        applicationContext,
                        object : LocationRequest.FetchLocationListener {

                            override fun onLocationNotAvailable() {

                                ToastUtility.errorToast(
                                    applicationContext, R.string.current_location_not_available
                                )

                            }

                            override fun onLocationRequestComplete() {


                            }

                            override fun onLocationPermissionError() {

                                ToastUtility.errorToast(
                                    applicationContext, R.string.access_location_permission_required
                                )

                            }

                            override fun onLocationProviderNotEnable() {

                                ToastUtility.errorToast(
                                    applicationContext, R.string.location_services_are_not_enabled
                                )

                            }

                            override fun onLocationAvailable(latLng: LatLng) {

                                tokenManager.saveLatitude(latLng.latitude.toString())
                                tokenManager.saveLongitude(latLng.longitude.toString())

                            }

                        })

                } else {

                    ToastUtility.errorToast(
                        applicationContext, getString(R.string.access_location_permission_required)
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

    private fun initCollectors() {

        collectLatestLifecycleFlow(viewModel.splashUiState) {
            if (it) {
                if (viewModel.getCurrentUser() != null) {

                    if (viewModel.getCurrentUser()!!.role_id == 3 || viewModel.getCurrentUser()!!.role_id == 5) {
                        navigateToNextScreenTenant()
                    }else{
                        navigateToNextScreenMain()
                    }

                } else {
                    navigateToNextScreenAuth()
                }
            }

        }
    }


    private fun navigateToNextScreenAuth() {

        Intent(this, AuthActivity::class.java).apply {
            startActivity(this)
            finishAffinity()
        }

    }

    private fun navigateToNextScreenMain() {

        Intent(this, MainManagementActivity::class.java).apply {
            startActivity(this)
            finishAffinity()
        }

    }

    private fun navigateToNextScreenTenant() {

        Intent(this, MainTenantsActivity::class.java).apply {
            startActivity(this)
            finishAffinity()
        }

    }

    override fun onLocationChanged(location: Location) {
        ToastUtility.successToast(this, "loc" + location.latitude)
    }
}