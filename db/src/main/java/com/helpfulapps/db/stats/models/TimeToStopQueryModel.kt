package com.helpfulapps.db.stats.models

import com.helpfulapps.data.db.stats.model.TimeToStopData
import com.helpfulapps.db.AlarmAppDatabase
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.QueryModel

@QueryModel(database = AlarmAppDatabase::class)
class TimeToStopQueryModel(
    @Column
    var avgStopTime: Float = 0f,
    @Column
    var dayOfWeek: Int = 0
) {
    fun toData(): TimeToStopData = TimeToStopData(avgStopTime, dayOfWeek)
}