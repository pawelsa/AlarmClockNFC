package com.helpfulapps.alarmclock.helpers

import java.text.SimpleDateFormat
import java.util.*

fun timeToString(time: Pair<Int, Int>): String {
    return timeToString(time.first, time.second)
}

fun timeToString(hour: Int, minute: Int): String {
    val date: Date = GregorianCalendar()
        .apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }.let {
            Date(it.timeInMillis)
        }
    return SimpleDateFormat("H:mm").format(date)
}

typealias Time = Pair<Int, Int>