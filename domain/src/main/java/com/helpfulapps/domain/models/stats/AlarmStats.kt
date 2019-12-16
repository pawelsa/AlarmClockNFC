package com.helpfulapps.domain.models.stats

data class AlarmStats(
    val dayOfWeek: Int,
    val dayOfYear: Int,
    val hour: Int,
    val minute: Int,
    val timeToStop: Int
)