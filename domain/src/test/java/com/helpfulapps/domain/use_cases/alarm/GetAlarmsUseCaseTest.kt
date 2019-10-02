package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.models.alarm.WeatherAlarm
import com.helpfulapps.domain.models.weather.*
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.repository.WeatherRepository
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.Test

class GetAlarmsUseCaseTest {

    val mockedWeatherRepository: WeatherRepository = mockk {}
    val mockedAlarmRepository: AlarmRepository = mockk {}
    val useCase = GetAlarmsUseCaseImpl(mockedAlarmRepository, mockedWeatherRepository)

    @Test
    fun `should get only alarms`() {

        val expectedResult = listOf(
            WeatherAlarm(alarmList[0], DayWeather()),
            WeatherAlarm(alarmList[1], DayWeather()),
            WeatherAlarm(alarmList[2], DayWeather())
        )

        every { mockedWeatherRepository.getForecastForAlarms() } returns Single.just(emptyList())
        every { mockedAlarmRepository.getAlarms() } returns Single.just(alarmList)

        useCase()
            .test()
            .assertResult(expectedResult)
            .dispose()
    }

    @Test
    fun `should obtain alarms with weather data`() {

        val expectedResult = listOf(
            WeatherAlarm(alarmList[0], weatherList[0]),
            WeatherAlarm(alarmList[1], weatherList[1]),
            WeatherAlarm(alarmList[2], weatherList[5])
        )

        every { mockedWeatherRepository.getForecastForAlarms() } returns Single.just(weatherList)
        every { mockedAlarmRepository.getAlarms() } returns Single.just(alarmList)

        useCase()
            .test()
            .assertResult(expectedResult)
            .dispose()
    }

    val alarmList = listOf(
        Alarm(
            id = 1,
            endTime = 1560996000,
            isRepeating = false,
            isTurnedOn = false,
            isVibrationOn = true,
            name = "Alarm 1",
            repetitionDays = arrayOf(false, false, false, false, false, false, false),
            ringtoneId = 1,
            startTime = 1560988800
        ),
        Alarm(
            id = 2,
            endTime = 1561168800,
            isRepeating = false,
            isTurnedOn = false,
            isVibrationOn = true,
            name = "Alarm 2",
            repetitionDays = arrayOf(false, false, false, false, false, false, false),
            ringtoneId = 2,
            startTime = 1561161600
        ),
        Alarm(
            id = 3,
            endTime = 1561341600,
            isRepeating = false,
            isTurnedOn = true,
            isVibrationOn = false,
            name = "Alarm 3",
            repetitionDays = arrayOf(false, true, false, true, false, false, false),
            ringtoneId = 5,
            startTime = 1561334400
        )
    )

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