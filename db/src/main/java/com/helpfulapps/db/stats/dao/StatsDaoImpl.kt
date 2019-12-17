package com.helpfulapps.db.stats.dao

import android.content.Context
import com.helpfulapps.data.db.stats.dao.StatsDao
import com.helpfulapps.data.db.stats.model.AlarmStatsData
import com.helpfulapps.db.stats.models.AlarmStatsEntity
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import io.reactivex.Single

class StatsDaoImpl(context: Context) : StatsDao {

    init {
        FlowManager.init(context)
    }

    override fun save(alarmStats: AlarmStatsData): Single<Boolean> {
        return AlarmStatsEntity(alarmStats).save()
    }

    override fun getAll(): Single<List<AlarmStatsData>> {
        return (select.from(AlarmStatsEntity::class.java)).rx().queryList()
            .map { it.map { entity -> entity.toData() } }
    }
}