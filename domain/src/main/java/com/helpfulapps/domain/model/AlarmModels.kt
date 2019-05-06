package com.helpfulapps.domain.model


data class Alarm(
    val id: Long,
    val name: String,
    val isRepeating: Boolean,
    val isVibrationOn: Boolean,
    val isTurnedOn: Boolean,
    val ringtoneId: Int,
    val startTime: Long,
    val endTime: Long,
    val repetitionDays: IntArray

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Alarm) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}