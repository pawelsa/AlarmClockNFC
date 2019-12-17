package com.example.db.stats.dao

import com.example.db.stats.models.AlarmStatsEntity
import com.helpfulapps.data.db.stats.dao.StatsDao
import com.helpfulapps.data.db.stats.model.AlarmStatsData
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import io.reactivex.Single

class StatsDaoImpl : StatsDao {
    override fun save(alarmStats: AlarmStatsData): Single<Boolean> {
        return AlarmStatsEntity(alarmStats).save()
    }

    override fun getAll(): Single<List<AlarmStatsData>> {
        return (select.from(AlarmStatsEntity::class.java)).rx().queryList()
            .map { it.map { entity -> entity.toData() } }
    }
}