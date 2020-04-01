package com.minutecodes.satelspeed.activities.util

import com.minutecodes.satelspeed.activities.GOOD_ACCURACY_RANGE
import com.minutecodes.satelspeed.activities.helpers.GpsAccuracy
import kotlin.math.roundToInt

class GpsUtil {

    companion object {
        fun getAccuracyStatus(accuracy: Float): GpsAccuracy {
            return if (hasGoodAccuracy(accuracy))
                GpsAccuracy.Good
            else
                GpsAccuracy.Weak
        }

        private fun hasGoodAccuracy(accuracy: Float): Boolean {
            return accuracy.roundToInt().run { this != 0 && this <= GOOD_ACCURACY_RANGE }
        }
    }

}