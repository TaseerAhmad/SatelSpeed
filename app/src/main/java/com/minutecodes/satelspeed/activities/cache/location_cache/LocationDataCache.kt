package com.minutecodes.satelspeed.activities.cache.location_cache

import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.minutecodes.satelspeed.activities.model.LocationData

class LocationDataCache {
    private val cache = MutableLiveData<LocationData>()

    fun store(locationData: LocationData) {
        if (Thread.currentThread().name != Looper.getMainLooper().thread.name)
            cache.postValue(locationData)
        else
            cache.value = locationData
    }

    fun remove() {
        cache.value = null
    }

    fun get(): MutableLiveData<LocationData>? {
        return cache
    }

}