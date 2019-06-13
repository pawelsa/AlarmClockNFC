package com.helpfulapps.data.db.weather.model

import com.helpfulapps.domain.models.weather.Forecast
import com.helpfulapps.domain.models.weather.Weather
import org.junit.Assert.assertEquals
import org.junit.Test

class ForecastDbModelMappingTest {

    private val baseDomainWeatherList =
        listOf(Weather(1, 1, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, "description", "icon"))
    private val baseDomainForecast = Forecast("cityName", 1, baseDomainWeatherList)
    private val baseDataWeatherDbList =
        listOf(WeatherDbModel(1, 1, 1, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, "description", "icon"))
    private val baseDataForecastDb = ForecastDbModel(1, "cityName", 1, baseDataWeatherDbList)


    @Test
    fun fromDataToDomainMappingTest() {
        val domainForecast = baseDataForecastDb.toDomain()
        domainForecast.weatherList = baseDomainWeatherList

        assertEquals(baseDomainForecast, domainForecast)
    }

    @Test
    fun fromDataToDomainDaysOfWeekMappingTest() {
        val domainWeatherList = baseDataForecastDb.toDomain()

        assertEquals(domainWeatherList.weatherList, baseDomainWeatherList)
    }

}