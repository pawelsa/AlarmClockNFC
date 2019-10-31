package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.repository.AlarmClockManager
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.type.CompletableUseCaseWithParameter
import io.reactivex.Completable


interface SwitchAlarmUseCase : CompletableUseCaseWithParameter<SwitchAlarmUseCase.Params> {
    data class Params(val alarmId: Long)
}

class SwitchAlarmUseCaseImpl(
    private val _repository: AlarmRepository,
    private val _clockManager: AlarmClockManager
) : SwitchAlarmUseCase {

    override fun invoke(parameter: SwitchAlarmUseCase.Params): Completable {
        return _repository.switchAlarm(parameter.alarmId)
            .flatMapCompletable { alarm ->
                when (alarm.isTurnedOn) {
                    true -> _clockManager.setAlarm(alarm)
                    false -> _clockManager.stopAlarm(alarm.id)
                }
            }
    }
}