package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.repository.AlarmClockManager
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.type.CompletableUseCaseWithParameter
import io.reactivex.Completable

interface SnoozeAlarmUseCase : CompletableUseCaseWithParameter<SnoozeAlarmUseCase.Param> {
    data class Param(val alarmId: Long)
}

class SnoozeAlarmUseCaseImpl(
    private val alarmRepository: AlarmRepository,
    private val clockManager: AlarmClockManager
) : SnoozeAlarmUseCase {
    override fun invoke(parameter: SnoozeAlarmUseCase.Param): Completable {
        return alarmRepository.getAlarm(parameter.alarmId)
            .flatMapCompletable {
                clockManager.snoozeAlarm(it)
            }
    }
}