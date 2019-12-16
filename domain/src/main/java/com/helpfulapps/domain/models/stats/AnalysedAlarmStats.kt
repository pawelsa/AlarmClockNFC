package com.helpfulapps.domain.models.stats

data class AnalysedAlarmStats(
    val averageStopTimeByDay: Array<Float>,
    val averageSnoozesByDay: Array<Int>
)