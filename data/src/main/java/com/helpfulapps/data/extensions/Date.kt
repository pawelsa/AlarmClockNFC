package com.helpfulapps.data.extensions

import java.util.*


fun Long.dayOfMonth(): Int =
    GregorianCalendar()
        .apply {
            timeInMillis = this@dayOfMonth
        }
        .get(Calendar.DAY_OF_MONTH)

fun Long.timestampAtMidnight() =
    GregorianCalendar()
        .apply {
            timeInMillis = this@timestampAtMidnight
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        .timeInMillis
