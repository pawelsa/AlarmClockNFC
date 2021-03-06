package com.helpfulapps.domain.use_cases.weather

import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.BaseUseCaseTest
import com.helpfulapps.domain.use_cases.mockData.MockData
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.jupiter.api.Test

class GetForecastForAlarmsUseCaseImplTest : BaseUseCaseTest<GetForecastForAlarmsUseCase>() {

    private val weatherRepository: WeatherRepository = mockk {}
    override val useCase = GetForecastForAlarmsUseCaseImpl(weatherRepository)

    @Test
    fun `should obtain forecast for alarm`() {
        every { weatherRepository.getForecastForAlarms() } returns Single.just(MockData.weatherList)

        useCase()
            .test()
            .assertResult(MockData.weatherList)
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


}