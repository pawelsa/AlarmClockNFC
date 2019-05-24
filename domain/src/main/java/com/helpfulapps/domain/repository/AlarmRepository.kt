package com.helpfulapps.domain.repository

import com.helpfulapps.domain.model.Alarm
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single

interface AlarmRepository {

    fun getAlarms(): Single<List<Alarm>>

    fun removeAlarm(alarmId: Long): Completable

    fun switchAlarm(alarmId: Long): Completable

    fun addAlarm(alarm: Alarm): Completable

    fun updateAlarm(alarm: Alarm): Completable

    fun getSchedulerIO() : Scheduler
}