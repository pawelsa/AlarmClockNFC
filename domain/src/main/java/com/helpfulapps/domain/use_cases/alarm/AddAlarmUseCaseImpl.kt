package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.alarm.definition.AddAlarmUseCase
import io.reactivex.Completable

class AddAlarmUseCaseImpl(private val repository: AlarmRepository) : AddAlarmUseCase {

    override fun invoke(parameter: AddAlarmUseCase.Params): Completable =
        repository.addAlarm(parameter.alarm)
}