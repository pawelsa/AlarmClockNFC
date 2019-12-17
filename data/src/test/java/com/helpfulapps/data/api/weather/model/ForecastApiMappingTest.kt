package com.helpfulapps.data.api.weather.model

import com.helpfulapps.data.mockData.MockData
import org.junit.Assert.assertEquals
import org.junit.Test

class ForecastApiMappingTest {

    @Test
    fun `map forecast for city to db forecast`() {

        val apiForecastForCity = MockData.createApiForecastForCity()
        val dayWeather = apiForecastForCity.toDbModel()

        val expected = MockData.createDbDayWeather()
        expected.weatherInfoEntity = null
        expected.hourWeatherEntityList.forEach { it.weatherInfoEntity = null }

        assertEquals(expected, dayWeather)
    }

}