package com.helpfulapps.data.helper

import android.content.SharedPreferences
import androidx.core.content.edit

class SettingsData(private val _sharedPreferences: SharedPreferences) {

    companion object {
        private const val KEY_WEATHER_UNITS = "units"
    }

    var units: Units
        set(value) = _sharedPreferences.edit { putString(KEY_WEATHER_UNITS, value.unit) }
        get() = if (_sharedPreferences
                .getString(KEY_WEATHER_UNITS, Units.METRIC.unit) == Units.METRIC.unit
        ) Units.METRIC else Units.IMPERIAL


}

enum class Units(val unit: String) {
    METRIC("metric"),
    IMPERIAL("imperial")

}