package com.helpfulapps.alarmclock.helpers

import java.text.DateFormatSymbols
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

fun Array<Boolean>.toShortWeekdays(): String {

    val weekdayList = arrayListOf<String>()

    val formatSymbols = DateFormatSymbols.getInstance()
    if (this@toShortWeekdays[0]) weekdayList.add(formatSymbols.shortWeekdays[Calendar.MONDAY])
    if (this@toShortWeekdays[1]) weekdayList.add(formatSymbols.shortWeekdays[Calendar.TUESDAY])
    if (this@toShortWeekdays[2]) weekdayList.add(formatSymbols.shortWeekdays[Calendar.WEDNESDAY])
    if (this@toShortWeekdays[3]) weekdayList.add(formatSymbols.shortWeekdays[Calendar.THURSDAY])
    if (this@toShortWeekdays[4]) weekdayList.add(formatSymbols.shortWeekdays[Calendar.FRIDAY])
    if (this@toShortWeekdays[5]) weekdayList.add(formatSymbols.shortWeekdays[Calendar.SATURDAY])
    if (this@toShortWeekdays[6]) weekdayList.add(formatSymbols.shortWeekdays[Calendar.SUNDAY])

    return buildString {
        weekdayList.forEachIndexed { index, weekDay ->
            if (index != 0) append(", ")
            append(weekDay)
            append(".")
        }
    }
}

fun Long.secondsToString(): String {
    var timeInSeconds = this
    val hours = timeInSeconds / 3600
    timeInSeconds -= hours * 3600
    val minutes = timeInSeconds / 60
    val seconds = timeInSeconds % 60

    return when {
        hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes, seconds)
        minutes > 0 -> String.format("%02d:%02d", minutes, seconds)
        else -> String.format("00:%02d", seconds)
    }
}

fun Long.millisToString(): String {
    var timeInMillis = this
    val hours = timeInMillis / 3600000
    timeInMillis -= hours * 3600000
    val minutes = timeInMillis / 60000
    timeInMillis -= minutes * 60000
    val seconds = timeInMillis / 1000
    timeInMillis -= seconds * 1000
    timeInMillis /= 10

    return when {
        hours > 0 -> String.format("%02d:%02d:%02d:%02d", hours, minutes, seconds, timeInMillis)
        minutes > 0 -> String.format("%02d:%02d:%02d", minutes, seconds, timeInMillis)
        else -> String.format("%02d:%02d", seconds, timeInMillis)
    }
}