package com.helpfulapps.domain.use_cases.weather

import com.helpfulapps.domain.exceptions.WeatherException
import com.helpfulapps.domain.repository.WeatherRepository
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import org.junit.jupiter.api.Test

class DownloadForecastForLocalizationUseCaseImplTest {

    val weatherRepository: WeatherRepository = mockk {}
    val useCase = DownloadForecastForLocalizationUseCaseImpl(weatherRepository)

    @Test
    fun `should download succeed`() {
        every { weatherRepository.downloadForecast(any(), any()) } returns Completable.complete()

        useCase(DownloadForecastForLocalizationUseCase.Params(5.0, 5.0))
            .test()
            .assertComplete()
            .dispose()
    }

    @Test
    fun `should download fail`() {
        every { weatherRepository.downloadForecast(any(), any()) } returns Completable.error(
            WeatherException("failed")
        )

        useCase(DownloadForecastForLocalizationUseCase.Params(5.0, 5.0))
            .test()
            .assertError(WeatherException::class.java)
            .dispose()
    }

}