package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.extensions.dayOfWeek
import com.helpfulapps.domain.extensions.dayOfYear
import com.helpfulapps.domain.helpers.TimeSetter
import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.models.stats.AlarmStats
import com.helpfulapps.domain.repository.AlarmClockManager
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.repository.StatsRepository
import com.helpfulapps.domain.use_cases.type.CompletableUseCaseWithParameter
import io.reactivex.Completable
import java.util.*

interface SnoozeAlarmUseCase : CompletableUseCaseWithParameter<SnoozeAlarmUseCase.Param> {
    data class Param(val alarmId: Long)
}

class SnoozeAlarmUseCaseImpl(
    private val alarmRepository: AlarmRepository,
    private val clockManager: AlarmClockManager,
    private val statsRepository: StatsRepository
) : SnoozeAlarmUseCase {
    override fun invoke(parameter: SnoozeAlarmUseCase.Param): Completable {

        return alarmRepository.getAlarm(parameter.alarmId)
            .flatMapCompletable { alarm ->

                val alarmStats = saveAlarmStats(alarm)

                val snoozeAlarm = clockManager.snoozeAlarm(alarm)

                return@flatMapCompletable Completable.merge(listOf(alarmStats, snoozeAlarm))
            }
    }

    private fun saveAlarmStats(alarm: Alarm): Completable {
        val timeSetter = TimeSetter()
        val alarmTime =
            timeSetter.setHourAndMinute(alarm, GregorianCalendar.getInstance()).timeInMillis

        return statsRepository.saveInfo(
            AlarmStats(
                alarmTime.dayOfWeek,
                alarmTime.dayOfYear,
                alarm.hour,
                alarm.minute,
                ((GregorianCalendar.getInstance().timeInMillis - alarmTime) / 1000).toInt()
            )
        )
    }
}