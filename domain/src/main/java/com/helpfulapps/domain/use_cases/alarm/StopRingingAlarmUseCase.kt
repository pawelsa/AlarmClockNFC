package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.repository.AlarmClockManager
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.repository.StatsRepository
import com.helpfulapps.domain.use_cases.stats.saveAlarmStats
import com.helpfulapps.domain.use_cases.type.CompletableUseCaseWithParameter
import io.reactivex.Completable

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

        val handleRepeatingAlarm = alarmRepository.updateAlarm(alarm)
            .flatMapCompletable {
                return@flatMapCompletable if (alarm.isRepeating) {
                    alarmClockManager.setAlarm(alarm)
                } else {
                    Completable.complete()
                }
            }

        val saveStats = statsRepository.saveInfo(saveAlarmStats(alarm))

        return Completable.merge(listOf(handleRepeatingAlarm, saveStats))
    }
}