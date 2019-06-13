package com.helpfulapps.data.repositories

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.test.core.app.ApplicationProvider
import com.helpfulapps.data.common.Settings
import com.helpfulapps.domain.repository.WeatherRepository
import com.raizlabs.android.dbflow.config.FlowManager
import org.junit.After
import org.junit.Before
import org.junit.Test

class WeatherRepositoryImplTest {

    // TODO write tests

    lateinit var weatherRepository: WeatherRepository
    lateinit var context: Context

    @Before
    fun setUp() {

        val application = ApplicationProvider.getApplicationContext<Application>()
        context = application.applicationContext
        weatherRepository =
            WeatherRepositoryImpl(
                Settings(context.getSharedPreferences("Test", MODE_PRIVATE)),
                application.applicationContext
            )
    }

    @After
    fun destroy() {
        FlowManager.destroy()
    }


    @Test
    fun shouldDownloadAndSaveForecastByCityName() {
        val testObserver = weatherRepository.downloadForecast("Pszczyna")
            .test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertComplete()
            .dispose()
    }

    @Test
    fun shouldDownloadAndSaveForecastByCords() {
        val testObserver = weatherRepository.downloadForecast(40, 40)
            .test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertComplete()
            .dispose()
    }

    @Test
    fun shouldDownloadAndFailToSaveForecast() {

    }

    @Test
    fun shouldDownloadFail() {

    }

    @Test
    fun shouldReturnForecastListWithElements() {

    }

    @Test
    fun shouldReturnForecastListEmpty() {

    }

    @Test
    fun shouldReturnForecastForTime() {

    }

    @Test
    fun shouldNotReturnForecastForTime() {

    }


}