package com.helpfulapps.domain.use_cases.weather

import com.helpfulapps.domain.models.weather.*
import com.helpfulapps.domain.repository.WeatherRepository
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.Test

class GetForecastForAlarmsUseCaseImplTest {

    val weatherRepository: WeatherRepository = mockk {}
    val useCase = GetForecastForAlarmsUseCaseImpl(weatherRepository)

    @Test
    fun `should obtain forecast for alarm`() {
        every { weatherRepository.getForecastForAlarms() } returns Single.just(weatherList)

        useCase()
            .test()
            .assertResult(weatherList)
            .dispose()
    }

    @Test
    fun `should not obtain forecast for alarm`() {
        every { weatherRepository.getForecastForAlarms() } returns Single.just(listOf())

        useCase()
            .test()
            .assertResult(listOf())
            .dispose()
    }

    val weatherList = listOf(
        DayWeather(
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
        ),
        DayWeather(
            id = 2,
            cityName = "Pszczyna",
            dt = 1561161600,
            hourWeatherList = listOf(),
            weatherInfo = WeatherInfo(
                rain = Rain.NO_RAIN,
                snow = Snow.NO_DATA,
                temperature = Temperature.HOT,
                wind = Wind.NORMAL
            )
        ),
        DayWeather(
            id = 3,
            cityName = "Pszczyna",
            dt = 1561075400,
            hourWeatherList = listOf(),
            weatherInfo = WeatherInfo(
                rain = Rain.NO_RAIN,
                snow = Snow.NO_DATA,
                temperature = Temperature.HOT,
                wind = Wind.NORMAL
            )
        ),
        DayWeather(
            id = 4,
            cityName = "Pszczyna",
            dt = 1561251600,
            hourWeatherList = listOf(),
            weatherInfo = WeatherInfo(
                rain = Rain.NO_RAIN,
                snow = Snow.NO_DATA,
                temperature = Temperature.HOT,
                wind = Wind.NORMAL
            )
        ),
        DayWeather(
            id = 5,
            cityName = "Pszczyna",
            dt = 1558310400,
            hourWeatherList = listOf(),
            weatherInfo = WeatherInfo(
                rain = Rain.NO_RAIN,
                snow = Snow.NO_DATA,
                temperature = Temperature.HOT,
                wind = Wind.NORMAL
            )
        ),
        DayWeather(
            id = 6,
            cityName = "Pszczyna",
            dt = 1561334600,
            hourWeatherList = listOf(),
            weatherInfo = WeatherInfo(
                rain = Rain.NO_RAIN,
                snow = Snow.NO_DATA,
                temperature = Temperature.HOT,
                wind = Wind.NORMAL
            )
        )
    )


}