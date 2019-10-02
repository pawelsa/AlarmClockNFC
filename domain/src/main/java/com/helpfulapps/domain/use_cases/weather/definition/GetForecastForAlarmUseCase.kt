package com.helpfulapps.domain.use_cases.weather.definition

import com.helpfulapps.domain.models.weather.DayWeather
import com.helpfulapps.domain.use_cases.type.SingleUseCaseWithParameter

interface GetForecastForAlarmUseCase :
    SingleUseCaseWithParameter<GetForecastForAlarmUseCase.Params, DayWeather> {
    data class Params(val timestamp: Long)
}