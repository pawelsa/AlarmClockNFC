package com.helpfulapps.device.alarms

import com.helpfulapps.domain.models.alarm.Alarm as DomainAlarm


data class Alarm(
    val id: Int,
    val isRepeating: Boolean,
    val hour: Int,
    val minute: Int,
    val repetitionDays: Array<Boolean>
) {

    constructor(domainAlarm: DomainAlarm) : this(
        id = domainAlarm.id.toInt(),
        isRepeating = domainAlarm.isRepeating,
        hour = domainAlarm.hour,
        minute = domainAlarm.minute,
        repetitionDays = domainAlarm.repetitionDays
    )

}