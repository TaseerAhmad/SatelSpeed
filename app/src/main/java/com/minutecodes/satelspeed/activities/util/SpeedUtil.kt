package com.minutecodes.satelspeed.activities.util

import android.location.Location
import android.util.Log
import com.minutecodes.satelspeed.activities.ACCEPTABLE_ACCELERATION_DIFFERENCE
import com.minutecodes.satelspeed.activities.MAX_ACCELERATION_THRESHOLD
import com.minutecodes.satelspeed.activities.STOPPED_ACCELERATION
import com.minutecodes.satelspeed.activities.helpers.Acceleration
import kotlin.math.abs
import kotlin.math.ceil

class SpeedUtil {
    private val computeSuccess = Acceleration.Computed()
    private var maxSpeed = 0F

    fun getTopSpeed(currentSpeed: Float): Float {
        if (currentSpeed > maxSpeed)
            maxSpeed = currentSpeed

        return maxSpeed
    }


    fun getRefinedLocation(current: Location, last: Location, acc: Float): Acceleration {
        var computeStatus: Acceleration = Acceleration.NotComputed
        val speed = current.distanceTo(last)
        if (speed != STOPPED_ACCELERATION && speed >= 1.5) {
            if (speed < MAX_ACCELERATION_THRESHOLD) {
                if (acc != -1F) {
                    val accelerationDifference = abs(speed - acc)
                    if (accelerationDifference <= ACCEPTABLE_ACCELERATION_DIFFERENCE) {
                        current.apply { this.speed = speed }
                        computeStatus = computeSuccess.apply { location = current }
                    }
                }
            }
        } else current.speed = 0F

        return computeStatus
    }

    companion object {
        fun formatSpeed(speed: Float, speedUnit: Float): Float {
            return ceil(speed * speedUnit)
        }
    }
}