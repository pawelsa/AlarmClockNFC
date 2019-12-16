package com.helpfulapps.data.repositories

import com.helpfulapps.data.db.stats.model.AlarmStatsEntity
import com.helpfulapps.data.db.stats.model.AlarmStatsEntity_Table
import com.helpfulapps.domain.exceptions.StatsException
import com.helpfulapps.domain.helpers.singleOf
import com.helpfulapps.domain.models.stats.AlarmStats
import com.helpfulapps.domain.models.stats.AnalysedAlarmStats
import com.helpfulapps.domain.repository.StatsRepository
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.kotlinextensions.where
import io.reactivex.Completable
import io.reactivex.Single

class StatsRepositoryImpl : StatsRepository {
    override fun getAllStats(): Single<AnalysedAlarmStats> {
        val dataByDay = mutableListOf<List<AlarmStatsEntity>>()
        for (day in 0..6) {
            dataByDay.add(
                (select.from(AlarmStatsEntity::class.java) where AlarmStatsEntity_Table.dayOfWeek.eq(
                    day
                )).queryList()
            )
        }

        val snoozesADay = Array(7) { index -> dataByDay[index].size }
        val sumOfStopTime = Array(7) { 0f }

        dataByDay.forEachIndexed { index, value ->
            if (value.isNotEmpty()) {
                var sumOfDay = 0
                value.forEach {
                    sumOfDay += it.timeToStop
                }
                sumOfStopTime[index] = (sumOfDay / value.size).toFloat()
            } else {
                sumOfStopTime[index] = 0f
            }
        }

        return singleOf {
            AnalysedAlarmStats(
                sumOfStopTime, snoozesADay
            )
        }
    }

    override fun saveInfo(alarmStats: AlarmStats): Completable {
        return AlarmStatsEntity(alarmStats).save().flatMapCompletable {
            if (it) {
                return@flatMapCompletable Completable.complete()
            }
            return@flatMapCompletable Completable.error(StatsException("Could not save stats"))
        }
    }
}