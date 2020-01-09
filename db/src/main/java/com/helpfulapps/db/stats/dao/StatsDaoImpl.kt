package com.helpfulapps.db.stats.dao

import android.content.Context
import com.helpfulapps.data.db.stats.dao.StatsDao
import com.helpfulapps.data.db.stats.model.AlarmStatsData
import com.helpfulapps.data.db.stats.model.SnoozeData
import com.helpfulapps.data.db.stats.model.TimeToStopData
import com.helpfulapps.db.stats.models.AlarmStatsEntity
import com.helpfulapps.db.stats.models.SnoozeQueryModel
import com.helpfulapps.db.stats.models.TimeToStopQueryModel
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import com.raizlabs.android.dbflow.sql.queriable.StringQuery
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

    override fun getSnoozed(): Single<List<SnoozeData>> {
        //select noSnoozes, dayOfWeek from (SELECT count(alarmStats.dayOfWeek) -1 as noSnoozes, alarmStats.dayOfWeek as dayOfWeek FROM alarmStats GROUP BY alarmStats.dayOfWeek, alarmStats.hour, alarmStats.minute) WHERE noSnoozes > 1
        return StringQuery(
            SnoozeQueryModel::class.java,
            "select sum(snoozes) as noSnoozes, dayOfWeek from (SELECT count(alarmStats.dayOfWeek) -1 as snoozes, alarmStats.dayOfWeek as dayOfWeek FROM alarmStats GROUP BY alarmStats.dayOfWeek, alarmStats.hour, alarmStats.minute) WHERE snoozes > 1 GROUP by dayOfWeek"
        )
            .rx().queryList().map { it.map { model -> model.toData() } }
    }

    override fun getStopTime(): Single<List<TimeToStopData>> {
        return StringQuery(
            TimeToStopQueryModel::class.java,
            "SELECT avg(alarmStats.timeToStop) as avgStopTime, alarmStats.dayOfWeek as dayOfWeek FROM alarmStats GROUP by alarmStats.dayOfWeek"
        )
            .rx().queryList().map { it.map { model -> model.toData() } }
    }
}