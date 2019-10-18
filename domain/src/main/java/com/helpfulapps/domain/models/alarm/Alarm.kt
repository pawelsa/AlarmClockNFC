package com.helpfulapps.domain.models.alarm

data class Alarm(
    val id: Long = 0,
    val name: String = "",
    val isRepeating: Boolean = false,
    val isVibrationOn: Boolean = true,
    val isTurnedOn: Boolean = true,
    val ringtoneId: Int,
    val hours: Int,
    val minutes: Int,
    val repetitionDays: Array<Boolean>

) {
    override operator fun equals(other: Any?): Boolean {
        return when(other) {
            this === other -> true
            is Alarm -> this.id == other.id
//            is DayWeather -> this.hours.dayOfYear == other.dt.dayOfYear
            else -> false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}