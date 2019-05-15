package com.helpfulapps.data.db.alarm.model

import com.helpfulapps.data.db.AlarmAppDatabase
import com.helpfulapps.data.db.alarm.model.DayOfWeekEntry.Companion.NAME
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

@Table(name = NAME, database = AlarmAppDatabase::class)
class DayOfWeekEntry : BaseModel {

    companion object{
        const val NAME = "DayOfWeekTable"
    }

    @PrimaryKey
    @Column
    var id : Long = 0L

    constructor(){

    }
}