package com.helpfulapps.data.db.stats.dao

import com.helpfulapps.data.db.stats.model.AlarmStatsData
import com.helpfulapps.data.db.stats.model.AlarmStatsEntity
import io.reactivex.Single

interface StatsDao {
    fun save(alarmStats: AlarmStatsData): Single<Boolean>
    fun getAll(): Single<List<AlarmStatsData>>
}