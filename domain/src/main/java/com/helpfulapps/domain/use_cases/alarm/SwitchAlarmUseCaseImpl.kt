package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.repository.AlarmClockManager
import com.helpfulapps.domain.use_cases.alarm.definition.SwitchAlarmUseCase
import io.reactivex.Completable

class SwitchAlarmUseCaseImpl(private val _repository: AlarmClockManager) : SwitchAlarmUseCase {

    //todo it should change value in the alarm to off or on and then interact with alarm manager
    override fun invoke(parameter: SwitchAlarmUseCase.Params): Completable {
        return _repository.stopAlarm(parameter.alarmId)
    }
}