package com.helpfulapps.domain.use_cases.weather

import com.helpfulapps.domain.models.weather.DayWeather
import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.BaseUseCaseTest
import com.helpfulapps.domain.use_cases.mockData.MockData
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.jupiter.api.Test

class GetForecastForAlarmUseCaseImplTest : BaseUseCaseTest<GetForecastForAlarmUseCase>() {

    private val weatherRepository: WeatherRepository = mockk {}
    override val useCase = GetForecastForAlarmUseCaseImpl(weatherRepository)

    @Test
    fun `should obtain forecast for alarm`() {
        every { weatherRepository.getForecastForAlarm(any()) } returns Single.just(MockData.dayWeather)

        useCase(GetForecastForAlarmUseCase.Params(5))
            .test()
            .assertResult(MockData.dayWeather)
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

}