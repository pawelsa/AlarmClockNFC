package com.helpfulapps.data.mockData

import com.helpfulapps.data.db.alarm.model.AlarmEntity
import com.helpfulapps.data.db.alarm.model.DaysOfWeekEntity
import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.models.weather.*
import com.example.api.models.Rain as ApiRain
import com.example.api.models.Snow as ApiSnow
import com.example.api.models.Wind as ApiWind
import com.helpfulapps.data.db.weather.model.DayWeatherEntity as DbDayWeather
import com.helpfulapps.data.db.weather.model.HourWeatherEntity as DbHourWeather
import com.helpfulapps.data.db.weather.model.WeatherInfoEntity as DbWeatherInfo

object MockData {

    // THU 06/20/2019 @ 2:00am
    const val BASE_TIMESTAMP = 1560996000000L
    const val HOUR = 3600 * 1000
    const val DAY_IN_HOURS = HOUR * 24

    val defaultAlarm = createDomainAlarm(id = 5, isTurnedOn = false, hour = 0, minute = 15)

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

    fun createDomainAlarm(
        id: Long = 1,
        title: String = "Alarm 1",
        hour: Int = 10,
        minute: Int = 10,
        isTurnedOn: Boolean = true,
        isVibrationOn: Boolean = true,
        ringtoneUrl: String = "ringtoneUrl",
        ringtoneTitle: String = "ringtoneTitle",
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
            hour,
            minute,
            repetitionDays
        )
    }

    fun createEntityAlarm(
        id: Long = 1L,
        name: String = "Alarm 1",
        isRepeating: Boolean = false,
        isVibrationOn: Boolean = true,
        isTurnedOn: Boolean = true,
        ringtoneId: String = "ringtoneUrl",
        ringtoneTitle: String = "ringtoneTitle",
        hour: Int = 10,
        minute: Int = 10,
        daysOfWeek: DaysOfWeekEntity = DaysOfWeekEntity()
    ): AlarmEntity {
        return AlarmEntity(
            id,
            name,
            isRepeating,
            isVibrationOn,
            isTurnedOn,
            ringtoneId,
            ringtoneTitle,
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

    fun createApiWeather(
        description: String = "desc1",
        icon: String = "icon1",
        id: Int = 1,
        main: String = "main1"
    ): com.example.api.models.Weather {
        return com.example.api.models.Weather(
            description, icon, id, main
        )
    }

    val apiWeatherList = listOf(createApiWeather())

    fun createApiForecast(
        clouds: com.example.api.models.Clouds = com.example.api.models.Clouds(
            80
        ),
        rain: ApiRain = ApiRain(2.0),
        snow: ApiSnow = ApiSnow(2.0),
        // using BASE_TIMESTAMP because we multiply timestamp provided by api by 1000, so it works with Calendar.class
        timestamp: Long = BASE_TIMESTAMP,
        timestampText: String = "1",
        main: com.example.api.models.Main = com.example.api.models.Main(
            100.0,
            100,
            100.0,
            100.0,
            100.0,
            100.0,
            100.0,
            100.0
        ),
        sys: com.example.api.models.Sys = com.example.api.models.Sys(
            "sys"
        ),
        weathers: List<com.example.api.models.Weather> = apiWeatherList,
        wind: ApiWind = ApiWind(80.0, 200.0)
    ): com.example.api.models.Forecast {
        return com.example.api.models.Forecast(
            clouds, rain, snow, timestamp, timestampText, main, sys, weathers, wind
        )
    }

    val forecasts = listOf(
        createApiForecast()
    )

    fun createCity(
        coord: com.example.api.models.Coord = com.example.api.models.Coord(
            1.0,
            1.0
        ),
        country: String = "PL",
        id: Int = 1,
        name: String = "Pszczyna",
        population: Int = 50000,
        timezone: Int = 5
    ): com.example.api.models.City {
        return com.example.api.models.City(
            coord, country, id, name, population, timezone
        )
    }

    fun createApiForecastForCity(
        city: com.example.api.models.City = createCity(),
        cnt: Int = 1,
        cod: String = "cod",
        list: List<com.example.api.models.Forecast> = forecasts,
        message: Double = 200.0
    ): com.example.api.models.ForecastForCity {
        return com.example.api.models.ForecastForCity(
            city, cnt, cod, list, message
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
    ): DbHourWeather {
        return DbHourWeather(
            id, dt, clouds, rain, snow, wind, humidity, pressure, temp, tempMax, tempMin
        )
    }

    fun createDbWeatherInfo(
        rain: Rain = Rain.NO_RAIN,
        snow: Snow = Snow.NO_DATA,
        temperature: Temperature = Temperature.HOT,
        wind: Wind = Wind.NORMAL
    ): DbWeatherInfo {
        return DbWeatherInfo(
            temperature = temperature.importance,
            rain = rain.importance,
            snow = snow.importance,
            wind = wind.importance
        )
    }

    fun createDbDayWeather(
        id: Int = 0,
        dt: Long = BASE_TIMESTAMP,
        cityName: String = "Pszczyna",
        hourWeatherList: List<DbHourWeather> = listOf(createDbHourWeather()),
        weatherInfo: DbWeatherInfo = createDbWeatherInfo()
    ) =
        DbDayWeather(
            id,
            dt,
            cityName,
            hourWeatherEntityList = hourWeatherList,
            weatherInfoEntity = weatherInfo
        )
}