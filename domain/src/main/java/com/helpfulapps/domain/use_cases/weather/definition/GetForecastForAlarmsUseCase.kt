package com.helpfulapps.domain.use_cases.weather.definition

import com.helpfulapps.domain.models.weather.Forecast
import com.helpfulapps.domain.use_cases.type.SingleUseCase

interface GetForecastForAlarmsUseCase : SingleUseCase<Forecast>