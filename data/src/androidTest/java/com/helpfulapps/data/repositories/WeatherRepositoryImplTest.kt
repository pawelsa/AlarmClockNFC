package com.helpfulapps.data.repositories

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.test.core.app.ApplicationProvider
import com.helpfulapps.data.api.weather.api.ApiCalls
import com.helpfulapps.data.api.weather.api.Downloader
import com.helpfulapps.data.helper.NetworkCheck
import com.helpfulapps.data.helper.Settings
import com.helpfulapps.data.mockData.MockDataIns
import com.helpfulapps.data.repositories.WeatherRepositoryImpl.Companion.ONE_AND_HALF_AN_HOUR
import com.helpfulapps.domain.exceptions.WeatherException
import com.helpfulapps.domain.models.weather.DayWeather
import com.raizlabs.android.dbflow.config.FlowManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class WeatherRepositoryImplTest {

    // TODO write tests

    lateinit var networkCheck: NetworkCheck
    lateinit var settings: Settings
    lateinit var apiCalls: ApiCalls
    lateinit var weatherRepository: WeatherRepositoryImpl
    lateinit var context: Context

    @Before
    fun setUp() {

        val application = ApplicationProvider.getApplicationContext<Application>()
        context = application.applicationContext

        apiCalls = mockk { Downloader.create() }
        settings = Settings(context.getSharedPreferences("Test", MODE_PRIVATE))
        networkCheck = mockk { NetworkCheck(context) }


        weatherRepository =
            WeatherRepositoryImpl(
                context,
                networkCheck,
                apiCalls,
                settings
            )
    }

    @After
    fun destroy() {
        FlowManager.destroy()
    }


    @Test
    fun shouldDownloadAndSaveForecastByCityName() {
        every { networkCheck.isConnectedToNetwork } returns Maybe.create { it.onSuccess(true) }
        every { apiCalls.downloadForecast(any(), any()) } returns Maybe.create {
            it.onSuccess(
                Response.success(
                    MockDataIns.createApiForecastForCity()
                )
            )
        }

        val testObserver = weatherRepository.downloadForecast("Pszczyna")
            .test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertComplete()
            .dispose()
    }

    @Test
    fun shouldDownloadAndSaveForecastByCords() {
        every { networkCheck.isConnectedToNetwork } returns Maybe.create { it.onSuccess(true) }
        every {
            apiCalls.downloadForecastForCoordinates(
                any(),
                any(),
                any(),
                any()
            )
        } returns Maybe.create {
            it.onSuccess(
                Response.success(
                    MockDataIns.createApiForecastForCity()
                )
            )
        }
        val testObserver = weatherRepository.downloadForecast(40, 40)
            .test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertComplete()
            .dispose()
    }

    @Test
    fun shouldDownloadFail() {
/*
        every { networkCheck.isConnectedToNetwork } returns Maybe.just(true)
        every { apiCalls.downloadForecast(any(), any()) } returns Maybe.create { Response.error<ForecastForCity>() }

        weatherRepository.downloadForecast("Pszczyna")
            .test()
            .assertError(CouldNotObtainForecast::class.java)
            .dispose()*/
    }

    @Test
    fun shouldReturnDayWeatherListWithElements() {
        every { weatherRepository.getForecastForAlarms() } returns Single.just(listOf())

        weatherRepository.getForecastForAlarms()
            .test()
            .assertResult(listOf())
            .dispose()
    }

    @Test
    fun shouldReturnDayWeatherListEmpty() {

        val mockedRepo = spyk(weatherRepository)
        every { mockedRepo.getDayWeatherList() } returns Flowable.never()

        val testObserver = mockedRepo.getForecastForAlarms()
            .test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertResult(emptyList())
            .dispose()
    }

    @Test
    fun shouldNotReturnDayWeatherForTimeBefore() {

        val availableData = 1562025600000
        val searchedTime = 1562019600000

        assertFalse(availableData <= searchedTime + ONE_AND_HALF_AN_HOUR && availableData > searchedTime - ONE_AND_HALF_AN_HOUR)
    }

    @Test
    fun shouldNotReturnDayWeatherForTimeAfter() {

        val availableData = 1562025600000
        val searchedTime = 1561945200000

        assertFalse(availableData <= searchedTime + ONE_AND_HALF_AN_HOUR && availableData > searchedTime - ONE_AND_HALF_AN_HOUR)
    }

    @Test
    fun shouldNotReturnDayWeatherForTimeEqual() {

        val availableData = 1562025600000
        val searchedTime = 1562025600000

        assertTrue(availableData <= searchedTime + ONE_AND_HALF_AN_HOUR && availableData > searchedTime - ONE_AND_HALF_AN_HOUR)
    }

    @Test
    fun shouldReturnDayWeatherForTime() {

        val availableData = 1562025600000
        val searchedTime = 1562029200000

        assertTrue(availableData <= searchedTime + ONE_AND_HALF_AN_HOUR && availableData > searchedTime - ONE_AND_HALF_AN_HOUR)
    }

    @Test
    fun shouldNotReturnDayWeatherForTime() {

        val mockedRepo = spyk(weatherRepository)
        every { mockedRepo.getDayWeatherForTime(any()) } returns Single.never()

        val testObserver = mockedRepo.getForecastForAlarm(111)
            .test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertResult(DayWeather(id = -1))
            .dispose()
    }

    @Test
    fun shouldFailWithoutInternet() {

        every { networkCheck.isConnectedToNetwork } returns Maybe.error(WeatherException(""))

        val testObserver = weatherRepository.downloadForecast("Pszczyna")
            .test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertError(WeatherException::class.java)
            .dispose()
    }

}