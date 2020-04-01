package com.minutecodes.satelspeed.activities.wrappers

import android.annotation.SuppressLint
import android.os.Looper
import com.google.android.gms.location.*
import com.minutecodes.satelspeed.activities.LOCATION_UPDATE_INTERVAL
import com.minutecodes.satelspeed.activities.interfaces.LocationRetriever

private typealias FusedClient = FusedLocationProviderClient

class UserLocation(fusedLocation: FusedClient, lr: LocationRetriever) : LocationCallback() {
    private val fusedClient = fusedLocation
    private val locationRetriever = lr
    private val locationRequest = getLocationRequestInstance()

    override fun onLocationResult(p0: LocationResult?) {
        p0 ?: return
        locationRetriever.onLocationResult(p0)
    }

    override fun onLocationAvailability(p0: LocationAvailability?) {
        p0 ?: return
        locationRetriever.onLocationAvailability(p0)
    }

    fun removeLocationUpdates() {
        fusedClient.removeLocationUpdates(this)
    }

    @SuppressLint("MissingPermission")
    fun observeLocationUpdates() {
        fusedClient.requestLocationUpdates(locationRequest, this, null)
    }

    fun getLocationRequest(): LocationRequest {
        return locationRequest
    }

    private fun getLocationRequestInstance(): LocationRequest {
        return LocationRequest().apply {
            interval = LOCATION_UPDATE_INTERVAL
            fastestInterval = LOCATION_UPDATE_INTERVAL
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

}