package com.helpfulapps.data.repositories

import com.helpfulapps.data.db.stats.model.AlarmStatsEntity
import com.helpfulapps.domain.exceptions.StatsException
import com.helpfulapps.domain.helpers.singleOf
import com.helpfulapps.domain.models.stats.AlarmStats
import com.helpfulapps.domain.models.stats.AnalysedAlarmStats
import com.helpfulapps.domain.repository.StatsRepository
import com.raizlabs.android.dbflow.kotlinextensions.select
import io.reactivex.Completable
import io.reactivex.Single

class StatsRepositoryImpl : StatsRepository {
    override fun getAllStats(): Single<AnalysedAlarmStats> {
        val dataByDay =
            (select.from(AlarmStatsEntity::class.java)).queryList().groupBy { it.dayOfWeek }

        val snoozesADay =
            Array(7) { index -> (dataByDay[index]?.size ?: 0) - 1 }.map { if (it < 0) 0 else it }
                .toTypedArray()
        val sumOfStopTime = Array(7) { index ->
            dataByDay[index]?.map { it.timeToStop }?.average()?.toFloat() ?: 0f
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