package com.minutecodes.satelspeed.activities.util

import android.location.Location

class DistanceUtil {
    private val distanceCalculationArray by lazy { FloatArray(3) }

    companion object {
        fun getDistanceCovered(current: Location, last: Location): Float {
            return current.distanceTo(last)
        }

        fun formatDistance(meters: Float, unit: Float): Float {
            return meters / unit
        }

        fun toKilometers(meters: Float): Float {
            return meters / 1000
        }
    }

    fun distanceBetween(start: Location, end: Location): Float {
        val lastLat = start.latitude
        val lastLong = start.longitude
        val currLat = end.latitude
        val currLong = end.longitude
        Location.distanceBetween(lastLat, lastLong, currLat,currLong, distanceCalculationArray)
        return distanceCalculationArray[0]
    }
}