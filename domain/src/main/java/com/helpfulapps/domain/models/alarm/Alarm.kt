package com.helpfulapps.domain.models.alarm

import com.helpfulapps.domain.extensions.dayOfWeek
import com.helpfulapps.domain.extensions.dayOfYear
import com.helpfulapps.domain.models.weather.DayWeather
import java.util.*

data class Alarm(
    val id: Long = 0,
    val title: String = "",
    val isRepeating: Boolean = false,
    val isVibrationOn: Boolean = true,
    val isTurnedOn: Boolean = true,
    val ringtoneUrl: String,
    val ringtoneTitle: String,
    val hour: Int,
    val minute: Int,
    val repetitionDays: Array<Boolean>

) {
    override operator fun equals(other: Any?): Boolean {

        if (other is DayWeather) {
            println("DayWeather id: ${other.id}, dt: ${other.dt}, dayOfWeek: ${other.dt.dayOfWeek}")
        }

        return when (other) {
            this === other -> true
            is Alarm -> this.id == other.id
            is DayWeather -> {

                val otherDayOfWeek = other.dt.dayOfWeek.let {
                    if (it == 1) 6 else it - 1
                }

                this.isTurnedOn &&
                        ((this.isRepeating && this.repetitionDays[other.dt.dayOfWeek]) ||
                                (!this.isRepeating &&
                                        Calendar.getInstance().timeInMillis.dayOfYear == other.dt.dayOfYear))
            }
            else -> false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}