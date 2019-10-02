package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.alarm.definition.SwitchAlarmUseCase
import io.reactivex.Completable

class SwitchAlarmUseCaseImpl(private val _repository: AlarmRepository) : SwitchAlarmUseCase {

    override fun invoke(parameter: SwitchAlarmUseCase.Params): Completable =
        _repository.switchAlarm(parameter.alarmId)
}