package com.helpfulapps.domain.repository

import com.helpfulapps.domain.models.stats.AlarmStats
import com.helpfulapps.domain.models.stats.AnalysedAlarmStats
import io.reactivex.Completable
import io.reactivex.Single

interface StatsRepository {

    fun getAllStats(): Single<AnalysedAlarmStats>
    fun saveInfo(alarmStats: AlarmStats): Completable

}