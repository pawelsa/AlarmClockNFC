package com.helpfulapps.alarmclock.helpers

import android.content.SharedPreferences
import androidx.core.content.edit

class Settings(private val sharedPreferences: SharedPreferences) {

    var firstRun: Boolean
        set(value) = sharedPreferences.edit { putBoolean(KEY_FIRST_TIME, value) }
        get() = sharedPreferences.getBoolean(KEY_FIRST_TIME, true)

    var hasNfc: Boolean
        set(value) = sharedPreferences.edit { putBoolean(KEY_HAS_NFC, value) }
        get() = sharedPreferences.getBoolean(KEY_HAS_NFC, false)

    companion object {
        const val KEY_FIRST_TIME = "com.helpfulapps.alarmclock.first_time"
        const val KEY_HAS_NFC = "com.helpfulapps.alarmclock.has_nfc"
    }

}