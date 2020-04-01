package com.minutecodes.satelspeed.activities.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.minutecodes.satelspeed.activities.KILOMETER
import com.minutecodes.satelspeed.activities.KMH_VALUE
import com.minutecodes.satelspeed.activities.model.LocationData
import com.minutecodes.satelspeed.activities.util.DistanceUtil
import com.minutecodes.satelspeed.activities.util.SpeedUtil
import java.util.*


class SharedLocationViewModel : ViewModel() {
    private val locationData = MutableLiveData<LocationData>()
    private val locationAvailability = MutableLiveData<Boolean>()
    private var speedUnit = KMH_VALUE
    private var distanceUnit = KILOMETER
    private val defaultLocale = Locale.getDefault()
    //    private val decimalFormat = DecimalFormat("#.#")

    fun setLocation(data: LocationData) {
        with(data) {
            distanceCovered = DistanceUtil.formatDistance(distanceCovered, distanceUnit)
            distanceCovered = String.format(defaultLocale, "%.1f", distanceCovered).toFloat()
            speed = SpeedUtil.formatSpeed(speed, speedUnit)
            locationData.value = this
        }
    }

    fun setSystemUnits(speedUnit: Float, distanceUnit: Float) {
        this.speedUnit = speedUnit
        this.distanceUnit = distanceUnit
    }

    fun setLocationAvailability(isAvailable: Boolean) {
        locationAvailability.value = isAvailable
    }

    fun locationData(): LiveData<LocationData> {
        return locationData
    }

    fun locationAvailability(): LiveData<Boolean> {
        return locationAvailability
    }

}