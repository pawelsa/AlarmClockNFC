package com.helpfulapps.data.db.alarm.model

import com.helpfulapps.data.AlarmAppDatabase
import com.helpfulapps.data.db.alarm.model.AlarmEntry.Companion.NAME
import com.helpfulapps.domain.models.alarm.Alarm
import com.raizlabs.android.dbflow.annotation.ForeignKey
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.rx2.structure.BaseRXModel

// TODO move conversion to another file, ex. converter
@Table(name = NAME, database = AlarmAppDatabase::class, allFields = true)
data class AlarmEntry(

    @PrimaryKey(autoincrement = true)
    var id: Long = 0L,
    var name: String = "",
    var isRepeating: Boolean = false,
    var isVibrationOn: Boolean = true,
    var isTurnedOn: Boolean = true,
    var ringtoneId: Int = 0,
    var hour: Int = 0,
    var minute: Int = 0,
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
        this.hour = alarm.hours
        this.minute = alarm.minutes
        this.daysOfWeek = DaysOfWeekEntry(alarm.repetitionDays)
    }

    constructor(
        id: Long,
        name: String,
        isRepeating: Boolean,
        isVibrationOn: Boolean,
        isTurnedOn: Boolean,
        ringtoneId: Int,
        startTime: Int,
        endTime: Int,
        days: Array<Boolean>
    ) : this(){

        this.id = id
        this.name = name
        this.isRepeating = isRepeating
        this.isVibrationOn = isVibrationOn
        this.isTurnedOn = isTurnedOn
        this.ringtoneId = ringtoneId
        this.hour = startTime
        this.minute = endTime
        this.daysOfWeek = DaysOfWeekEntry(days)
    }

    fun toDomain(): Alarm =
        Alarm(
            id,
            name,
            isRepeating,
            isVibrationOn,
            isTurnedOn,
            ringtoneId,
            hour,
            minute,
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
        if (hour != other.hour) return false
        if (minute != other.minute) return false
        if (daysOfWeek != other.daysOfWeek) return false

        return true
    }


}