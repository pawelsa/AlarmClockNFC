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
)