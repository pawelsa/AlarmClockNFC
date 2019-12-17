package com.helpfulapps.data.repositories

import android.content.Context
import com.helpfulapps.data.db.alarm.model.AlarmEntity
import com.helpfulapps.data.db.alarm.model.AlarmEntity_Table
import com.helpfulapps.data.extensions.checkCompleted
import com.helpfulapps.domain.eventBus.DatabaseNotifiers
import com.helpfulapps.domain.eventBus.RxBus
import com.helpfulapps.domain.exceptions.AlarmException
import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.repository.AlarmRepository
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.kotlinextensions.where
import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import com.raizlabs.android.dbflow.structure.Model
import io.reactivex.Completable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

// TODO remove subscribing on bg thread
open class AlarmRepositoryImpl(context: Context) : AlarmRepository {

    private val TAG = AlarmRepositoryImpl::class.java.simpleName

    init {
        FlowManager.init(context)
    }

    override fun getAlarm(id: Long): Single<Alarm> {
        return getSingleAlarm(id).flatMapSingle { Single.just(it.toDomain()) }
    }

    override fun getAlarms(): Single<List<Alarm>> {
        return getAlarmsQuery()
            .map { list ->
                list.map { element -> element.toDomain() }
            }
            .timeout(2L, TimeUnit.SECONDS) { observer ->
                observer.onSuccess(emptyList())
            }
    }

    override fun removeAlarm(alarmId: Long): Completable {
        return getSingleAlarm(alarmId)
            .flatMapSingle { it.delete() }
            .flatMapCompletable { isDeleted ->
                isDeleted.checkCompleted(
                    AlarmException(
                        "Couldn't delete alarm"
                    )
                )
            }.doOnComplete { RxBus.publish(DatabaseNotifiers.Removed) }
    }

    // todo zabezpieczyć przed niepoprawnym id
    override fun switchAlarm(alarmId: Long): Single<Alarm> {
        return getSingleAlarm(alarmId)
            .map { alarmEntry ->
                alarmEntry.isTurnedOn = !alarmEntry.isTurnedOn
                alarmEntry
            }
            .flatMapSingle(AlarmEntity::update)
            .flatMap { isUpdated ->
                if (isUpdated) {
                    return@flatMap getAlarmDomain(alarmId)
                }
                throw AlarmException("Couldn't update alarm")
            }
            .doOnSuccess { RxBus.publish(DatabaseNotifiers.Updated) }
    }

    override fun addAlarm(alarm: Alarm): Single<Alarm> {
        return AlarmEntity(alarm).insert()
            .flatMap { alarmId ->
                if (alarmId == Model.INVALID_ROW_ID) {
                    throw AlarmException("Couldn't save alarm")
                }
                getAlarmDomain(alarmId)
            }
            .doOnSuccess { RxBus.publish(DatabaseNotifiers.Saved) }
    }

    override fun updateAlarm(alarm: Alarm): Single<Alarm> {
        return AlarmEntity(alarm).update()
            .flatMap { isUpdated ->
                if (isUpdated) {
                    return@flatMap getAlarmDomain(alarm.id)
                }
                throw AlarmException(
                    "Couldn't update alarm"
                )
            }
            .doOnSuccess { RxBus.publish(DatabaseNotifiers.Updated) }
    }

    fun getAlarmsQuery(): Single<MutableList<AlarmEntity>> =
        select.from(AlarmEntity::class.java).rx().queryList()

    private fun getAlarmDomain(alarmId: Long): Single<Alarm> =
        getSingleAlarm(alarmId).map { it.toDomain() }.toSingle()

    private fun getSingleAlarm(alarmId: Long) =
        (select from AlarmEntity::class where AlarmEntity_Table.id.`is`(alarmId)).rx().querySingle()

}