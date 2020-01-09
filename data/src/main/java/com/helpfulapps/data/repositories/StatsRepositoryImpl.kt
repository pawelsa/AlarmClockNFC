package com.helpfulapps.data.repositories

import com.helpfulapps.data.db.stats.dao.StatsDao
import com.helpfulapps.data.db.stats.model.AlarmStatsData
import com.helpfulapps.data.db.stats.model.SnoozeData
import com.helpfulapps.data.db.stats.model.TimeToStopData
import com.helpfulapps.domain.models.stats.AlarmStats
import com.helpfulapps.domain.models.stats.AnalysedAlarmStats
import com.helpfulapps.domain.repository.StatsRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class StatsRepositoryImpl(
    private val statsDao: StatsDao
) : StatsRepository {
    override fun getAllStats(): Single<AnalysedAlarmStats> {
        return Single.zip(
            statsDao.getStopTime(),
            statsDao.getSnoozed(),
            BiFunction<List<TimeToStopData>, List<SnoozeData>, AnalysedAlarmStats> { timeToStop, snooze ->
                val stopTime = Array(7) { index ->
                    timeToStop.find { it.dayOfWeek == index }?.avgStopTime ?: 0f
                }
                val noSnoozes =
                    Array(7) { index -> snooze.find { it.dayOfWeek == index }?.noSnoozes ?: 0 }

                AnalysedAlarmStats(stopTime, noSnoozes)
            })
    }

    override fun saveInfo(alarmStats: AlarmStats): Completable {

        return statsDao.save(AlarmStatsData(alarmStats))
            .flatMapCompletable {
                return@flatMapCompletable Completable.complete()
            }.onErrorComplete()
    }
}