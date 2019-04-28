package com.helpfulapps.alarmclock.clock_fragment

data class AlarmData(
    val primaryText: String,
    val secondaryText: String,
    val icon: Int,
    var isExpanded: Boolean = false,
    var isRepeating: Boolean = false
)