package com.minutecodes.satelspeed.activities.helpers

sealed class GpsAccuracy {
    object Good : GpsAccuracy()
    object Weak : GpsAccuracy()
}
