package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.repository.AlarmClockManager
import com.helpfulapps.domain.use_cases.type.CompletableUseCaseWithParameter
import io.reactivex.Completable


interface AddAlarmUseCase : CompletableUseCaseWithParameter<AddAlarmUseCase.Params> {
    data class Params(val alarm: Alarm)
}

//alarms have to be working despite the set start time timestamp, it can destroy correct working of getAlarms zip
class AddAlarmUseCaseImpl(private val _repository: AlarmClockManager) : AddAlarmUseCase {

    override fun invoke(parameter: AddAlarmUseCase.Params): Completable =
        _repository.setAlarm(parameter.alarm)
}