package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.type.CompletableUseCaseWithParameter
import io.reactivex.Completable

class SwitchAlarmUseCase(private val repository: AlarmRepository) :
    CompletableUseCaseWithParameter<Long> {

    override fun execute(alarmId: Long): Completable = repository.switchAlarm(alarmId)
}