package com.helpfulapps.domain.use_cases.weather

import com.helpfulapps.domain.models.weather.DayWeather
import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.type.SingleUseCaseWithParameter
import io.reactivex.Single

interface GetForecastForAlarmUseCase :
    SingleUseCaseWithParameter<GetForecastForAlarmUseCase.Params, DayWeather> {
    data class Params(val timestamp: Long)
}

class GetForecastForAlarmUseCaseImpl(private val repository: WeatherRepository) :
    GetForecastForAlarmUseCase {

    override fun invoke(parameter: GetForecastForAlarmUseCase.Params): Single<DayWeather> =
        repository.getForecastForAlarm(parameter.timestamp)
}