package com.helpfulapps.data.db.alarm.model

import com.helpfulapps.data.AlarmAppDatabase
import com.helpfulapps.data.db.alarm.model.DaysOfWeekEntry.Companion.NAME
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.rx2.structure.BaseRXModel

@Table(name = NAME, database = AlarmAppDatabase::class)
class DaysOfWeekEntry(

    @PrimaryKey(autoincrement = true)
    @Column
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

    constructor(weeksTable: Array<Boolean>) : this() {
        if (weeksTable.size > 6) {
            monday = weeksTable[0]
            tuesday = weeksTable[1]
            wednesday = weeksTable[2]
            thursday = weeksTable[3]
            friday = weeksTable[4]
            saturday = weeksTable[5]
            sunday = weeksTable[6]
        }
    }

    fun toDomain(): Array<Boolean> = arrayOf(monday, tuesday, wednesday, thursday, friday, saturday, sunday)

    override fun hashCode(): Int = id.toInt()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DaysOfWeekEntry

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