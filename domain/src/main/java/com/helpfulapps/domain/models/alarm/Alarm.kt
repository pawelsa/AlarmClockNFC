package com.helpfulapps.domain.models.alarm

import com.helpfulapps.domain.extensions.dayOfYear
import com.helpfulapps.domain.models.weather.DayWeather

data class Alarm(
    val id: Long = 0,
    val name: String = "",
    val isRepeating: Boolean = false,
    val isVibrationOn: Boolean = true,
    val isTurnedOn: Boolean = true,
    val ringtoneId: Int,
    val startTime: Long,
    val endTime: Long,
    val repetitionDays: Array<Boolean>

) {
    override operator fun equals(other: Any?): Boolean {
        return when(other) {
            this === other -> true
            is Alarm -> this.id == other.id
            is DayWeather -> this.startTime.dayOfYear == other.dt.dayOfYear
            else -> false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}