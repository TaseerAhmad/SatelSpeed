package com.minutecodes.satelspeed.activities.interfaces

import android.location.Location

interface LocationCacheRetriever {
    fun getCachedLocations(): ArrayList<Location>
}