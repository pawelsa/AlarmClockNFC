package com.helpfulapps.data.mockData

import com.helpfulapps.domain.models.alarm.Alarm

object MockData {

    val defaultAlarm = Alarm(
        id = 5,
        title = "",
        isRepeating = false,
        isVibrationOn = true,
        isTurnedOn = false,
        ringtoneUrl = "ringtoneUrl",
        hour = 0,
        minute = 15,
        repetitionDays = arrayOf(true, true, false, false, false, false, false)
    )

    val notInDbAlarm = Alarm(
        id = 100,
        title = "",
        isRepeating = false,
        isVibrationOn = true,
        isTurnedOn = false,
        ringtoneUrl = "ringtoneUrl",
        hour = 0,
        minute = 15,
        repetitionDays = arrayOf(true, true, false, false, false, false, false)
    )

    val additionalAlarm = Alarm(
        id = 1,
        title = "test 1",
        isRepeating = false,
        isVibrationOn = true,
        isTurnedOn = false,
        ringtoneUrl = "ringtoneUrl",
        hour = 0,
        minute = 15,
        repetitionDays = arrayOf(true, true, false, false, false, false, false)
    )


}