package com.minutecodes.satelspeed.activities.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationRequest
import com.minutecodes.satelspeed.activities.model.LocationData
import com.minutecodes.satelspeed.activities.repository.LocationRepository
import com.minutecodes.satelspeed.activities.util.DistanceUtil
import com.minutecodes.satelspeed.activities.util.SpeedUtil

class LocationViewModel(context: Context) : ViewModel() {
    private val locationRepository = LocationRepository(context)

    fun locationData(): LiveData<LocationData>? {
        return locationRepository.locationData()
    }

    fun getLocationRequest(): LocationRequest {
        return locationRepository.getLocationRequest()
    }

    fun locationAvailability(): LiveData<Boolean> {
        return locationRepository.locationAvailability()
    }

    fun startLocationUpdates() {
        locationRepository.startLocationUpdates()
    }

    fun stopLocationUpdates() {
        locationRepository.stopLocationUpdates()
    }

}