package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.type.CompletableUseCaseWithParameter
import io.reactivex.Completable

class AddAlarmUseCase(private val repository: AlarmRepository) :
    CompletableUseCaseWithParameter<Alarm> {

    override fun execute(alarm: Alarm): Completable = repository.addAlarm(alarm)
}