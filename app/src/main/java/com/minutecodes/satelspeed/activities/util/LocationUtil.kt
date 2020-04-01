package com.minutecodes.satelspeed.activities.util

import android.location.Location

private typealias MeanCoordinates = (Double, Double) -> Unit

class LocationUtil {

    companion object {
        fun getMeanLocation(currentLocation: Location, lastLocation: Location): Location {
            val meanLat = getMean(currentLocation.latitude, lastLocation.latitude)
            val meanLong = getMean(currentLocation.longitude, lastLocation.longitude)
            val meanAcc = getMean(currentLocation.accuracy, lastLocation.accuracy)
            val meanAlt = getMean(currentLocation.altitude, lastLocation.altitude)
            val meanTime = getMean(currentLocation.time, lastLocation.time)
            val meanBear = getMean(currentLocation.bearing, lastLocation.bearing)

            return currentLocation.apply {
                latitude = meanLat
                longitude = meanLong
                accuracy = meanAcc
                altitude = meanAlt
                time = meanTime
                bearing = meanBear
            }
        }

        fun getMeanCoordinates(current: Location, last: Location, block: MeanCoordinates) {
            val meanLat = getMean(last.latitude, current.latitude)
            val meanLong = getMean(last.longitude, current.longitude)
            block(meanLat, meanLong)
        }


        private fun getMean(p0: Double, p1: Double) = (p0 + p1) / 2

        private fun getMean(p0: Long, p1: Long) = (p0 + p1) / 2

        private fun getMean(p0: Float, p1: Float) = (p0 + p1) / 2
    }

}