package com.example.db.alarm.models

import com.example.db.AlarmAppDatabase
import com.example.db.alarm.models.DaysOfWeekEntity.Companion.NAME
import com.helpfulapps.data.db.alarm.model.DaysOfWeekData
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.rx2.structure.BaseRXModel

@Table(
    name = NAME,
    database = AlarmAppDatabase::class,
    allFields = true,
    useBooleanGetterSetters = false
)
data class DaysOfWeekEntity(

    @PrimaryKey(autoincrement = true)
    var id: Long = 0L,
    var monday: Boolean = false,
    var tuesday: Boolean = false,
    var wednesday: Boolean = false,
    var thursday: Boolean = false,
    var friday: Boolean = false,
    var saturday: Boolean = false,
    var sunday: Boolean = false

) : BaseRXModel() {

    companion object {
        const val NAME = "DayOfWeekTable"
    }

    constructor(weeksTable: DaysOfWeekData) : this() {
        monday = weeksTable.monday
        tuesday = weeksTable.tuesday
        wednesday = weeksTable.wednesday
        thursday = weeksTable.thursday
        friday = weeksTable.friday
        saturday = weeksTable.saturday
        sunday = weeksTable.sunday
    }

    fun toData(): DaysOfWeekData =
        DaysOfWeekData(id, monday, tuesday, wednesday, thursday, friday, saturday, sunday)

    override fun hashCode(): Int = id.toInt()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DaysOfWeekEntity

        if (id != other.id) return false
        if (monday != other.monday) return false
        if (tuesday != other.tuesday) return false
        if (wednesday != other.wednesday) return false
        if (thursday != other.thursday) return false
        if (friday != other.friday) return false
        if (saturday != other.saturday) return false
        if (sunday != other.sunday) return false

        return true
    }

}