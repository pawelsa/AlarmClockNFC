package com.helpfulapps.domain.use_cases.stats

import com.helpfulapps.domain.extensions.dayOfWeek
import com.helpfulapps.domain.extensions.dayOfYear
import com.helpfulapps.domain.helpers.TimeSetter
import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.models.stats.AlarmStats
import java.util.*

fun saveAlarmStats(alarm: Alarm): AlarmStats {
    val timeSetter = TimeSetter()
    val alarmTime =
        timeSetter.setHourAndMinute(alarm, GregorianCalendar.getInstance()).timeInMillis

    return AlarmStats(
        alarmTime.dayOfWeek,
        alarmTime.dayOfYear,
        alarm.hour,
        alarm.minute,
        ((GregorianCalendar.getInstance().timeInMillis - alarmTime) / 1000).toInt()
    )
}