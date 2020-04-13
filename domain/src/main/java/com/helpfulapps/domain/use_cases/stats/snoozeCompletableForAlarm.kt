package com.helpfulapps.domain.use_cases.stats

import com.helpfulapps.domain.helpers.TimeSetter
import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.models.stats.AlarmStats
import java.util.*

fun saveAlarmStats(alarm: Alarm): AlarmStats {
    val timeSetter = TimeSetter()
    val alarmTime =
        timeSetter.setHourAndMinute(alarm/*, GregorianCalendar.getInstance()*/).milliseconds

    return AlarmStats(
        3,
        3,
        alarm.hour,
        alarm.minute,
        ((GregorianCalendar.getInstance().timeInMillis - alarmTime) / 1000).toInt()
    )
}