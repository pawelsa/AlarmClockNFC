package com.helpfulapps.domain.use_cases.alarm.definition

import com.helpfulapps.domain.use_cases.type.CompletableUseCaseWithParameter

interface RemoveAlarmUseCase : CompletableUseCaseWithParameter<RemoveAlarmUseCase.Params> {
    data class Params(val alarmId: Long)
}