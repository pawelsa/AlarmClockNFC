package com.helpfulapps.data.repositories

import android.content.Context
import com.helpfulapps.data.db.alarm.model.AlarmEntity
import com.helpfulapps.data.db.alarm.model.AlarmEntity_Table
import com.helpfulapps.data.extensions.checkCompleted
import com.helpfulapps.domain.exceptions.AlarmException
import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.repository.AlarmRepository
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.kotlinextensions.where
import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

// TODO remove subscribing on bg thread
open class AlarmRepositoryImpl(context: Context) : AlarmRepository {

    init {
        FlowManager.init(context)
    }

    override fun getAlarms(): Single<List<Alarm>> =
        getAlarmsQuery()
            .map { list ->
                list.map { element -> element.toDomain() }
            }
            .timeout(2L, TimeUnit.SECONDS) { observer -> observer.onSuccess(emptyList()) }

    override fun removeAlarm(alarmId: Long): Completable =
        getAlarm(alarmId)
            .flatMapSingle { it.delete() }
            .flatMapCompletable { isDeleted -> isDeleted.checkCompleted(
                AlarmException(
                    "Couldn't delete alarm"
                )
            ) }


    override fun switchAlarm(alarmId: Long): Completable =
        getAlarm(alarmId)
            .map { alarmEntry ->
                alarmEntry.isTurnedOn = !alarmEntry.isTurnedOn
                alarmEntry
            }
            .flatMapSingle(AlarmEntity::update)
            .flatMapCompletable { isUpdated -> isUpdated.checkCompleted(
                AlarmException(
                    "Couldn't update the alarm"
                )
            ) }

    override fun addAlarm(alarm: Alarm): Completable =
        AlarmEntity(alarm).save()
            .flatMapCompletable { isSaved -> isSaved.checkCompleted(
                AlarmException(
                    "Couldn't save alarm"
                )
            ) }

    override fun updateAlarm(alarm: Alarm): Completable =
        AlarmEntity(alarm).update()
            .flatMapCompletable { isUpdated -> isUpdated.checkCompleted(
                AlarmException(
                    "Couldn't update alarm"
                )
            ) }

    fun getAlarmsQuery() = select.from(AlarmEntity::class.java).rx().queryList()

    private fun getAlarm(alarmId: Long) =
        (select from AlarmEntity::class where AlarmEntity_Table.id.`is`(alarmId)).rx().querySingle()

    fun getSchedulerIO(): Scheduler = Schedulers.io()
}
