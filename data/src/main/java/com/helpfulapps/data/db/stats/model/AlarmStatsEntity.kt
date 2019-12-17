package com.helpfulapps.data.db.stats.model

import com.helpfulapps.domain.models.stats.AlarmStats

data class AlarmStatsData(
    var id: Long = 0,
    var dayOfWeek: Int = 0,
    var dayOfYear: Int = 0,
    var hour: Int = 0,
    var minute: Int = 0,
    var timeToStop: Int = 0
) {
    constructor(alarmStats: AlarmStats) : this() {
        dayOfWeek = alarmStats.dayOfWeek
        dayOfYear = alarmStats.dayOfYear
        hour = alarmStats.hour
        minute = alarmStats.minute
        timeToStop = alarmStats.timeToStop
    }
}