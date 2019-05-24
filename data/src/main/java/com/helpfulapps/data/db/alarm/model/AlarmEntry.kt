package com.helpfulapps.data.db.alarm.model

import com.helpfulapps.data.db.AlarmAppDatabase
import com.helpfulapps.domain.model.Alarm
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.rx2.structure.BaseRXModel

@Table(database = AlarmAppDatabase::class, allFields = true)
data class AlarmEntry(

    //todo ogarnąć ID w alarmach, bo będzie problem zaraz

    @PrimaryKey(autoincrement = true)
    var id: Long = 0L,

    var name: String = "",
    var isRepeating: Boolean = false,
    var isVibrationOn: Boolean = true,
    var isTurnedOn: Boolean = true,
    var ringtoneId: Int = 0,
    var startTime: Long = 0,
    var endTime: Long = 0

    ) :BaseRXModel() {

    companion object {
        const val NAME = "AlarmTable"
    }

    constructor(alarm: Alarm) : this(){
        this.name = alarm.name
        this.isRepeating = alarm.isRepeating
        this.isVibrationOn = alarm.isVibrationOn
        this.isTurnedOn = alarm.isTurnedOn
        this.ringtoneId = alarm.ringtoneId
        this.startTime = alarm.startTime
        this.endTime = alarm.endTime
    }

    fun toDomain(): Alarm = Alarm(
        id,
        name,
        isRepeating,
        isVibrationOn,
        isTurnedOn,
        ringtoneId,
        startTime,
        endTime,
        IntArray(7)
    )
}