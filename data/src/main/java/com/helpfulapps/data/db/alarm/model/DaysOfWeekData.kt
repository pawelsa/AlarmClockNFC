package com.helpfulapps.data.db.alarm.model

data class DaysOfWeekData(
    var id: Long = 0L,
    var monday: Boolean = false,
    var tuesday: Boolean = false,
    var wednesday: Boolean = false,
    var thursday: Boolean = false,
    var friday: Boolean = false,
    var saturday: Boolean = false,
    var sunday: Boolean = false
) {

    constructor(array: Array<Boolean>) : this(
        monday = array[0],
        tuesday = array[1],
        wednesday = array[2],
        thursday = array[3],
        friday = array[4],
        saturday = array[5],
        sunday = array[6]
    )

    fun toDomain(): Array<Boolean> {
        return arrayOf(
            monday,
            tuesday,
            wednesday,
            thursday,
            friday,
            saturday,
            sunday
        )
    }
}