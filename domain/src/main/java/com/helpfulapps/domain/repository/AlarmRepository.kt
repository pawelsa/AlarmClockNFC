package com.helpfulapps.domain.repository

import com.helpfulapps.domain.models.alarm.Alarm
import io.reactivex.Completable
import io.reactivex.Single

interface AlarmRepository {

    fun getAlarms(): Single<List<Alarm>>

    fun removeAlarm(alarmId: Long): Completable

    fun switchAlarm(alarmId: Long): Single<Alarm>

    fun addAlarm(alarm: Alarm): Single<Alarm>

    fun updateAlarm(alarm: Alarm): Completable
}


interface AlarmClockManager {
    fun setAlarm(
        domainAlarm: Alarm
    ): Completable

    fun stopAlarm(alarmId: Long): Completable
}