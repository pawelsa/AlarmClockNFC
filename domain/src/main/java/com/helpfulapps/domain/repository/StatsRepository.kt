package com.helpfulapps.domain.repository

import com.helpfulapps.domain.models.stats.AlarmStats
import io.reactivex.Completable
import io.reactivex.Single

interface StatsRepository {

    fun getAllStats(): Single<List<AlarmStats>>
    fun saveInfo(alarmStats: AlarmStats): Completable

}