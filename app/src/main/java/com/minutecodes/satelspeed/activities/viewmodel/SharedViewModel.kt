package com.minutecodes.satelspeed.activities.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.minutecodes.satelspeed.activities.model.LocationData

class SharedViewModel : ViewModel() {
    private val mutableLiveLocationData = MutableLiveData<LocationData>()

    fun cacheLocationData(data: LocationData) {
        mutableLiveLocationData.value = data
    }

    fun locationData() : LiveData<LocationData> {
        return mutableLiveLocationData
    }
}