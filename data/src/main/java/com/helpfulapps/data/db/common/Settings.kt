package com.helpfulapps.data.db.common

import android.content.SharedPreferences
import androidx.core.content.edit

class Settings(val sharedPreferences: SharedPreferences) {

    companion object {
        private const val KEY_WEATHER_UNITS = "units"
    }

    var units: String
        set(value) = sharedPreferences.edit { putString(KEY_WEATHER_UNITS, value) }
        get() = sharedPreferences.getString(KEY_WEATHER_UNITS, "metric")

}