package com.minutecodes.satelspeed.activities.interfaces

import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationResult

interface LocationRetriever {
    fun onLocationResult(p0: LocationResult)
    fun onLocationAvailability(p0: LocationAvailability)
}