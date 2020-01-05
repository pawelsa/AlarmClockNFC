package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.repository.AlarmClockManager
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.repository.StatsRepository
import com.helpfulapps.domain.use_cases.stats.saveAlarmStats
import com.helpfulapps.domain.use_cases.type.CompletableUseCaseWithParameter
import io.reactivex.Completable

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

                val alarmStats = statsRepository.saveInfo(saveAlarmStats(alarm))

                val snoozeAlarm = clockManager.snoozeAlarm(alarm)

                return@flatMapCompletable Completable.merge(listOf(alarmStats, snoozeAlarm))
            }
    }
}