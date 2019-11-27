package com.helpfulapps.domain.helpers

interface Settings {
    var firstRun: Boolean
    var hasNfc: Boolean
    var askForBatteryOptimization: Boolean
    var city: String
    var useMobileData: Boolean
    var snoozeAlarmTime: Int
    var alarmTime: Int
    var units: Units


    companion object {
        const val KEY_FIRST_TIME = "com.helpfulapps.alarmclock.first_time"
        const val KEY_HAS_NFC = "com.helpfulapps.alarmclock.has_nfc"
        const val KEY_ASK_FOR_OPTIMIZATIONS = "com.helpfulapps.alarmclock.battery_optimization"
        const val KEY_CITY = "com.helpfulapps.alarmclock.city"
        const val KEY_WEATHER_UNITS = "com.helpfulapps.alarmclock.units"

        // Update in preferences.xml
        const val KEY_USE_MOBILE_DATA = "com.helpfulapps.alarmclock.use_mobile_data"
        const val KEY_SNOOZE_ALARM = "com.helpfulapps.alarmclock.settings_snooze"
        const val KEY_ALARM_TIME = "com.helpfulapps.alarmclock.settings_alarm"
    }

    enum class Units(val unit: String) {
        METRIC("metric"),
        IMPERIAL("imperial")

    }

}