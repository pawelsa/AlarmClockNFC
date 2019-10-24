package com.helpfulapps.data.db.alarm.model

import com.helpfulapps.data.AlarmAppDatabase
import com.helpfulapps.data.db.alarm.model.AlarmEntity.Companion.NAME
import com.helpfulapps.domain.models.alarm.Alarm
import com.raizlabs.android.dbflow.annotation.ForeignKey
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.rx2.structure.BaseRXModel

// TODO move conversion to another file, ex. converter
@Table(name = NAME, database = AlarmAppDatabase::class, allFields = true)
data class AlarmEntity(

    @PrimaryKey(autoincrement = true)
    var id: Long = 0L,
    var name: String = "",
    var isRepeating: Boolean = false,
    var isVibrationOn: Boolean = true,
    var isTurnedOn: Boolean = true,
    var ringtoneId: String = "",
    var hour: Int = 0,
    var minute: Int = 0,
    @ForeignKey(saveForeignKeyModel = true)
    var daysOfWeek: DaysOfWeekEntry? = DaysOfWeekEntry()

    ) :BaseRXModel() {

    companion object {
        const val NAME = "AlarmTable"
    }

    constructor(domainAlarm: Alarm) : this() {
        this.id = domainAlarm.id
        this.name = domainAlarm.name
        this.isRepeating = domainAlarm.isRepeating
        this.isVibrationOn = domainAlarm.isVibrationOn
        this.isTurnedOn = domainAlarm.isTurnedOn
        this.ringtoneId = domainAlarm.ringtoneUrl
        this.hour = domainAlarm.hour
        this.minute = domainAlarm.minute
        this.daysOfWeek = DaysOfWeekEntry(domainAlarm.repetitionDays)
    }

    constructor(
        id: Long,
        name: String,
        isRepeating: Boolean,
        isVibrationOn: Boolean,
        isTurnedOn: Boolean,
        ringtoneId: String,
        hour: Int,
        minute: Int,
        days: Array<Boolean>
    ) : this(){

        this.id = id
        this.name = name
        this.isRepeating = isRepeating
        this.isVibrationOn = isVibrationOn
        this.isTurnedOn = isTurnedOn
        this.ringtoneId = ringtoneId
        this.hour = hour
        this.minute = minute
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
            daysOfWeek?.toDomain() ?: BooleanArray(7) { false }.toTypedArray()
        )

    override fun hashCode(): Int = this.id.toInt()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AlarmEntity

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