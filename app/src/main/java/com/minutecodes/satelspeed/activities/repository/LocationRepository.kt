package com.minutecodes.satelspeed.activities.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.minutecodes.satelspeed.activities.cache.location_cache.CacheJobScheduler
import com.minutecodes.satelspeed.activities.cache.location_cache.LocationCache
import com.minutecodes.satelspeed.activities.cache.location_cache.LocationDataCache
import com.minutecodes.satelspeed.activities.interfaces.LocationRetriever
import com.minutecodes.satelspeed.activities.interfaces.java.LocationFilter
import com.minutecodes.satelspeed.activities.model.LocationData
import com.minutecodes.satelspeed.activities.util.LocationMetaData
import com.minutecodes.satelspeed.activities.util.SpeedUtil
import com.minutecodes.satelspeed.activities.wrappers.UserLocation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationRepository(context: Context) {
    private val fusedLocationClient = getFusedLocationClient(context)
    private val userLocation by lazy { setupUserLocation() }
    private val locationCache = LocationCache()
    private val locationFilter = setupLocationFilter()
    private val cacheJobScheduler = CacheJobScheduler(locationFilter)
    private val locationDataCache = LocationDataCache()
    private val locationAvailability = MutableLiveData<Boolean>()

    fun locationData(): LiveData<LocationData>? {
        return locationDataCache.get()
    }

    fun locationAvailability(): LiveData<Boolean> {
        return locationAvailability
    }

    fun startLocationUpdates() {
        cacheJobScheduler.startScheduler()
        userLocation.observeLocationUpdates()
    }

    fun stopLocationUpdates() {
        cacheJobScheduler.stopScheduler()
        userLocation.removeLocationUpdates()
    }

    fun getLocationRequest(): LocationRequest {
        return userLocation.getLocationRequest()
    }

    private fun setupUserLocation(): UserLocation {
        return UserLocation(fusedLocationClient, object : LocationRetriever {
            override fun onLocationResult(p0: LocationResult) {
                locationCache.store(p0.lastLocation)
            }

            override fun onLocationAvailability(p0: LocationAvailability) {
                locationAvailability.value = p0.isLocationAvailable
            }
        })
    }

    private fun getFusedLocationClient(context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    private fun setupLocationFilter(): LocationMetaData {
        return LocationMetaData(locationCache, LocationFilter {
            it.accuracy = computeGpsSignalStrength(it.accuracy)
            locationDataCache.store(it)
        })
    }

    private fun computeGpsSignalStrength(accuracy: Int): Int { //TODO Make a better signal computer
        return when (accuracy) {
            in 15..20 -> 25
            in 10..12 -> 50
            in 7..9 -> 75
            in 1..6 -> 100
            else -> 0
        }
    }
}