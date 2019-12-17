package com.helpfulapps.db.alarm.models

import com.helpfulapps.data.db.alarm.model.AlarmData
import com.helpfulapps.data.db.alarm.model.DaysOfWeekData
import com.helpfulapps.db.AlarmAppDatabase
import com.helpfulapps.db.alarm.models.AlarmEntity.Companion.NAME
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
    var ringtoneTitle: String = "",
    var isUsingNFC: Boolean = false,
    var hour: Int = 0,
    var minute: Int = 0,
    @ForeignKey(saveForeignKeyModel = true)
    var daysOfWeek: DaysOfWeekEntity? = null

) : BaseRXModel() {

    companion object {
        const val NAME = "AlarmTable"
    }

    constructor(domainAlarm: AlarmData) : this(
        id = domainAlarm.id,
        name = domainAlarm.name,
        isRepeating = domainAlarm.isRepeating,
        isVibrationOn = domainAlarm.isVibrationOn,
        isTurnedOn = domainAlarm.isTurnedOn,
        ringtoneId = domainAlarm.ringtoneId,
        ringtoneTitle = domainAlarm.ringtoneTitle,
        isUsingNFC = domainAlarm.isUsingNFC,
        hour = domainAlarm.hour,
        minute = domainAlarm.minute,
        daysOfWeek = DaysOfWeekEntity(domainAlarm.daysOfWeek)
    )

    fun toData(): AlarmData =
        AlarmData(
            id,
            name,
            isRepeating,
            isVibrationOn,
            isTurnedOn,
            ringtoneId,
            ringtoneTitle,
            isUsingNFC,
            hour,
            minute,
            daysOfWeek?.toData() ?: DaysOfWeekData()
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
        if (isUsingNFC != other.isUsingNFC) return false
        if (hour != other.hour) return false
        if (ringtoneTitle != other.ringtoneTitle) return false
        if (minute != other.minute) return false
        if (daysOfWeek != other.daysOfWeek) return false

        return true
    }

}