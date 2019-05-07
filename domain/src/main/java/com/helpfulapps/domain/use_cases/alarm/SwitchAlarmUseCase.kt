package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.type.CompletableUseCaseWithParameter
import io.reactivex.Completable

class SwitchAlarmUseCase(private val repository: AlarmRepository) :
    CompletableUseCaseWithParameter<Int> {

    override fun execute(alarmId: Int): Completable = repository.switchAlarm(alarmId)
}