package com.helpfulapps.data.repositories

import com.helpfulapps.data.db.alarm.dao.AlarmDao
import com.helpfulapps.data.db.alarm.model.AlarmData
import com.helpfulapps.data.db.alarm.model.AlarmEntity_Table
import com.helpfulapps.data.extensions.checkCompleted
import com.helpfulapps.domain.eventBus.DatabaseNotifiers
import com.helpfulapps.domain.eventBus.RxBus
import com.helpfulapps.domain.exceptions.AlarmException
import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.repository.AlarmRepository
import com.raizlabs.android.dbflow.structure.Model
import io.reactivex.Completable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

// TODO remove subscribing on bg thread
open class AlarmRepositoryImpl(
    private val alarmDao: AlarmDao
) : AlarmRepository {

    private val TAG = AlarmRepositoryImpl::class.java.simpleName

    override fun getAlarm(id: Long): Single<Alarm> {
        return getSingleAlarm(id).flatMap { Single.just(it.toDomain()) }
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
            .flatMap(alarmDao::delete)
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
            .flatMap(alarmDao::update)
            .flatMap { isUpdated ->
                if (isUpdated) {
                    return@flatMap getAlarmDomain(alarmId)
                }
                throw AlarmException("Couldn't update alarm")
            }
            .doOnSuccess { RxBus.publish(DatabaseNotifiers.Updated) }
    }

    override fun addAlarm(alarm: Alarm): Single<Alarm> {
        return alarmDao.insert(AlarmData(alarm))
            .flatMap { alarmId ->
                if (alarmId == Model.INVALID_ROW_ID) {
                    throw AlarmException("Couldn't save alarm")
                }
                getAlarmDomain(alarmId)
            }
            .doOnSuccess { RxBus.publish(DatabaseNotifiers.Saved) }
    }

    override fun updateAlarm(alarm: Alarm): Single<Alarm> {
        return alarmDao.update(AlarmData(alarm))
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

    fun getAlarmsQuery(): Single<List<AlarmData>> =
        alarmDao.getAlarms()

    private fun getSingleAlarm(alarmId: Long) =
        alarmDao.getSingleAlarm(alarmId)

    private fun getAlarmDomain(alarmId: Long): Single<Alarm> =
        getSingleAlarm(alarmId).map { it.toDomain() }

}