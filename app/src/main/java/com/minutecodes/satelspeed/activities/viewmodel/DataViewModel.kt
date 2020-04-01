package com.minutecodes.satelspeed.activities.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.minutecodes.satelspeed.activities.KILOMETER
import com.minutecodes.satelspeed.activities.KMH_VALUE
import com.minutecodes.satelspeed.activities.model.SpeedHistory
import com.minutecodes.satelspeed.activities.repository.DataRepository

class DataViewModel(context: Application) : ViewModel() {
    private val dataRepository = DataRepository(context)
    private lateinit var historyCache: LiveData<List<SpeedHistory>>

    fun saveSpeedUnit(unit: Float) {
        dataRepository.saveSpeedUnit(unit)
    }

    fun getSpeedUnit(): Float {
        return dataRepository.getSpeedUnit()
    }

    fun saveDistanceUnit(unit: Float) {
        dataRepository.saveDistanceUnit(unit)
    }

    fun getDistanceUnit(): Float {
        return dataRepository.getDistanceUnit()
    }

    fun getMapCameraHeight(): Double {
        return dataRepository.getMapCameraHeight().toDouble()
    }

    fun saveMapCameraHeight(height: Double) {
        dataRepository.saveMapCameraHeight(height.toInt())
    }

    fun getMapThemeId(): String {
        return dataRepository.getMapThemeId()
    }

    fun saveMapThemeId(id: String) {
        dataRepository.saveMapThemeId(id)
    }


    fun getMapCamTilt(): Double {
        return dataRepository.getMapCamTilt().toDouble()
    }

    fun saveMapCamTilt(tilt: Double) {
        dataRepository.saveMapCamTilt(tilt.toInt())
    }

    fun createHistory(speedHistory: SpeedHistory) {
        dataRepository.insertHistory(speedHistory)
    }

    fun historyRecord(): LiveData<List<SpeedHistory>>? {
        return dataRepository.getAllHistory()
    }

    fun updateHistory(speedHistory: SpeedHistory) {
        dataRepository.updateHistory(speedHistory)
    }

    fun deleteHistory(speedHistory: SpeedHistory) {
        dataRepository.deleteHistory(speedHistory)
    }

    fun deleteAllHistory() {
        dataRepository.deleteAllHistory()
    }

}
