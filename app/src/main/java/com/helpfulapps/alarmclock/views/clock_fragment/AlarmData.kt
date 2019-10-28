package com.helpfulapps.alarmclock.views.clock_fragment

import com.helpfulapps.alarmclock.helpers.timeToString
import com.helpfulapps.domain.models.alarm.Alarm

data class AlarmData(
    val id: Long = 0,
    var title: String = "",
    var isTurnedOn: Boolean,
    var isExpanded: Boolean = false,
    var weatherIcon: Int,
    var isVibrationOn: Boolean = true,
    var ringtoneUrl: String,
    var ringtoneTitle: String,
    var hour: Int,
    var minute: Int,
    var alarmTime: String = timeToString(hour, minute),
    var isRepeating: Boolean = false,
    var repetitionDays: Array<Boolean>
) {

    fun toDomain(): Alarm {
        return Alarm(
            id,
            title,
            isRepeating,
            isVibrationOn,
            isTurnedOn,
            ringtoneUrl,
            ringtoneTitle,
            hour,
            minute,
            repetitionDays
        )
    }

}