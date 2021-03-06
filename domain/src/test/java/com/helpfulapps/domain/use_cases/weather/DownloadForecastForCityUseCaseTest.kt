package com.helpfulapps.domain.use_cases.weather

import com.helpfulapps.domain.exceptions.WeatherException
import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.BaseUseCaseTest
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import org.junit.jupiter.api.Test

class DownloadForecastForCityUseCaseTest : BaseUseCaseTest<DownloadForecastForCityUseCase>() {

    private val weatherRepository: WeatherRepository = mockk {}
    override val useCase = DownloadForecastForCityUseCaseImpl(weatherRepository)

    @Test
    fun `should download forecast`() {
        every { weatherRepository.downloadForecast(any()) } returns Completable.complete()
        useCase(DownloadForecastForCityUseCase.Params("Pszczyna"))
            .test()
            .assertComplete()
            .dispose()
    }

    @Test
    fun `should download fail`() {
        every { weatherRepository.downloadForecast(any()) } returns Completable.error(
            WeatherException("failed")
        )
        useCase(DownloadForecastForCityUseCase.Params("Pszczyna"))
            .test()
            .assertError(WeatherException::class.java)
            .dispose()
    }

}