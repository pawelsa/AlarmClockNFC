package com.helpfulapps.device.alarms.helpers

import android.content.SharedPreferences
import androidx.core.content.edit

class SettingsDevice(private val sharedPreferences: SharedPreferences) {

    var firstRun: Boolean
        set(value) = sharedPreferences.edit { putBoolean(KEY_FIRST_TIME, value) }
        get() = sharedPreferences.getBoolean(KEY_FIRST_TIME, true)

    var hasNfc: Boolean
        set(value) = sharedPreferences.edit { putBoolean(KEY_HAS_NFC, value) }
        get() = sharedPreferences.getBoolean(KEY_HAS_NFC, false)

    var askForBatteryOptimization: Boolean
        set(value) = sharedPreferences.edit { putBoolean(KEY_ASK_FOR_OPTIMIZATIONS, value) }
        get() = sharedPreferences.getBoolean(KEY_ASK_FOR_OPTIMIZATIONS, true)

    var city: String
        set(value) = sharedPreferences.edit { putString(KEY_CITY, value) }
        get() = sharedPreferences.getString(KEY_CITY, "-1") ?: "-1"

    var useMobileData: Boolean
        set(value) = sharedPreferences.edit { putBoolean(KEY_USE_MOBILE_DATA, value) }
        get() = sharedPreferences.getBoolean(KEY_USE_MOBILE_DATA, false)

    var snoozeAlarmTime: Int
        set(value) = sharedPreferences.edit { putString(KEY_SNOOZE_ALARM, value.toString()) }
        get() = (sharedPreferences.getString(KEY_SNOOZE_ALARM, "3") ?: "3").toInt()

    var alarmTime: Int
        set(value) = sharedPreferences.edit { putString(KEY_ALARM_TIME, value.toString()) }
        get() = (sharedPreferences.getString(KEY_ALARM_TIME, "2") ?: "2").toInt()

    companion object {
        const val KEY_FIRST_TIME = "com.helpfulapps.alarmclock.first_time"
        const val KEY_HAS_NFC = "com.helpfulapps.alarmclock.has_nfc"
        const val KEY_ASK_FOR_OPTIMIZATIONS = "com.helpfulapps.alarmclock.battery_optimization"
        const val KEY_CITY = "com.helpfulapps.alarmclock.city"

        // Update in preferences.xml
        const val KEY_USE_MOBILE_DATA = "com.helpfulapps.alarmclock.use_mobile_data"
        const val KEY_SNOOZE_ALARM = "com.helpfulapps.alarmclock.settings_snooze"
        const val KEY_ALARM_TIME = "com.helpfulapps.alarmclock.settings_alarm"
    }

}