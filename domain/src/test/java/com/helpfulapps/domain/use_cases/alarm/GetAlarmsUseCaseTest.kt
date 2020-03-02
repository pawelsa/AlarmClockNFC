package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.models.alarm.WeatherAlarm
import com.helpfulapps.domain.models.weather.DayWeather
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.BaseUseCaseTest
import com.helpfulapps.domain.use_cases.mockData.MockData
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.reactivex.Single
import org.junit.jupiter.api.Test
import java.util.*

class GetAlarmsUseCaseTest : BaseUseCaseTest<GetAlarmsUseCase>() {

    private val mockedWeatherRepository: WeatherRepository = mockk {}
    private val mockedAlarmRepository: AlarmRepository = mockk {}
    override val useCase = GetAlarmsUseCaseImpl(mockedAlarmRepository, mockedWeatherRepository)


    @Test
    fun `should get only alarms`() {

        val expectedResult = listOf(
            WeatherAlarm(MockData.alarmList[0], DayWeather()),
            WeatherAlarm(MockData.alarmList[1], DayWeather()),
            WeatherAlarm(MockData.alarmList[2], DayWeather())
        )

        every { mockedWeatherRepository.getForecastForAlarms() } returns Single.just(emptyList())
        every { mockedAlarmRepository.getAlarms() } returns Single.just(
            MockData.alarmList.subList(
                0,
                3
            )
        )

        useCase()
            .test()
            .assertResult(expectedResult)
            .dispose()
    }

    @Test
    fun `should get only alarms when weather fails`() {

        val expectedResult = listOf(
            WeatherAlarm(MockData.alarmList[0], DayWeather()),
            WeatherAlarm(MockData.alarmList[1], DayWeather()),
            WeatherAlarm(MockData.alarmList[2], DayWeather())
        )

        every { mockedWeatherRepository.getForecastForAlarms() } returns Single.error(Exception())
        every { mockedAlarmRepository.getAlarms() } returns Single.just(
            MockData.alarmList.subList(
                0,
                3
            )
        )

        useCase()
            .test()
            .assertResult(expectedResult)
            .dispose()
    }

    @Test
    fun `should obtain alarms with weather data`() {

        val expectedResult = listOf(
            WeatherAlarm(MockData.alarmList[0], MockData.shortWeatherList[0]),
            WeatherAlarm(MockData.alarmList[1], MockData.shortWeatherList[0]),
            WeatherAlarm(MockData.alarmList[2], DayWeather()),
            WeatherAlarm(MockData.alarmList[3], DayWeather()),
            WeatherAlarm(MockData.alarmList[4], MockData.shortWeatherList[3])
        )
        val calendar = mock<Calendar>()
        mockkStatic(Calendar::class)
        every { Calendar.getInstance() } returns calendar
        whenever(calendar.timeInMillis).thenReturn(1560996000000L)

        every { mockedWeatherRepository.getForecastForAlarms() } returns Single.just(MockData.shortWeatherList)
        every { mockedAlarmRepository.getAlarms() } returns Single.just(MockData.alarmList)


        useCase()
            .test()
            .assertResult(expectedResult)
            .dispose()
    }

    @Test
    fun `should return alarm, after downloading forecast at 23`() {

        val expectedResult = listOf(
            WeatherAlarm(MockData.alarmList[0], MockData.shortWeatherList[0]),
            WeatherAlarm(MockData.alarmList[1], MockData.shortWeatherList[0]),
            WeatherAlarm(MockData.alarmList[2], DayWeather())
        )

        val calendar = mock<Calendar>()
        mockkStatic(Calendar::class)
        every { Calendar.getInstance() } returns calendar
        whenever(calendar.timeInMillis).thenReturn(1560987490000L)

        every { mockedWeatherRepository.getForecastForAlarms() } returns Single.just(
            MockData.shortWeatherList.drop(
                1
            )
        )
        every { mockedAlarmRepository.getAlarms() } returns Single.just(MockData.alarmList.take(3))


        useCase()
            .test()
            .assertResult(expectedResult)
            .dispose()
    }


}