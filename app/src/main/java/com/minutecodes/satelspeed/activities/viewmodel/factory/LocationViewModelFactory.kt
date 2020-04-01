package com.minutecodes.satelspeed.activities.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.minutecodes.satelspeed.activities.viewmodel.LocationViewModel

@Suppress("UNCHECKED_CAST")
class LocationViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LocationViewModel(context) as T
    }
}