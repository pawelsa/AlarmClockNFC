package com.helpfulapps.data.db.alarm.dao

import com.helpfulapps.data.db.alarm.model.AlarmData
import io.reactivex.Single

interface AlarmDao {
    fun update(alarmData: AlarmData): Single<Boolean>
    fun insert(alarmData: AlarmData): Single<Long>
    fun delete(alarmData: AlarmData): Single<Boolean>
    fun getAlarms(): Single<List<AlarmData>>
    fun getSingleAlarm(alarmId: Long): Single<AlarmData>
}