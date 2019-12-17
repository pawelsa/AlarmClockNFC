package com.helpfulapps.db.stats.models

import com.helpfulapps.data.db.stats.model.AlarmStatsData
import com.helpfulapps.db.AlarmAppDatabase
import com.helpfulapps.db.stats.models.AlarmStatsEntity.Companion.TABLE_NAME
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.rx2.structure.BaseRXModel

@Table(database = AlarmAppDatabase::class, name = TABLE_NAME, allFields = true)
data class AlarmStatsEntity(
    @PrimaryKey(autoincrement = true)
    var id: Long = 0,
    var dayOfWeek: Int = 0,
    var dayOfYear: Int = 0,
    var hour: Int = 0,
    var minute: Int = 0,
    var timeToStop: Int = 0
) : BaseRXModel() {

    constructor(alarmStats: AlarmStatsData) : this(
        dayOfWeek = alarmStats.dayOfWeek,
        dayOfYear = alarmStats.dayOfYear,
        hour = alarmStats.hour,
        minute = alarmStats.minute,
        timeToStop = alarmStats.timeToStop
    )

    fun toData(): AlarmStatsData {
        return AlarmStatsData(
            id = id,
            dayOfWeek = dayOfWeek,
            dayOfYear = dayOfYear,
            hour = hour,
            minute = minute,
            timeToStop = timeToStop
        )
    }

    companion object {
        const val TABLE_NAME = "alarmStats"
    }
}