package com.helpfulapps.data.db.alarm.model

import com.helpfulapps.data.db.AlarmAppDatabase
import com.helpfulapps.data.db.alarm.model.AlarmEntry.Companion.NAME
import com.helpfulapps.domain.model.Alarm
import com.raizlabs.android.dbflow.annotation.ColumnIgnore
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

@Table(name = NAME, database = AlarmAppDatabase::class, allFields = true)
class AlarmEntry : BaseModel {

    companion object {
        const val NAME = "AlarmTable"
    }

    @PrimaryKey(autoincrement = true)
    var id: Long = 0L

    var alarmId : Int = 0
    var name: String = ""
    var isRepeating: Boolean = false
    var isVibrationOn: Boolean = true
    var isTurnedOn: Boolean = true
    var ringtoneId: Int = 0
    var startTime: Long = 0
    var endTime: Long = 0

    @ColumnIgnore
    var repetitionDays: IntArray = intArrayOf(0)

    constructor() {

    }

    fun toDomain(): Alarm = Alarm(
        alarmId,
        name,
        isRepeating,
        isVibrationOn,
        isTurnedOn,
        ringtoneId,
        startTime,
        endTime,
        repetitionDays
    )

}