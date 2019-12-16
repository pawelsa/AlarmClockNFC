package com.helpfulapps.data.repositories

import com.helpfulapps.data.db.stats.model.AlarmStatsEntity
import com.helpfulapps.domain.models.stats.AlarmStats
import com.helpfulapps.domain.repository.StatsRepository
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import io.reactivex.Single

class StatsRepositoryImpl : StatsRepository {
    override fun getAllStats(): Single<List<AlarmStats>> {
        return (select.from(AlarmStatsEntity::class.java)).rx().queryList().map {
            return@map it.map { entity ->
                entity.toDomain()
            }
        }
    }
}