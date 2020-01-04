package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.repository.AlarmClockManager
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.type.CompletableUseCaseWithParameter
import io.reactivex.Completable


interface RemoveAlarmUseCase : CompletableUseCaseWithParameter<RemoveAlarmUseCase.Params> {
    data class Params(val alarmId: Long)
}

class RemoveAlarmUseCaseImpl(
    private val clockManager: AlarmClockManager,
    private val repository: AlarmRepository
) : RemoveAlarmUseCase {

    override fun invoke(parameter: RemoveAlarmUseCase.Params): Completable =
        clockManager.stopAlarm(parameter.alarmId)
            .concatWith(repository.removeAlarm(parameter.alarmId))
}