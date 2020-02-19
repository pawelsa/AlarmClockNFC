package com.helpfulapps.domain.models.alarm

import com.helpfulapps.domain.extensions.dayOfWeek
import com.helpfulapps.domain.extensions.dayOfYear
import com.helpfulapps.domain.helpers.TimeSetter
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
    val isUsingNFC: Boolean,
    val hour: Int,
    val minute: Int,
    val repetitionDays: Array<Boolean>

) {


    override operator fun equals(other: Any?): Boolean {
        return when (other) {
            this === other -> true
            is Alarm -> {
                id == other.id
                        && title == other.title
                        && isRepeating == other.isRepeating
                        && isVibrationOn == other.isVibrationOn
                        && isTurnedOn == other.isTurnedOn
                        && ringtoneUrl == other.ringtoneUrl
                        && ringtoneTitle == other.ringtoneTitle
                        && isUsingNFC == other.isUsingNFC
                        && hour == other.hour
                        && minute == other.minute
                        && repetitionDays.contentEquals(other.repetitionDays)
            }
            is DayWeather -> {

                println(
                    "Alarm: id - $id,  isRepeating - ${isRepeating}, calendar - ${
                    Calendar.getInstance().timeInMillis.dayOfYear}, weather - ${other.dt.dayOfYear}"
                )

                if (!this.isTurnedOn) return false
                val otherDayOfWeek = other.dt.dayOfWeek.let {
                    if (it == 1) 6 else it - 2
                }
                if (this.isRepeating && this.repetitionDays[otherDayOfWeek]) return true

                val timeSetter = TimeSetter()
                val alarmStartTime = timeSetter.getAlarmStartingTime(this)

                val rangeOfAlarmForForecast =
                    (alarmStartTime - ONE_AND_HALF_HOUR_MILLIS)..(alarmStartTime + ONE_AND_HALF_HOUR_MILLIS)
                return !this.isRepeating &&
                        (other.dt in rangeOfAlarmForForecast || alarmStartTime.dayOfYear == other.dt.dayOfYear)
            }
            else -> false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    companion object {
        const val ONE_AND_HALF_HOUR_MILLIS = 90 * 60 * 1000
    }
}