package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.alarm.definition.UpdateAlarmUseCase
import io.reactivex.Completable

class UpdateAlarmUseCaseImpl(private val _repository: AlarmRepository) :
    UpdateAlarmUseCase {

    override fun invoke(parameter: UpdateAlarmUseCase.Params): Completable =
        _repository.updateAlarm(parameter.alarm)
}