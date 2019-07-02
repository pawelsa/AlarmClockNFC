package com.helpfulapps.domain.use_cases.weather.definition

import com.helpfulapps.domain.models.weather.DayWeather
import com.helpfulapps.domain.use_cases.type.SingleUseCase

interface GetForecastForAlarmsUseCase : SingleUseCase<List<DayWeather>>