package com.helpfulapps.alarmclock.helpers.extensions

import ca.antonious.materialdaypicker.MaterialDayPicker

fun List<MaterialDayPicker.Weekday>.toDayArray(): Array<Boolean> {
    val repeatingDays = Array(7) { false }

    forEach {
        val myDay = if (it.ordinal == 0) 6 else it.ordinal - 1
        repeatingDays[myDay] = true
    }
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
