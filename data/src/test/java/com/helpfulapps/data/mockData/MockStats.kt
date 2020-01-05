package com.helpfulapps.data.mockData

import com.helpfulapps.data.db.stats.model.AlarmStatsData
import com.helpfulapps.domain.models.stats.AnalysedAlarmStats

object MockStats {

    val defaultAnalysedStats = createAnalysedStats()

    val alarmStatsList = listOf(
        createAlrmsStatsData(timeToStop = 10, dayOfWeek = 1, dayOfYear = 1),
        createAlrmsStatsData(timeToStop = 10, dayOfWeek = 1, dayOfYear = 1),

        createAlrmsStatsData(timeToStop = 100, dayOfWeek = 2, dayOfYear = 2),
        createAlrmsStatsData(timeToStop = 100, dayOfWeek = 2, dayOfYear = 2),
        createAlrmsStatsData(timeToStop = 100, dayOfWeek = 2, dayOfYear = 2),

        createAlrmsStatsData(timeToStop = 50, dayOfWeek = 3, dayOfYear = 3),
        createAlrmsStatsData(timeToStop = 50, dayOfWeek = 3, dayOfYear = 3),
        createAlrmsStatsData(timeToStop = 50, dayOfWeek = 3, dayOfYear = 3),
        createAlrmsStatsData(timeToStop = 50, dayOfWeek = 3, dayOfYear = 3),
        createAlrmsStatsData(timeToStop = 50, dayOfWeek = 3, dayOfYear = 3)
    )

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

    fun createAlrmsStatsData(
        dayOfWeek: Int = 1,
        dayOfYear: Int = 1,
        hour: Int = 8,
        minute: Int = 30,
        timeToStop: Int = 100,
        id: Long = 1
    ): AlarmStatsData {
        return AlarmStatsData(
            id, dayOfWeek, dayOfYear, hour, minute, timeToStop
        )
    }

}