package com.jeuxdevelopers.estatepie.utils.location

import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Address
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.location.LocationManager.NETWORK_PROVIDER
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import timber.log.Timber

object LocationRequest {

    fun getCurrentLocation(
        context: Context, fetchLocationListener: FetchLocationListener
    ) {

        val providerClient = LocationServices
            .getFusedLocationProviderClient(context)

        val tokenSource = CancellationTokenSource()

        val locationManager = context
            .getSystemService(LOCATION_SERVICE) as LocationManager

        val isGPSEnable = locationManager.isProviderEnabled(GPS_PROVIDER)

        val isNetworkEnable = locationManager.isProviderEnabled(NETWORK_PROVIDER)

        if (isGPSEnable && isNetworkEnable) {

            try {

                providerClient.lastLocation.addOnSuccessListener { location ->

                    // If last known location is not null fetch current location.

                    location?.let {

                        fetchLocationListener.onLocationRequestComplete()

                        fetchLocationListener.onLocationAvailable(
                            LatLng(location.latitude, location.longitude)
                        )

                    } ?: run {

                        providerClient.getCurrentLocation(

                            PRIORITY_HIGH_ACCURACY, tokenSource.token

                        ).addOnSuccessListener { location ->

                            location?.let {

                                fetchLocationListener.onLocationRequestComplete()

                                fetchLocationListener.onLocationAvailable(
                                    LatLng(location.latitude, location.longitude)
                                )

                            } ?: run {

                                fetchLocationListener.onLocationRequestComplete()
                                fetchLocationListener.onLocationNotAvailable()
                            }

                        }.addOnFailureListener { currentLocationException ->

                            Timber.d("Error -> %s", currentLocationException.message)

                            fetchLocationListener.onLocationRequestComplete()
                            fetchLocationListener.onLocationNotAvailable()

                        }
                    }

                }.addOnFailureListener { lastLocationException ->

                    Timber.d("Error -> %s", lastLocationException.message)

                    providerClient.getCurrentLocation(

                        PRIORITY_HIGH_ACCURACY, tokenSource.token

                    ).addOnSuccessListener { location ->

                        location?.let {

                            fetchLocationListener.onLocationRequestComplete()

                            fetchLocationListener.onLocationAvailable(
                                LatLng(location.latitude, location.longitude)
                            )

                        } ?: run {

                            fetchLocationListener.onLocationRequestComplete()
                            fetchLocationListener.onLocationNotAvailable()
                        }

                    }.addOnFailureListener { currentLocationException ->

                        Timber.d("Error -> %s", currentLocationException.message)

                        fetchLocationListener.onLocationRequestComplete()
                        fetchLocationListener.onLocationNotAvailable()

                    }
                }

            } catch (exception: SecurityException) {

                Timber.d("Error -> %s", exception.message)

                fetchLocationListener.onLocationRequestComplete()
                fetchLocationListener.onLocationPermissionError()
            }

        } else {

            fetchLocationListener.onLocationRequestComplete()
            fetchLocationListener.onLocationProviderNotEnable()

        }

    }

    interface FetchLocationListener {

        fun onLocationNotAvailable()

        fun onLocationRequestComplete()

        fun onLocationPermissionError()

        fun onLocationProviderNotEnable()

        fun onLocationAvailable(latLng: LatLng)

    }

}