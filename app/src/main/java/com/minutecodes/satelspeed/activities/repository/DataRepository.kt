package com.minutecodes.satelspeed.activities.repository

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.minutecodes.satelspeed.activities.KILOMETER
import com.minutecodes.satelspeed.activities.KMH_VALUE
import com.minutecodes.satelspeed.activities.cache.data_cache.DataCache
import com.minutecodes.satelspeed.activities.cache.data_cache.MapViewPreference
import com.minutecodes.satelspeed.activities.dao.HistoryDao
import com.minutecodes.satelspeed.activities.database.HistoryDatabase
import com.minutecodes.satelspeed.activities.model.SpeedHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class SystemUnitPreference(application: Application) {
    private val appContext = application
    private val fileName = "SI_PREF"
    private val speedUnitTag = "SPEED_SI_UNIT"
    private val distanceUnitTag = "DISTANCE_SI_UNIT"
    private lateinit var preference: SharedPreferences

    init {
        getFileInstance { preference = it }
    }

    fun saveSpeedUnit(unit: Float) {
        preference.edit().putFloat(speedUnitTag, unit).apply()
    }

    fun getSpeedUnit(): Float {
        return preference.getFloat(speedUnitTag, KMH_VALUE)
    }

    fun saveDistanceUnit(unit: Float) {
        preference.edit().putFloat(distanceUnitTag, unit).apply()
    }

    fun getDistanceUnit(): Float {
        return preference.getFloat(distanceUnitTag, KILOMETER)
    }

    private inline fun getFileInstance(crossinline block: (SharedPreferences) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val prefFile = appContext.getSharedPreferences(fileName, Context.MODE_PRIVATE)
            block(prefFile)
        }
    }
}

class DataRepository(application: Application) {
    private var historyDao: HistoryDao
    private val dataCache = DataCache()
    private val mapViewPreference = MapViewPreference(application)
    private val systemUnitPreference = SystemUnitPreference(application)

    init {
        HistoryDatabase.getInstance(application).also {
            historyDao = it.historyDao()
            populateHistoryCache()
        }
    }

    fun saveSpeedUnit(unit: Float) {
        systemUnitPreference.saveSpeedUnit(unit)
    }

    fun getSpeedUnit(): Float {
        return systemUnitPreference.getSpeedUnit()
    }

    fun saveDistanceUnit(unit: Float) {
        systemUnitPreference.saveDistanceUnit(unit)
    }

    fun getDistanceUnit(): Float {
        return systemUnitPreference.getDistanceUnit()
    }

    fun getMapCameraHeight(): Int {
        return mapViewPreference.getMapCameraHeight()
    }

    fun saveMapCameraHeight(height: Int) {
        mapViewPreference.saveMapCameraHeight(height)
    }

    fun getMapCamTilt(): Int {
        return mapViewPreference.getMapCamTilt()
    }

    fun saveMapCamTilt(tilt: Int) {
        mapViewPreference.saveMapCamTilt(tilt)
    }

    fun getMapThemeId(): String {
        return mapViewPreference.getMapThemeId()
    }

    fun saveMapThemeId(id: String) {
        mapViewPreference.saveMapThemeId(id)
    }

    fun deleteAllHistory() = historyDao.deleteAll()

    fun getAllHistory(): LiveData<List<SpeedHistory>>? = dataCache.get()

    fun insertHistory(speedHistory: SpeedHistory) = historyDao.insert(speedHistory)

    fun updateHistory(speedHistory: SpeedHistory) = historyDao.update(speedHistory)

    fun deleteHistory(speedHistory: SpeedHistory) = historyDao.delete(speedHistory)

    private fun populateHistoryCache() {
        CoroutineScope(Dispatchers.IO).launch {
            dataCache.store(getHistoryList().value)
        }
    }

    private suspend fun getHistoryList() = coroutineScope { historyDao.getAll() }
}
