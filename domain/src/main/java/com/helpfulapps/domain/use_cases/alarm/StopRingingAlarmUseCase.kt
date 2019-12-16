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

interface StopRingingAlarmUseCase : CompletableUseCaseWithParameter<StopRingingAlarmUseCase.Param> {
    data class Param(val alarm: Alarm)
}

class StopRingingAlarmUseCaseImpl(
    private val alarmRepository: AlarmRepository,
    private val alarmClockManager: AlarmClockManager,
    private val statsRepository: StatsRepository
) : StopRingingAlarmUseCase {
    override fun invoke(parameter: StopRingingAlarmUseCase.Param): Completable {
        var alarm = parameter.alarm
        if (!alarm.isRepeating) {
            alarm = alarm.copy(isTurnedOn = false)
        }

        val manageAlarm = alarmRepository.updateAlarm(alarm)
            .flatMapCompletable {
                return@flatMapCompletable if (alarm.isRepeating) {
                    alarmClockManager.setAlarm(alarm)
                } else {
                    Completable.complete()
                }
            }

        val timeSetter = TimeSetter()
        val alarmTime =
            timeSetter.setHourAndMinute(alarm, GregorianCalendar.getInstance()).timeInMillis


        val saveStats = statsRepository.saveInfo(
            AlarmStats(
                alarmTime.dayOfWeek,
                alarmTime.dayOfYear,
                alarm.hour,
                alarm.minute,
                ((GregorianCalendar.getInstance().timeInMillis - alarmTime) / 1000).toInt()
            )
        )

        return Completable.merge(listOf(manageAlarm, saveStats))
    }
}