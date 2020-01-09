package com.helpfulapps.data.db.stats.dao

import com.helpfulapps.data.db.stats.model.AlarmStatsData
import com.helpfulapps.data.db.stats.model.SnoozeData
import com.helpfulapps.data.db.stats.model.TimeToStopData
import io.reactivex.Single

interface StatsDao {
    fun save(alarmStats: AlarmStatsData): Single<Boolean>
    fun getAll(): Single<List<AlarmStatsData>>
    fun getSnoozed(): Single<List<SnoozeData>>
    fun getStopTime(): Single<List<TimeToStopData>>
}