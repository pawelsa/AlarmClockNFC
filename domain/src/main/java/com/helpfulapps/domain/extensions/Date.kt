package com.helpfulapps.domain.extensions

import java.util.*

val Long.dayOfYear: Int
    get() = GregorianCalendar()
        .apply {
            timeInMillis = this@dayOfYear
        }
        .get(Calendar.DAY_OF_YEAR)
