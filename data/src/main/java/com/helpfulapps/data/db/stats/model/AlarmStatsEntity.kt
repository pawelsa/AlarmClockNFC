package com.helpfulapps.data.db.stats.model

import com.helpfulapps.data.AlarmAppDatabase
import com.helpfulapps.data.db.stats.model.AlarmStatsEntity.Companion.TABLE_NAME
import com.helpfulapps.domain.models.stats.AlarmStats
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.rx2.structure.BaseRXModel


@Table(database = AlarmAppDatabase::class, name = TABLE_NAME, allFields = true)
data class AlarmStatsEntity(
    @PrimaryKey
    var id: Long = 0,
    var dayOfWeek: Int = 0,
    var dayOfYear: Int = 0,
    var hour: Int = 0,
    var minute: Int = 0,
    var timeToStop: Int = 0
) : BaseRXModel() {

    fun toDomain(): AlarmStats {
        return AlarmStats(
            dayOfWeek, dayOfYear, hour, minute, timeToStop
        )
    }

    companion object {
        const val TABLE_NAME = "alarmStats"
    }
}