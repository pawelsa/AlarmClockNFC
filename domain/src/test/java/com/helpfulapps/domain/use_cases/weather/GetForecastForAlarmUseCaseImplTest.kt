package com.helpfulapps.domain.use_cases.weather

import com.helpfulapps.domain.models.weather.*
import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.weather.definition.GetForecastForAlarmUseCase
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.Test

class GetForecastForAlarmUseCaseImplTest {

    private val weatherRepository: WeatherRepository = mockk {}
    val useCase = GetForecastForAlarmUseCaseImpl(weatherRepository)

    @Test
    fun `should obtain forecast for alarm`() {
        every { weatherRepository.getForecastForAlarm(any()) } returns Single.just(dayWeather)

        useCase(GetForecastForAlarmUseCase.Params(5))
            .test()
            .assertResult(dayWeather)
            .dispose()
    }

    @Test
    fun `weather db is empty, should return empty list`() {
        every { weatherRepository.getForecastForAlarm(any()) } returns Single.just(DayWeather(id = -1))

        useCase(GetForecastForAlarmUseCase.Params(5))
            .test()
            .assertResult(DayWeather(id = -1))
            .dispose()
    }

    val dayWeather = DayWeather(
        id = 1,
        cityName = "Pszczyna",
        dt = 1560996000,
        hourWeatherList = listOf(),
        weatherInfo = WeatherInfo(
            rain = Rain.NO_RAIN,
            snow = Snow.NO_DATA,
            temperature = Temperature.HOT,
            wind = Wind.NORMAL
        )
    )

}