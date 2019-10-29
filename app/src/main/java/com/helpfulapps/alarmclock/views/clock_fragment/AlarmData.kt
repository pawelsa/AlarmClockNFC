package com.helpfulapps.alarmclock.views.clock_fragment

import com.helpfulapps.alarmclock.helpers.timeToString
import com.helpfulapps.domain.models.alarm.Alarm

data class AlarmData(
    val id: Long = 0,
    var title: String = "",
    var isTurnedOn: Boolean,
    var isExpanded: Boolean = false,
    var weatherIcon: Int,
    var isVibrationOn: Boolean = true,
    var ringtoneUrl: String,
    var ringtoneTitle: String,
    var hour: Int,
    var minute: Int,
    var alarmTime: String = timeToString(hour, minute),
    var isRepeating: Boolean = false,
    var repetitionDays: Array<Boolean>
) {

    fun toDomain(): Alarm {
        return Alarm(
            id,
            title,
            isRepeating,
            isVibrationOn,
            isTurnedOn,
            ringtoneUrl,
            ringtoneTitle,
            hour,
            minute,
            repetitionDays
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AlarmData

        if (id != other.id) return false
        if (title != other.title) return false
        if (isTurnedOn != other.isTurnedOn) return false
        if (isExpanded != other.isExpanded) return false
        if (weatherIcon != other.weatherIcon) return false
        if (isVibrationOn != other.isVibrationOn) return false
        if (ringtoneUrl != other.ringtoneUrl) return false
        if (ringtoneTitle != other.ringtoneTitle) return false
        if (hour != other.hour) return false
        if (minute != other.minute) return false
        if (alarmTime != other.alarmTime) return false
        if (isRepeating != other.isRepeating) return false
        if (!repetitionDays.contentEquals(other.repetitionDays)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + isTurnedOn.hashCode()
        result = 31 * result + isExpanded.hashCode()
        result = 31 * result + weatherIcon
        result = 31 * result + isVibrationOn.hashCode()
        result = 31 * result + ringtoneUrl.hashCode()
        result = 31 * result + ringtoneTitle.hashCode()
        result = 31 * result + hour
        result = 31 * result + minute
        result = 31 * result + alarmTime.hashCode()
        result = 31 * result + isRepeating.hashCode()
        result = 31 * result + repetitionDays.contentHashCode()
        return result
    }

}