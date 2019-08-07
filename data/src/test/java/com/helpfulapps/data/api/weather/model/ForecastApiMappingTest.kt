package com.helpfulapps.data.api.weather.model

import com.helpfulapps.data.db.converter.DayWeatherConverter
import com.helpfulapps.data.db.converter.HourWeatherConverter
import com.helpfulapps.data.db.weather.model.DayWeather
import com.helpfulapps.data.db.weather.model.HourWeather
import com.helpfulapps.data.helper.Units
import org.junit.Test

class ForecastApiMappingTest {

    val weathers = listOf(
        Weather("desc", "20", 20, "main")/*,
        Weather("desc1", "21", 21, "main1")*/
    )

    val forecasts = listOf(
        Forecast(
            Clouds(80),
            Rain(2.0),
            Snow(2.0),
            1,
            "1",
            Main(100.0, 100, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0),
            Sys("sys"),
            weathers,
            Wind(80.0, 200.0)
        )
    )
    val forecastApi = ForecastForCity(
        City(
            Coord(1.0, 1.0),
            "PL", 1, "Pszczyna", 50000, 5
        ), 1, "cod",
        forecasts, 200.0
    )


    @Test
    fun mapForecastFromApiToDb() {
        val dbWeatherList = listOf(
            HourWeather(
                tempMin = 100.0,
                tempMax = 100.0,
                temp = 100.0,
                dt = 1,
                clouds = 80,
                rain = 2.0,
                snow = 2.0,
                humidity = 100,
                pressure = 100.0,
                wind = 200.0
            )
        )
        HourWeatherConverter.analyzeWeather(dbWeatherList[0], Units.METRIC)

        val dayWeather = DayWeather(
            dt = 0,
            cityName = "Pszczyna"
        )
        DayWeatherConverter.analyzeWeather(dayWeather, Units.METRIC)

    }

    @Test
    fun mapWeatherFromApiToDb() {

    }

}