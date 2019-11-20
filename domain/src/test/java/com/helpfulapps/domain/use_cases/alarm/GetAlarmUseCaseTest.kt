package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.helpers.singleOf
import com.helpfulapps.domain.models.alarm.WeatherAlarm
import com.helpfulapps.domain.models.weather.DayWeather
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.mockData.MockData
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.Test

class GetAlarmUseCaseTest {

    private val alarmRepository: AlarmRepository = mockk {}
    private val _weatherRepository: WeatherRepository = mockk {}
    private val useCase = GetAlarmUseCaseImpl(alarmRepository, _weatherRepository)

    @Test
    fun `should obtain alarm`() {
        every { alarmRepository.getAlarm(any()) } returns singleOf { MockData.pairs[0].alarm }
        every { _weatherRepository.getForecastForAlarm(any()) } returns singleOf { MockData.pairs[0].dayWeather }

        useCase(GetAlarmUseCase.Params(1))
            .test()
            .assertResult(MockData.pairs[0])
            .dispose()
    }

    @Test
    fun `should obtaining alarm fail`() {
        every { alarmRepository.getAlarm(any()) } returns Single.error(Exception())

        useCase(GetAlarmUseCase.Params(1))
            .test()
            .assertError(Exception::class.java)
            .dispose()
    }

    @Test
    fun `should obtaining weather fail`() {
        every { alarmRepository.getAlarm(any()) } returns singleOf { MockData.pairs[0].alarm }
        every { _weatherRepository.getForecastForAlarm(any()) } returns Single.error(Exception())

        useCase(GetAlarmUseCase.Params(1))
            .test()
            .assertResult(WeatherAlarm(MockData.pairs[0].alarm, DayWeather()))
            .dispose()
    }

}