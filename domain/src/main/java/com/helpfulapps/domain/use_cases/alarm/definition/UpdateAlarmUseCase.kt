package com.helpfulapps.domain.use_cases.alarm.definition

import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.use_cases.type.CompletableUseCaseWithParameter

interface UpdateAlarmUseCase : CompletableUseCaseWithParameter<UpdateAlarmUseCase.Params> {
    data class Params(val alarm: Alarm)
}