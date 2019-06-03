package com.helpfulapps.data.db.alarm.model

import com.helpfulapps.data.db.AlarmAppDatabase
import com.helpfulapps.data.db.alarm.model.AlarmEntry.Companion.NAME
import com.helpfulapps.domain.model.Alarm
import com.raizlabs.android.dbflow.annotation.ForeignKey
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.rx2.structure.BaseRXModel

@Table(name = NAME, database = AlarmAppDatabase::class, allFields = true)
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
    var endTime: Long = 0,
    @ForeignKey(saveForeignKeyModel = true)
    var daysOfWeek: DaysOfWeekEntry? = DaysOfWeekEntry()

    ) :BaseRXModel() {

    companion object {
        const val NAME = "AlarmTable"
    }

    constructor(alarm: Alarm) : this(){
        this.id = alarm.id
        this.name = alarm.name
        this.isRepeating = alarm.isRepeating
        this.isVibrationOn = alarm.isVibrationOn
        this.isTurnedOn = alarm.isTurnedOn
        this.ringtoneId = alarm.ringtoneId
        this.startTime = alarm.startTime
        this.endTime = alarm.endTime
        this.daysOfWeek = DaysOfWeekEntry(alarm.repetitionDays)
    }

    constructor(
        id: Long,
        name: String,
        isRepeating: Boolean,
        isVibrationOn: Boolean,
        isTurnedOn: Boolean,
        ringtoneId: Int,
        startTime: Long,
        endTime: Long,
        days: Array<Boolean>
    ) : this(){

        this.id = id
        this.name = name
        this.isRepeating = isRepeating
        this.isVibrationOn = isVibrationOn
        this.isTurnedOn = isTurnedOn
        this.ringtoneId = ringtoneId
        this.startTime = startTime
        this.endTime = endTime
        this.daysOfWeek = DaysOfWeekEntry(days)
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
        daysOfWeek!!.toDomain()
    )

    override fun hashCode(): Int = this.id.toInt()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AlarmEntry

        if (id != other.id) return false
        if (name != other.name) return false
        if (isRepeating != other.isRepeating) return false
        if (isVibrationOn != other.isVibrationOn) return false
        if (isTurnedOn != other.isTurnedOn) return false
        if (ringtoneId != other.ringtoneId) return false
        if (startTime != other.startTime) return false
        if (endTime != other.endTime) return false
        if (daysOfWeek != other.daysOfWeek) return false

        return true
    }


}