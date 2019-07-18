package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.alarm.definition.RemoveAlarmUseCase
import io.reactivex.Completable

class RemoveAlarmUseCaseImpl(private val _repository: AlarmRepository) : RemoveAlarmUseCase {

    override fun invoke(parameter: RemoveAlarmUseCase.Params): Completable =
        _repository.removeAlarm(parameter.alarmId)
}