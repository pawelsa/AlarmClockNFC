package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.repository.AlarmClockManager
import com.helpfulapps.domain.use_cases.alarm.definition.AddAlarmUseCase
import io.reactivex.Completable

//alarms have to be working despite the set start time timestamp, it can destroy correct working of getAlarms zip
class AddAlarmUseCaseImpl(private val _repository: AlarmClockManager) : AddAlarmUseCase {

    override fun invoke(parameter: AddAlarmUseCase.Params): Completable =
        _repository.setAlarm(parameter.alarm)
}