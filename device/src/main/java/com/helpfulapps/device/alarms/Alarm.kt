package com.helpfulapps.device.alarms

import com.helpfulapps.domain.models.alarm.Alarm

data class Alarm(
    val id: Int,
    val hour: Int,
    val minute: Int,
    val ringtoneUrl: String
) {

    constructor(alarm: Alarm) : this(
        id = alarm.id.toInt(),
        hour = alarm.hours,
        minute = alarm.minutes,
        ringtoneUrl = alarm.ringtoneUrl
    )

}