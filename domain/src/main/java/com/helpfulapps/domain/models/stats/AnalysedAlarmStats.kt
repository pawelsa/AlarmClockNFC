package com.helpfulapps.domain.models.stats

data class AnalysedAlarmStats(
    val averageStopTimeByDay: Array<Float>,
    val averageSnoozesByDay: Array<Int>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AnalysedAlarmStats

        if (!averageStopTimeByDay.contentEquals(other.averageStopTimeByDay)) return false
        if (!averageSnoozesByDay.contentEquals(other.averageSnoozesByDay)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = averageStopTimeByDay.contentHashCode()
        result = 31 * result + averageSnoozesByDay.contentHashCode()
        return result
    }
}