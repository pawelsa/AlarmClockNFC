package com.helpfulapps.data.db.alarm

import android.content.Context
import com.helpfulapps.data.db.alarm.model.AlarmEntry
import com.helpfulapps.data.db.alarm.model.AlarmEntry_Table
import com.helpfulapps.domain.model.Alarm
import com.helpfulapps.domain.repository.AlarmRepository
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.delete
import com.raizlabs.android.dbflow.kotlinextensions.save
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.kotlinextensions.update
import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import io.reactivex.Completable
import io.reactivex.Completable.complete
import io.reactivex.CompletableObserver
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class AlarmRepositoryImpl(context: Context) : AlarmRepository {

    init {
        FlowManager.init(context)
    }

    override fun getAlarms(): Single<List<Alarm>> =
        select.from(AlarmEntry::class.java).rx().queryList().map { list ->
            list.map { element -> element.toDomain() }
        }
            .subscribeOn(Schedulers.io())

    override fun removeAlarm(alarmId: Long): Completable =
        select.from(AlarmEntry::class.java).where(AlarmEntry_Table.id.`is`(alarmId)).rx()
            .queryList().flatMapCompletable {
                var allDeleted = true
                var currentDeleted = false
                it.forEach { alarmEntry: AlarmEntry? ->
                    currentDeleted = alarmEntry?.delete()!!
                    when {
                        !currentDeleted -> allDeleted = false
                    }
                }
                when (allDeleted) {
                    true -> complete()
                    else -> error(Throwable("Could'n delete all alarms of this id"))
                }
            }
            .subscribeOn(Schedulers.io())


    override fun switchAlarm(alarmId: Long): Completable =
        select.from(AlarmEntry::class.java).where(AlarmEntry_Table.id.`is`(alarmId)).rx()
            .querySingle()
            .flatMapCompletable { alarmEntry: AlarmEntry ->
                alarmEntry.isTurnedOn = !alarmEntry.isTurnedOn
                when (alarmEntry.update()) {
                    true -> complete()
                    else -> Completable.error(Throwable("Couldn't switch the alarm"))
                }
            }
            .subscribeOn(Schedulers.io())

    override fun addAlarm(alarm: Alarm): Completable = object : Completable() {
        override fun subscribeActual(observer: CompletableObserver?) {
            when (AlarmEntry(alarm).save()) {
                true -> observer?.onComplete()
                else -> observer?.onError(Throwable("Couldn't add the alarm"))
            }
        }
    }
        .subscribeOn(Schedulers.io())

    override fun updateAlarm(alarm: Alarm): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
