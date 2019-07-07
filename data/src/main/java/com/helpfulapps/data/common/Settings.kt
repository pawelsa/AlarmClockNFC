package com.helpfulapps.data.common

import android.content.SharedPreferences
import androidx.core.content.edit

class Settings(val sharedPreferences: SharedPreferences) {

    companion object {
        private const val KEY_WEATHER_UNITS = "units"
    }

    var units: Units
        set(value) = sharedPreferences.edit { putString(KEY_WEATHER_UNITS, value.unit) }
        get() = if (sharedPreferences
                .getString(KEY_WEATHER_UNITS, Units.METRIC.unit) == Units.METRIC.unit
        ) Units.METRIC else Units.IMPERIAL


}

enum class Units(val unit: String) {
    METRIC("metric"),
    IMPERIAL("imperial")

}