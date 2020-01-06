package com.helpfulapps.alarmclock.helpers.extensions

import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

fun timeToString(time: Pair<Int, Int>): String {
    return timeToString(
        time.first,
        time.second
    )
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
    val formatSymbols = DateFormatSymbols.getInstance()
    val convertToShortWeekdays: (Int) -> String = { calendarIndex ->
        formatSymbols.shortWeekdays[calendarIndex].capitalize()
    }

    return buildString {
        var wasSomeBefore = false
        this@toShortWeekdays.forEachIndexed { index: Int, isTurnedOn: Boolean ->
            if (isTurnedOn) {
                if (wasSomeBefore) append(", ")
                val calendarIndex = if (index == 6) 1 else index + 2
                append(convertToShortWeekdays(calendarIndex))
                wasSomeBefore = true
            }
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

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}