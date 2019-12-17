package com.helpfulapps.data.db.stats.dao

import com.helpfulapps.data.db.stats.model.AlarmStatsEntity
import com.helpfulapps.domain.models.stats.AlarmStats
import io.reactivex.Single


interface StatsDao {
    fun saveStats(alarmStats: AlarmStats): Single<Boolean>
    fun getStats(): Single<List<AlarmStatsEntity>>
}