package com.helpfulapps.data.repositories

import com.helpfulapps.data.api.PrepareData
import com.helpfulapps.data.db.weather.dao.WeatherDao
import com.helpfulapps.data.mockData.MockData
import com.helpfulapps.data.repositories.WeatherRepositoryImpl.Companion.ONE_AND_HALF_AN_HOUR
import com.helpfulapps.domain.exceptions.WeatherException
import com.helpfulapps.domain.helpers.singleOf
import com.helpfulapps.domain.models.weather.DayWeather
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class WeatherRepositoryImplTest {

    private val prepareData: PrepareData = mockk {}
    private val weatherDao: WeatherDao = mockk {}
    private val weatherRepository = WeatherRepositoryImpl(prepareData, weatherDao)


    @Nested
    inner class DownloadForecast {

        @Test
        fun `should download by name`() {
            every { prepareData.downloadForecast(any()) } returns singleOf {
                listOf(
                    MockData.createDataDayWeather(
                        cityName = "Pszczyna"
                    )
                )
            }
            every { weatherDao.insert(any()) } returns singleOf { true }
            every { weatherDao.clearWeatherTables() } returns Unit

            weatherRepository.downloadForecast("Pszczyna")
                .test()
                .assertComplete()
                .dispose()
        }

        @Test
        fun `should download by coords`() {
            every {
                prepareData.downloadForecastForCoordinates(
                    any(),
                    any()
                )
            } returns singleOf { listOf(MockData.createDataDayWeather(cityName = "Pszczyna")) }
            every { weatherDao.insert(any()) } returns singleOf { true }
            every { weatherDao.clearWeatherTables() } returns Unit

            weatherRepository.downloadForecast(40.0, 40.0)
                .test()
                .assertComplete()
                .dispose()
        }

        @Test
        fun `should download fail`() {

            every { prepareData.downloadForecast(any()) } returns Single.error(WeatherException("fsd"))

            weatherRepository.downloadForecast("Pszczyna")
                .test()
                .assertError(WeatherException::class.java)
                .dispose()
            verify(exactly = 0) { weatherDao.insert(any()) }
            verify(exactly = 0) { weatherDao.clearWeatherTables() }
        }

        @Test
        fun `should saving fail`() {
            every { prepareData.downloadForecast(any()) } returns singleOf {
                listOf(
                    MockData.createDataDayWeather(
                        cityName = "Pszczyna"
                    )
                )
            }
            every { weatherDao.insert(any()) } returns singleOf { false }
            every { weatherDao.clearWeatherTables() } returns Unit

            weatherRepository.downloadForecast("Pszczyna")
                .test()
                .assertError(WeatherException::class.java)
                .dispose()
        }

        @Test
        fun `should clearing tables fail`() {
            every { prepareData.downloadForecast(any()) } returns singleOf {
                listOf(
                    MockData.createDataDayWeather(
                        cityName = "Pszczyna"
                    )
                )
            }
            every { weatherDao.clearWeatherTables() } throws Exception()

            weatherRepository.downloadForecast("Pszczyna")
                .test()
                .assertError(Exception::class.java)
                .dispose()

            verify(exactly = 0) { weatherDao.insert(any()) }
        }

    }

    @Nested
    inner class GetForecastsForAlarm {

        @Test
        fun `should return weather alarms`() {
            every { weatherDao.streamWeatherList() } returns Flowable.fromIterable(MockData.weatherDataList)

            weatherRepository.getForecastForAlarms()
                .test()
                .assertResult(MockData.weatherDomainList)
                .dispose()
        }

        @Test
        fun `should return empty list`() {
            every { weatherDao.streamWeatherList() } returns Flowable.fromIterable(emptyList())

            weatherRepository.getForecastForAlarms()
                .test()
                .assertResult(emptyList())
                .dispose()
        }

        @Test
        fun `should return empty list when flowable never emits`() {
            every { weatherDao.streamWeatherList() } returns Flowable.never()

            val testObserver = weatherRepository.getForecastForAlarms()
                .test()

            testObserver.awaitTerminalEvent()

            testObserver
                .assertResult(listOf())
                .dispose()
        }

    }

    @Nested
    inner class GetForecastForAlarmAtTime {

        @Test
        fun `should return weather`() {
            every { weatherDao.getWeatherForTime(any()) } returns singleOf { MockData.createDataDayWeather() }

            weatherRepository.getForecastForAlarm(1)
                .test()
                .assertResult(MockData.dayWeather)
                .dispose()
        }

        @Test
        fun `should not find weather`() {
            every { weatherDao.getWeatherForTime(any()) } returns Single.never()

            val testObserver = weatherRepository.getForecastForAlarm(1)
                .test()

            testObserver.awaitTerminalEvent()

            testObserver
                .assertResult(DayWeather(id = -1))
                .dispose()
        }

        @Test
        fun `should return default weather on error`() {
            every { weatherDao.getWeatherForTime(any()) } returns Single.error(
                NoSuchElementException()
            )

            weatherRepository.getForecastForAlarm(1)
                .test()
                .assertResult(DayWeather())
                .dispose()
        }

    }

    @Nested
    inner class SortingWeatherInfo {
        @Test
        fun `should not return day weather for time before`() {

            val availableData = 1562025600000
            val searchedTime = 1562019600000

            assertFalse(availableData <= searchedTime + ONE_AND_HALF_AN_HOUR && availableData > searchedTime - ONE_AND_HALF_AN_HOUR)
        }

        @Test
        fun `should not return day weather for time after`() {

            val availableData = 1562025600000
            val searchedTime = 1561945200000

            assertFalse(availableData <= searchedTime + ONE_AND_HALF_AN_HOUR && availableData > searchedTime - ONE_AND_HALF_AN_HOUR)
        }

        @Test
        fun `should not return day weather for time equal`() {

            val availableData = 1562025600000
            val searchedTime = 1562025600000

            assertTrue(availableData <= searchedTime + ONE_AND_HALF_AN_HOUR && availableData > searchedTime - ONE_AND_HALF_AN_HOUR)
        }

        @Test
        fun `should return day weather for time`() {

            val availableData = 1562025600000
            val searchedTime = 1562029200000

            assertTrue(availableData <= searchedTime + ONE_AND_HALF_AN_HOUR && availableData > searchedTime - ONE_AND_HALF_AN_HOUR)
        }
    }

}