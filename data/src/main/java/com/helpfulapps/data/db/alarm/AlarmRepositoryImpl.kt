package com.helpfulapps.data.db.alarm

import android.content.Context
import android.util.Log
import com.helpfulapps.data.db.alarm.model.AlarmEntry
import com.helpfulapps.data.db.alarm.model.AlarmEntry_Table
import com.helpfulapps.data.db.extensions.completed
import com.helpfulapps.domain.model.Alarm
import com.helpfulapps.domain.repository.AlarmRepository
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import io.reactivex.*
import io.reactivex.schedulers.Schedulers

open class AlarmRepositoryImpl(val context: Context) : AlarmRepository {

    init {
        FlowManager.init(context)
    }

    override fun getAlarms(): Single<List<Alarm>> =
        select.from(AlarmEntry::class.java).rx().queryList().map { list ->
            list.map { element -> element.toDomain() }
        }
            .doOnSuccess { println("getAlarm success") }
            .doOnError { println("getAlarm error") }
            .subscribeOn(getSchedulerIO())

    override fun removeAlarm(alarmId: Long): Completable =
        select.from(AlarmEntry::class.java).where(AlarmEntry_Table.id.`is`(alarmId)).rx()
            .querySingle()
            .flatMapSingle { it.delete() }
            .flatMapCompletable { isDeleted -> isDeleted.completed("Couldn't delete alarm") }
            .subscribeOn(getSchedulerIO())


    override fun switchAlarm(alarmId: Long): Completable =
        select.from(AlarmEntry::class.java).where(AlarmEntry_Table.id.`is`(alarmId)).rx()
            .querySingle()
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


    override fun getSchedulerIO() : Scheduler = Schedulers.io()
}
