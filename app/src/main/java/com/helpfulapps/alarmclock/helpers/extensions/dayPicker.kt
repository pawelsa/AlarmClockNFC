package com.helpfulapps.alarmclock.helpers.extensions

import ca.antonious.materialdaypicker.MaterialDayPicker

fun List<MaterialDayPicker.Weekday>.toArray(): Array<Boolean> {
    val repeatingDays = Array(7) { false }
    repeatingDays[0] = any { it == MaterialDayPicker.Weekday.MONDAY }
    repeatingDays[1] = any { it == MaterialDayPicker.Weekday.TUESDAY }
    repeatingDays[2] = any { it == MaterialDayPicker.Weekday.WEDNESDAY }
    repeatingDays[3] = any { it == MaterialDayPicker.Weekday.THURSDAY }
    repeatingDays[4] = any { it == MaterialDayPicker.Weekday.FRIDAY }
    repeatingDays[5] = any { it == MaterialDayPicker.Weekday.SATURDAY }
    repeatingDays[6] = any { it == MaterialDayPicker.Weekday.SUNDAY }
    return repeatingDays
}