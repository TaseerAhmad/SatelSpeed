package com.minutecodes.satelspeed.activities.util

import android.location.Location
import com.minutecodes.satelspeed.activities.helpers.Acceleration
import com.minutecodes.satelspeed.activities.helpers.GpsAccuracy
import com.minutecodes.satelspeed.activities.interfaces.JobInvokeNotifier
import com.minutecodes.satelspeed.activities.interfaces.LocationCacheRetriever
import com.minutecodes.satelspeed.activities.interfaces.java.LocationFilter
import com.minutecodes.satelspeed.activities.model.LocationData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private typealias JIN = JobInvokeNotifier
private typealias Weak = GpsAccuracy.Weak

class LocationMetaData(retriever: LocationCacheRetriever, filterLoc: LocationFilter) : JIN {
    private val cacheRetriever = retriever
    private val filteredLocation = filterLoc
    private var lastAcceleration = -1F
    private val speedUtil = SpeedUtil()
    private val locationData = LocationData()
    private lateinit var firstLocation: Location
    private lateinit var lastAveragedLocation: Location
    private val distanceUtil = DistanceUtil()

    override fun onJobInvoked() {
        CoroutineScope(Dispatchers.Default).launch {
            val locationList = cacheRetriever.getCachedLocations()
            if (locationList.hasNoItem()) {
                return@launch
            } else if (locationList.hasOneItem()) {
                handleSingleItem(locationList)
                return@launch
            }

            val lastLocation = locationList.last()
            val lastAccuracy = lastLocation.accuracy
            val gpsAccuracy = GpsUtil.getAccuracyStatus(lastAccuracy)

            if (gpsAccuracy == Weak)
                return@launch

            if (lastLocation.hasSpeed()) {
                if (!::firstLocation.isInitialized)
                    firstLocation = lastLocation

                val preComputedSpeed = getPreComputedSpeed(lastLocation)
                lastAcceleration = preComputedSpeed
                lastAveragedLocation = lastLocation.apply { speed = preComputedSpeed }
            } else {
                calculateSpeed(locationList).also {
                    lastAveragedLocation = it
                    lastAcceleration = it.speed
                }
            }

            val metersCovered = distanceUtil.distanceBetween(firstLocation, lastLocation)
            val data = updateLocationData(lastAveragedLocation)
            data.distanceCovered = metersCovered//DistanceUtil.toKilometers(metersCovered)
            filteredLocation.onLocationFiltered(data)
        }
    }

    private fun getPreComputedSpeed(location: Location): Float {
        return if (location.speed >= 1.5)
            location.speed
        else 0F
    }

    private fun calculateSpeed(locationList: ArrayList<Location>): Location { //TODO Separate out speed calculation
        var firstLocation = locationList.first() //TODO Change name
        for (index in 1 until locationList.size)
            firstLocation = getRefinedLocation(locationList[index], firstLocation, lastAcceleration)
        return firstLocation
    }

    private fun getMeanLocation(current: Location, last: Location): Location {
        LocationUtil.getMeanCoordinates(current, last) { lat: Double, long: Double ->
            current.apply {
                latitude = lat
                longitude = long
            }
        }
        return current
    }

    private fun getRefinedLocation(current: Location, last: Location, acc: Float): Location {
        return speedUtil.getRefinedLocation(current, last, acc).run {
            if (this is Acceleration.Computed) {
                val meanCoordinatedLocation = getMeanLocation(current, last)
                current.set(meanCoordinatedLocation)

                if (::firstLocation.isInitialized) {
                    val totalMetersCovered = DistanceUtil.getDistanceCovered(current, firstLocation)
                    val km = DistanceUtil.toKilometers(totalMetersCovered)
                    locationData.distanceCovered = km
                } else firstLocation = last
            }
            current
        }
    }

    private fun updateLocationData(averagedLocation: Location): LocationData {
        averagedLocation.also {
            locationData.apply {
                latitude = it.latitude
                longitude = it.longitude
                accuracy = it.accuracy.roundToInt()
                altitude = it.altitude.roundToInt()
                speed = it.speed
                topSpeed = speedUtil.getTopSpeed(averagedLocation.speed)
            }
        }
        return locationData
    }

    private fun ArrayList<Location>.hasNoItem(): Boolean {
        return size == 0
    }

    private fun ArrayList<Location>.hasOneItem(): Boolean {
        return size == 1
    }

    private fun handleSingleItem(locationList: ArrayList<Location>) {
        //TODO If accuracy was good, then assign as first location
        val lastLocation = locationList.last().apply { speed = 0F }
        if (!::firstLocation.isInitialized)
            firstLocation = lastLocation

        updateLocationData(lastLocation).also { filteredLocation.onLocationFiltered(it) }
    }
}
