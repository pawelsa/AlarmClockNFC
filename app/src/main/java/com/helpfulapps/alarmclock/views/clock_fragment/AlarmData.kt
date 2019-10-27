package com.helpfulapps.alarmclock.views.clock_fragment

data class AlarmData(
    val alarmTime: String,
    val title: String,
    val weatherIcon: Int,
    var isExpanded: Boolean = false,
    var isRepeating: Boolean = false,
    var ringtoneTile: String,
    var isTurnedOn: Boolean
)