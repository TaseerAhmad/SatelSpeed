package com.minutecodes.satelspeed.activities.cache.location_cache

import android.location.Location
import com.minutecodes.satelspeed.activities.interfaces.LocationCacheRetriever

class LocationCache : LocationCacheRetriever {
    private val locationArray = ArrayList<Location>()

    override fun getCachedLocations(): ArrayList<Location> { //TODO TEMPORARY SOLUTION, REDUCE OBJECT CREATION
        val list = ArrayList<Location>().apply { locationArray.all { add(it) } }
        locationArray.clear() //TODO CONCURRENT MODIFICATION EXCEPTION, CHECK IT
        return list
    }

    fun getAll(): ArrayList<Location> {
        return locationArray
    }

    fun clearCache() {
        locationArray.clear()
    }

    fun store(location: Location) {
        locationArray.add(location)
    }

}