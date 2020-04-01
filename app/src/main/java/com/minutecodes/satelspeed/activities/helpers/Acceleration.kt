package com.minutecodes.satelspeed.activities.helpers

import android.location.Location

sealed class Acceleration {
    class Computed : Acceleration() {
        lateinit var location: Location
    }

    object NotComputed : Acceleration()
}