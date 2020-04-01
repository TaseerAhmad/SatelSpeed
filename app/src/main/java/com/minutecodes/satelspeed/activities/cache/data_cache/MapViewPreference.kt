package com.minutecodes.satelspeed.activities.cache.data_cache

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Paint
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewPreference(application: Application) {
    private lateinit var preference: SharedPreferences

    init {
        loadPreferenceAsync(application) {
            preference = it
        }
    }

    fun getMapCameraHeight(): Int {
        return preference.getInt("MV_CAM_HEIGHT", 20)
    }

    fun saveMapCameraHeight(height: Int) {
        preference.edit().putInt("MV_CAM_HEIGHT", height).apply()
    }

    fun getMapCamTilt(): Int {
        return preference.getInt("MV_CAM_TILT",50)
    }

    fun saveMapCamTilt(tilt: Int) {
        preference.edit().putInt("MV_CAM_TILT", tilt).apply()
    }

    fun getMapThemeId(): String {
        return preference.getString("MV_THEME", Style.DARK) ?: Style.DARK
    }

    fun saveMapThemeId(id: String) {
        preference.edit().putString("MV_THEME", id).apply()
    }

    private inline fun loadPreferenceAsync(
        application: Application,
        crossinline block: (SharedPreferences) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            block(application.getSharedPreferences("MV_PREF", Context.MODE_PRIVATE))
        }
    }
}