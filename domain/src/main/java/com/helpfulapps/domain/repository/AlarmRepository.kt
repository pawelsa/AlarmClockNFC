package com.helpfulapps.domain.repository

import com.helpfulapps.domain.model.Alarm
import io.reactivex.Completable
import io.reactivex.Single

interface AlarmRepository {

    fun getAlarms(): Single<List<Alarm>>

    fun removeAlarm(alarmId: Int): Completable

    fun switchAlarm(alarmId: Int): Completable

    fun addAlarm(alarm: Alarm): Completable

    fun updateAlarm(alarm: Alarm): Completable


}