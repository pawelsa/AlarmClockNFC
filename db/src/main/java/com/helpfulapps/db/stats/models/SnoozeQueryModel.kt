package com.helpfulapps.db.stats.models

import com.helpfulapps.data.db.stats.model.SnoozeData
import com.helpfulapps.db.AlarmAppDatabase
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.QueryModel

@QueryModel(database = AlarmAppDatabase::class)
data class SnoozeQueryModel(
    @Column
    var noSnoozes: Int = 0,
    @Column
    var dayOfWeek: Int = 0
) {
    fun toData(): SnoozeData = SnoozeData(noSnoozes, dayOfWeek)
}