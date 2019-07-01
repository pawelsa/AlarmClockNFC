package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.alarm.definition.UpdateAlarmUseCase
import io.reactivex.Completable

class UpdateAlarmUseCaseImpl(private val repository: AlarmRepository) :
    UpdateAlarmUseCase {

    override fun invoke(parameter: UpdateAlarmUseCase.Params): Completable =
        repository.updateAlarm(parameter.alarm)
}