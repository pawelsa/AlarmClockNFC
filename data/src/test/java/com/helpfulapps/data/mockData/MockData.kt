package com.helpfulapps.data.mockData

import com.helpfulapps.data.db.alarm.model.AlarmData
import com.helpfulapps.data.db.alarm.model.DaysOfWeekData
import com.helpfulapps.data.db.weather.model.*
import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.models.weather.*

object MockData {

    // THU 06/20/2019 @ 2:00am
    const val BASE_TIMESTAMP = 1560996000000L
    const val HOUR = 3600 * 1000
    const val DAY_IN_HOURS = HOUR * 24

    val defaultAlarm = createDomainAlarm(id = 5, isTurnedOn = false, hour = 0, minute = 15)

    val defaultDataAlarm = createDataAlarm(id = 5, isTurnedOn = false, hour = 0, minute = 15)

    val alarmList = listOf(
        createDomainAlarm(),
        createDomainAlarm(id = 2, title = "Alarm 2"),
        createDomainAlarm(
            id = 3, title = "Alarm 3", isTurnedOn = false, isVibrationOn = false
        ),
        createDomainAlarm(
            id = 4, isRepeating = true,
            repetitionDays = arrayOf(false, true, false, false, false, false, false)
        ),
        createDomainAlarm(
            id = 5, isRepeating = true,
            repetitionDays = arrayOf(false, false, false, false, false, true, false)
        )
    )

    val alarmDataList = listOf(
        createDataAlarm(),
        createDataAlarm(id = 2, name = "Alarm 2"),
        createDataAlarm(
            id = 3, name = "Alarm 3", isTurnedOn = false, isVibrationOn = false
        ),
        createDataAlarm(
            id = 4, isRepeating = true,
            daysOfWeek = DaysOfWeekData(
                monday = false,
                tuesday = true,
                wednesday = false,
                thursday = false,
                friday = false,
                saturday = false,
                sunday = false
            )
        ),
        createDataAlarm(
            id = 5, isRepeating = true,
            daysOfWeek = DaysOfWeekData(
                monday = false,
                tuesday = false,
                wednesday = false,
                thursday = false,
                friday = false,
                saturday = true,
                sunday = false
            )
        )
    )

    fun createDomainAlarm(
        id: Long = 1,
        title: String = "Alarm 1",
        hour: Int = 10,
        minute: Int = 10,
        isTurnedOn: Boolean = true,
        isVibrationOn: Boolean = true,
        ringtoneUrl: String = "ringtoneUrl",
        ringtoneTitle: String = "ringtoneTitle",
        isUsingNfc: Boolean = false,
        isRepeating: Boolean = false,
        repetitionDays: Array<Boolean> = Array(7) { false }
    ): Alarm {
        return Alarm(
            id,
            title,
            isRepeating,
            isVibrationOn,
            isTurnedOn,
            ringtoneUrl,
            ringtoneTitle,
            isUsingNfc,
            hour,
            minute,
            repetitionDays
        )
    }

    fun createDataAlarm(
        id: Long = 1L,
        name: String = "Alarm 1",
        isRepeating: Boolean = false,
        isVibrationOn: Boolean = true,
        isTurnedOn: Boolean = true,
        ringtoneId: String = "ringtoneUrl",
        ringtoneTitle: String = "ringtoneTitle",
        isUsingNfc: Boolean = false,
        hour: Int = 10,
        minute: Int = 10,
        daysOfWeek: DaysOfWeekData = DaysOfWeekData()
    ): AlarmData {
        return AlarmData(
            id,
            name,
            isRepeating,
            isVibrationOn,
            isTurnedOn,
            ringtoneId,
            ringtoneTitle,
            isUsingNfc,
            hour,
            minute,
            daysOfWeek
        )
    }

    val dayWeather = createDomainDayWeather()

    val shortWeatherList = listOf(
        createDomainDayWeather(),
        createDomainDayWeather(id = 2, dt = BASE_TIMESTAMP + DAY_IN_HOURS),
        createDomainDayWeather(id = 3, dt = BASE_TIMESTAMP + 4 * DAY_IN_HOURS),
        createDomainDayWeather(id = 4, dt = BASE_TIMESTAMP + 2 * DAY_IN_HOURS)
    )

    val weatherList = listOf(
        createDomainDayWeather(),
        createDomainDayWeather(id = 2, dt = BASE_TIMESTAMP + DAY_IN_HOURS),
        createDomainDayWeather(id = 3, dt = BASE_TIMESTAMP + 2 * DAY_IN_HOURS),
        createDomainDayWeather(id = 4, dt = BASE_TIMESTAMP + 3 * DAY_IN_HOURS),
        createDomainDayWeather(id = 5, dt = BASE_TIMESTAMP + 4 * DAY_IN_HOURS)
    )

    fun createDomainDayWeather(
        id: Int = 0,
        dt: Long = BASE_TIMESTAMP,
        cityName: String = "Pszczyna",
        hourWeatherList: List<HourWeather> = listOf(),
        weatherInfo: WeatherInfo = createWeatherInfo()
    ) =
        DayWeather(
            id, dt, cityName, hourWeatherList, weatherInfo
        )

    fun createHourWeather(
        id: Int = 0,
        dt: Long = BASE_TIMESTAMP,
        clouds: Int = 80,
        rain: Double = 2.0,
        snow: Double = 2.0,
        wind: Double = 200.0,
        humidity: Int = 100,
        pressure: Double = 100.0,
        temp: Double = 100.0,
        tempMax: Double = 100.0,
        tempMin: Double = 100.0,
        weatherInfo: WeatherInfo = createWeatherInfo()
    ): HourWeather {
        return HourWeather(
            id,
            dt,
            clouds,
            rain,
            snow,
            wind,
            humidity,
            pressure,
            temp,
            tempMax,
            tempMin,
            weatherInfo
        )
    }

    fun createWeatherInfo(
        rain: Rain = Rain.NO_RAIN,
        snow: Snow = Snow.NO_DATA,
        temperature: Temperature = Temperature.HOT,
        wind: Wind = Wind.NORMAL
    ): WeatherInfo {
        return WeatherInfo(
            temperature, rain, wind, snow
        )
    }


    fun createDbHourWeather(
        id: Int = 0,
        dt: Long = BASE_TIMESTAMP,
        clouds: Int = 80,
        rain: Double = 2.0,
        snow: Double = 2.0,
        wind: Double = 200.0,
        humidity: Int = 100,
        pressure: Double = 100.0,
        temp: Double = 100.0,
        tempMax: Double = 100.0,
        tempMin: Double = 100.0
    ): HourWeatherData {
        return HourWeatherData(
            id, dt, clouds, rain, snow, wind, humidity, pressure, temp, tempMax, tempMin
        )
    }

    fun createDbWeatherInfo(
        rain: RainData = RainData.NO_RAIN,
        snow: SnowData = SnowData.NO_DATA,
        temperature: TemperatureData = TemperatureData.HOT,
        wind: WindData = WindData.NORMAL
    ): WeatherInfoData {
        return WeatherInfoData(
            temperature = temperature,
            rain = rain,
            snow = snow,
            wind = wind
        )
    }

    fun createDbDayWeather(
        id: Int = 0,
        dt: Long = BASE_TIMESTAMP,
        cityName: String = "Pszczyna",
        hourWeatherList: List<HourWeatherData> = listOf(createDbHourWeather()),
        weatherInfo: WeatherInfoData = createDbWeatherInfo()
    ) =
        DayWeatherData(
            id,
            dt,
            cityName,
            hourWeatherList = hourWeatherList,
            weatherInfo = weatherInfo
        )
}