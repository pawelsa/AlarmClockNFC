package com.helpfulapps.domain.use_cases.mockData

import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.models.alarm.WeatherAlarm
import com.helpfulapps.domain.models.weather.*

object MockData {

    // THU 06/20/2019 @ 2:00am
    const val BASE_TIMESTAMP = 1560996000000L
    const val HOUR = 3600 * 1000
    const val DAY_IN_HOURS = HOUR * 24

    val defaultAlarm = createAlarm(id = 5, isTurnedOn = false, hour = 0, minute = 15)

    val alarmList = listOf(
        createAlarm(),
        createAlarm(id = 2),
        createAlarm(
            id = 3, isTurnedOn = false, isVibrationOn = false
        ),
        createAlarm(
            id = 4, isRepeating = true,
            repetitionDays = arrayOf(false, true, false, false, false, false, false)
        ),
        createAlarm(
            id = 5, isRepeating = true,
            repetitionDays = arrayOf(false, false, false, false, false, true, false)
        )
    )

    val lateAlarmList = listOf(
        createAlarm(hour = 23, minute = 58),
        createAlarm(id = 2, hour = 23, minute = 59),
        createAlarm(
            id = 3, isTurnedOn = false, isVibrationOn = false
        ),
        createAlarm(
            id = 4, isRepeating = true,
            repetitionDays = arrayOf(false, true, false, false, false, false, false)
        ),
        createAlarm(
            id = 5, isRepeating = true,
            repetitionDays = arrayOf(false, false, false, false, false, true, false)
        )
    )

    fun createAlarm(
        id: Long = 1,
        title: String = "Alarm $id",
        hour: Int = 10,
        minute: Int = 10,
        isTurnedOn: Boolean = true,
        isVibrationOn: Boolean = true,
        ringtoneUrl: String = "ringtoneUrl",
        ringtoneTitle: String = "ringtoneTitle",
        isNfcUsed: Boolean = false,
        isRepeating: Boolean = false,
        repetitionDays: Array<Boolean> = arrayOf(false, false, false, false, false, false, false)
    ): Alarm {
        return Alarm(
            id,
            title,
            isRepeating,
            isVibrationOn,
            isTurnedOn,
            ringtoneUrl,
            ringtoneTitle,
            isNfcUsed,
            hour,
            minute,
            repetitionDays
        )
    }


    val dayWeather = createDayWeather()

    val shortWeatherList = listOf(
        createDayWeather(),
        createDayWeather(id = 2, dt = BASE_TIMESTAMP + DAY_IN_HOURS),
        createDayWeather(id = 3, dt = BASE_TIMESTAMP + 4 * DAY_IN_HOURS),
        createDayWeather(id = 4, dt = BASE_TIMESTAMP + 2 * DAY_IN_HOURS)
    )

    val weatherList = listOf(
        createDayWeather(),
        createDayWeather(id = 2, dt = BASE_TIMESTAMP + DAY_IN_HOURS),
        createDayWeather(id = 3, dt = BASE_TIMESTAMP + 2 * DAY_IN_HOURS),
        createDayWeather(id = 4, dt = BASE_TIMESTAMP + 3 * DAY_IN_HOURS),
        createDayWeather(id = 5, dt = BASE_TIMESTAMP + 4 * DAY_IN_HOURS)
    )

    fun createDayWeather(
        id: Int = 1,
        dt: Long = BASE_TIMESTAMP,
        cityName: String = "Pszczyna",
        hourWeatherList: List<HourWeather> = listOf(),
        weatherInfo: WeatherInfo = createWeatherInfo()
    ) =
        DayWeather(
            id, dt, cityName, hourWeatherList, weatherInfo
        )

    fun createWeatherInfo(
        rain: Rain = Rain.NO_RAIN,
        snow: Snow = Snow.NO_DATA,
        temperature: Temperature = Temperature.HOT,
        wind: Wind = Wind.NORMAL
    ) =
        WeatherInfo(
            temperature, rain, wind, snow
        )

    val pairs = listOf(
        WeatherAlarm(alarmList[0], shortWeatherList[0]),
        WeatherAlarm(alarmList[1], shortWeatherList[0]),
        WeatherAlarm(alarmList[2], DayWeather()),
        WeatherAlarm(alarmList[3], DayWeather()),
        WeatherAlarm(alarmList[4], shortWeatherList[3])
    )

}