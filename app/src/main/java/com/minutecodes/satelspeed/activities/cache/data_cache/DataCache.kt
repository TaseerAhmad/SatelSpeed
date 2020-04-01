package com.minutecodes.satelspeed.activities.cache.data_cache

import androidx.lifecycle.MutableLiveData
import com.minutecodes.satelspeed.activities.model.SpeedHistory

class DataCache {
    private val historyCache = MutableLiveData<List<SpeedHistory>>()

    fun store(speedHistory: List<SpeedHistory>?) {
        historyCache.postValue(speedHistory)
    }

    fun get(): MutableLiveData<List<SpeedHistory>> {
        return historyCache
    }

    fun remove() {
        historyCache.value = null
    }
}