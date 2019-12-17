package com.helpfulapps.data.db.alarm.dao

import com.helpfulapps.data.db.alarm.model.AlarmEntity
import com.helpfulapps.domain.models.alarm.Alarm
import io.reactivex.Single

interface AlarmDao {
    fun getAlarms(): Single<List<AlarmEntity>>
    fun getSingleAlarm(alarmId: Long): Single<Alarm>
}