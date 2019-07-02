package com.helpfulapps.data.api.weather.model

import org.junit.Assert.assertEquals
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
        val apiForecast = forecastApi.toDbModel()
        val dbWeather = listOf(
            WeatherDbModel(
                0,
                1,
                80,
                200.0,
                2.0,
                2.0,
                100.0,
                100.0,
                100.0,
                "desc",
                "20",
                null
            )
        )
        val dbForecast = ForecastDbModel(0, "Pszczyna", 5, dbWeather)

        assertEquals(apiForecast, dbForecast)
    }

    @Test
    fun mapWeatherFromApiToDb() {
        val dbWeather =
            WeatherDbModel(0, 1, 80, 200.0, 2.0, 2.0, 100.0, 100.0, 100.0, "desc", "20", null)
        val apiWeather = forecasts.first().toDbModel()

        assertEquals(dbWeather, apiWeather)
    }

}