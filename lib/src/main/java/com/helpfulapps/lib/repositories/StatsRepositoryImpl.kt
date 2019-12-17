package com.helpfulapps.data.repositories

import com.helpfulapps.data.db.stats.dao.StatsDao
import com.helpfulapps.data.db.stats.model.AlarmStatsData
import com.helpfulapps.domain.exceptions.StatsException
import com.helpfulapps.domain.helpers.singleOf
import com.helpfulapps.domain.models.stats.AlarmStats
import com.helpfulapps.domain.models.stats.AnalysedAlarmStats
import com.helpfulapps.domain.repository.StatsRepository
import io.reactivex.Completable
import io.reactivex.Single

class StatsRepositoryImpl(
    private val statsDao: StatsDao
) : StatsRepository {
    override fun getAllStats(): Single<AnalysedAlarmStats> {
        return statsDao.getAll()
            .flatMap { alarmStatsList ->
                val data = alarmStatsList.groupBy { it.dayOfWeek }

                val snoozesADay =
                    Array(7) { index -> (data[index].size ?: 0) - 1 }.map { if (it < 0) 0 else it }
                        .toTypedArray()

                val sumOfStopTime = Array(7) { index ->
                    data[index].map { it.timeToStop }.average().toFloat() ?: 0f
                }

                singleOf {
                    AnalysedAlarmStats(
                        sumOfStopTime, snoozesADay
                    )
                }

            }


    }

    override fun saveInfo(alarmStats: AlarmStats): Completable {

        return statsDao.save(AlarmStatsData(alarmStats))
            .flatMapCompletable {
                if (it) {
                    return@flatMapCompletable Completable.complete()
                }
                return@flatMapCompletable Completable.error(StatsException("Could not save stats"))
            }
    }
}