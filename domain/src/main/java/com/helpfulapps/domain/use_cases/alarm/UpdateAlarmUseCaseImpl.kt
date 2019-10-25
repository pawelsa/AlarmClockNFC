package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.type.CompletableUseCaseWithParameter
import io.reactivex.Completable

interface UpdateAlarmUseCase : CompletableUseCaseWithParameter<UpdateAlarmUseCase.Params> {
    data class Params(val alarm: Alarm)
}

class UpdateAlarmUseCaseImpl(private val _repository: AlarmRepository) :
    UpdateAlarmUseCase {

    override fun invoke(parameter: UpdateAlarmUseCase.Params): Completable =
        _repository.updateAlarm(parameter.alarm)
}