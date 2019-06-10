package com.helpfulapps.data.db.alarm

import android.content.Context
import com.helpfulapps.data.db.alarm.model.AlarmEntry
import com.helpfulapps.data.db.alarm.model.AlarmEntry_Table
import com.helpfulapps.data.db.extensions.completed
import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.repository.AlarmRepository
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

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
            .subscribeOn(getSchedulerIO())

    override fun removeAlarm(alarmId: Long): Completable =
        getAlarm(alarmId)
            .flatMapSingle { it.delete() }
            .flatMapCompletable { isDeleted -> isDeleted.completed("Couldn't delete alarm") }
            .subscribeOn(getSchedulerIO())


    override fun switchAlarm(alarmId: Long): Completable =
        getAlarm(alarmId)
            .map { alarmEntry ->
                alarmEntry.isTurnedOn = !alarmEntry.isTurnedOn
                alarmEntry
            }
            .flatMapSingle(AlarmEntry::update)
            .flatMapCompletable { isUpdated -> isUpdated.completed("Couldn't update the alarm") }
            .subscribeOn(getSchedulerIO())

    override fun addAlarm(alarm: Alarm): Completable =
        AlarmEntry(alarm).save()
            .flatMapCompletable { isSaved -> isSaved.completed("Couldn't save alarm") }
            .subscribeOn(getSchedulerIO())

    override fun updateAlarm(alarm: Alarm): Completable =
        AlarmEntry(alarm).update()
            .flatMapCompletable { isUpdated -> isUpdated.completed("Couldn't update alarm") }
            .subscribeOn(getSchedulerIO())


    open fun getSchedulerIO(): Scheduler = Schedulers.io()

    open fun getAlarmsQuery() = select.from(AlarmEntry::class.java).rx().queryList()

    open fun getAlarm(alarmId : Long) = select.from(AlarmEntry::class.java).where(AlarmEntry_Table.id.`is`(alarmId)).rx().querySingle()
}
