package com.helpfulapps.data.db.alarm.model

import com.helpfulapps.domain.models.alarm.Alarm

data class AlarmData(
    var id: Long = 0L,
    var name: String = "",
    var isRepeating: Boolean = false,
    var isVibrationOn: Boolean = true,
    var isTurnedOn: Boolean = true,
    var ringtoneId: String = "",
    var ringtoneTitle: String = "",
    var isUsingNFC: Boolean = false,
    var hour: Int = 0,
    var minute: Int = 0,
    var daysOfWeek: DaysOfWeekData = DaysOfWeekData()
) {

    constructor(alarm: Alarm) : this(
        id = alarm.id,
        name = alarm.title,
        isRepeating = alarm.isRepeating,
        isVibrationOn = alarm.isVibrationOn,
        isTurnedOn = alarm.isTurnedOn,
        ringtoneId = alarm.ringtoneUrl,
        ringtoneTitle = alarm.ringtoneTitle,
        isUsingNFC = alarm.isUsingNFC,
        hour = alarm.hour,
        minute = alarm.minute,
        daysOfWeek = DaysOfWeekData(alarm.repetitionDays)
    )

    fun toDomain(): Alarm {
        return Alarm(
            id = id,
            title = name,
            isRepeating = isRepeating,
            isVibrationOn = isVibrationOn,
            isTurnedOn = isTurnedOn,
            ringtoneUrl = ringtoneId,
            ringtoneTitle = ringtoneTitle,
            isUsingNFC = isUsingNFC,
            hour = hour,
            minute = minute,
            repetitionDays = daysOfWeek.toDomain()
        )
    }
}