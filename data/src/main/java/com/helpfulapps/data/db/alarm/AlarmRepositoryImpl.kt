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

open class AlarmRepositoryImpl(context: Context) : AlarmRepository {

    init {
        FlowManager.init(context)
    }


    fun savvve(){

        val alarm1 = Alarm(0,"",false,true,false,15,0L,15L, IntArray(0))
        val alarmEntry = AlarmEntry(alarm1)

        alarmEntry.save().subscribe ({ isSaved -> Log.d("saveing", "saved : $isSaved") },{ t-> Log.d("saveing", t.message) }).dispose()
    }

    override fun getAlarms(): Single<List<Alarm>> =
        select.from(AlarmEntry::class.java).rx().queryList().map { list ->
            list.map { element -> element.toDomain() }
        }
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
            .doOnSuccess { success->  Log.d("add alarm", "added : $success") }
            .flatMapCompletable { isSaved -> isSaved.completed("Couldn't save alarm") }
            .subscribeOn(getSchedulerIO())

    override fun updateAlarm(alarm: Alarm): Completable =
        AlarmEntry(alarm).update()
            .flatMapCompletable { isUpdated -> isUpdated.completed("Couldn't update alarm") }
            .subscribeOn(getSchedulerIO())


    fun getSchedulerIO() : Scheduler = Schedulers.io()
}
