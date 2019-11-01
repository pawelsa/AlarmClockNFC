package com.helpfulapps.alarmclock.helpers.extensions

import ca.antonious.materialdaypicker.MaterialDayPicker

fun List<MaterialDayPicker.Weekday>.toDayArray(): Array<Boolean> {
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

fun Array<Boolean>.toDayList(): List<MaterialDayPicker.Weekday> {
    val dayList = arrayListOf<MaterialDayPicker.Weekday>()
    this.forEachIndexed { index, day ->
        if (day) {
            dayList.add(MaterialDayPicker.Weekday[if (index == 6) 0 else index + 1])
        }
    }
    return dayList
}

fun List<MaterialDayPicker.Weekday>.notEqual(list: List<MaterialDayPicker.Weekday>): Boolean {
    if (this.size != list.size) return true

    this.forEach { leftList ->
        if (!list.any { it == leftList }) return true
    }
    return false
}