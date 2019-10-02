package com.helpfulapps.domain.use_cases.alarm.definition

import com.helpfulapps.domain.models.alarm.WeatherAlarm
import com.helpfulapps.domain.use_cases.type.SingleUseCase

interface GetAlarmsUseCase : SingleUseCase<List<WeatherAlarm>>