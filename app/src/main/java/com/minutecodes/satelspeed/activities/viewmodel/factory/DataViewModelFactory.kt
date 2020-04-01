package com.minutecodes.satelspeed.activities.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.minutecodes.satelspeed.activities.viewmodel.DataViewModel

@Suppress("UNCHECKED_CAST")
class DataViewModelFactory(private val context: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.cast(DataViewModel(context)) as T
    }
}