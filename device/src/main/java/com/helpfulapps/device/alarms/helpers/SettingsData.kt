package com.helpfulapps.device.alarms.helpers

import android.content.SharedPreferences
import androidx.core.content.edit
import com.helpfulapps.domain.helpers.Settings
import com.helpfulapps.domain.helpers.Settings.Companion.KEY_ALARM_TIME
import com.helpfulapps.domain.helpers.Settings.Companion.KEY_ASK_FOR_OPTIMIZATIONS
import com.helpfulapps.domain.helpers.Settings.Companion.KEY_CITY
import com.helpfulapps.domain.helpers.Settings.Companion.KEY_FIRST_TIME
import com.helpfulapps.domain.helpers.Settings.Companion.KEY_HAS_NFC
import com.helpfulapps.domain.helpers.Settings.Companion.KEY_SNOOZE_ALARM
import com.helpfulapps.domain.helpers.Settings.Companion.KEY_TIMER_TIME
import com.helpfulapps.domain.helpers.Settings.Companion.KEY_USE_MOBILE_DATA
import com.helpfulapps.domain.helpers.Settings.Companion.KEY_WEATHER_UNITS

class SettingsData(private val sharedPreferences: SharedPreferences) : Settings {

    override var firstRun: Boolean
        set(value) = sharedPreferences.edit { putBoolean(KEY_FIRST_TIME, value) }
        get() = sharedPreferences.getBoolean(KEY_FIRST_TIME, true)

    override var hasNfc: Boolean
        set(value) = sharedPreferences.edit { putBoolean(KEY_HAS_NFC, value) }
        get() = sharedPreferences.getBoolean(KEY_HAS_NFC, false)

    override var askForBatteryOptimization: Boolean
        set(value) = sharedPreferences.edit { putBoolean(KEY_ASK_FOR_OPTIMIZATIONS, value) }
        get() = sharedPreferences.getBoolean(KEY_ASK_FOR_OPTIMIZATIONS, true)

    override var city: String
        set(value) = sharedPreferences.edit { putString(KEY_CITY, value) }
        get() = sharedPreferences.getString(KEY_CITY, "-1") ?: "-1"

    override var useMobileData: Boolean
        set(value) = sharedPreferences.edit { putBoolean(KEY_USE_MOBILE_DATA, value) }
        get() = sharedPreferences.getBoolean(KEY_USE_MOBILE_DATA, false)

    override var snoozeAlarmTime: Int
        set(value) = sharedPreferences.edit { putString(KEY_SNOOZE_ALARM, value.toString()) }
        get() = (sharedPreferences.getString(KEY_SNOOZE_ALARM, "3") ?: "3").toInt()

    override var alarmTime: Int
        set(value) = sharedPreferences.edit { putString(KEY_ALARM_TIME, value.toString()) }
        get() = (sharedPreferences.getString(KEY_ALARM_TIME, "2") ?: "2").toInt()

    override var units: Settings.Units
        set(value) = sharedPreferences.edit { putString(KEY_WEATHER_UNITS, value.unit) }
        get() = if (sharedPreferences
                .getString(
                    KEY_WEATHER_UNITS,
                    Settings.Units.METRIC.unit
                ) == Settings.Units.METRIC.unit
        ) Settings.Units.METRIC else Settings.Units.IMPERIAL

    override var timeLeft: Long
        set(value) = sharedPreferences.edit { putLong(KEY_TIMER_TIME, value) }
        get() = sharedPreferences.getLong(KEY_TIMER_TIME, -1L)
}