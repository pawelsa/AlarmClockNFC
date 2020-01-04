package com.helpfulapps.domain.use_cases.mockData

import com.helpfulapps.domain.models.stats.AnalysedAlarmStats

object MockStats {

    val defaultAnalysedStats = createAnalysedStats()

    val emptyStatsList = createAnalysedStats(
        averageSnoozesByDay = arrayOf(0, 0, 0, 0, 0, 0, 0),
        averageStopTimeByDay = arrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f)
    )

    fun createAnalysedStats(
        averageStopTimeByDay: Array<Float> = arrayOf(0f, 10f, 100f, 50f, 0f, 0f, 0f),
        averageSnoozesByDay: Array<Int> = arrayOf(0, 2, 3, 5, 0, 0, 0)
    ): AnalysedAlarmStats {
        return AnalysedAlarmStats(
            averageStopTimeByDay, averageSnoozesByDay
        )
    }

}