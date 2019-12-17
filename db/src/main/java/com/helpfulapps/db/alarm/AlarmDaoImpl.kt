package com.helpfulapps.db.alarm

import android.content.Context
import com.helpfulapps.data.db.alarm.dao.AlarmDao
import com.helpfulapps.data.db.alarm.model.AlarmData
import com.helpfulapps.db.alarm.models.AlarmEntity
import com.helpfulapps.db.alarm.models.AlarmEntity_Table
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.kotlinextensions.where
import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import io.reactivex.Single

class AlarmDaoImpl(context: Context) : AlarmDao {

    init {
        FlowManager.init(context)
    }

    override fun getAlarms(): Single<List<AlarmData>> {
        return select.from(AlarmEntity::class.java).rx().queryList()
            .flatMap {
                Single.just(it.map { entity -> entity.toData() })
            }
    }

    override fun getSingleAlarm(alarmId: Long): Single<AlarmData> {
        return (select from AlarmEntity::class where AlarmEntity_Table.id.`is`(alarmId)).rx()
            .querySingle()
            .flatMapSingle {
                return@flatMapSingle Single.just(it.toData())
            }
    }

    override fun insert(alarmData: AlarmData): Single<Long> {
        val alarmEntity = AlarmEntity(alarmData)
        return alarmEntity.insert()
    }

    override fun update(alarmData: AlarmData): Single<Boolean> {
        val alarmEntity = AlarmEntity(alarmData)
        return alarmEntity.update()
    }

    override fun delete(alarmData: AlarmData): Single<Boolean> {
        val alarmEntity = AlarmEntity(alarmData)
        return alarmEntity.delete()
    }
}