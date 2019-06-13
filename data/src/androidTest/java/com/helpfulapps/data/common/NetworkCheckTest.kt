package com.helpfulapps.data.common

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.helpfulapps.data.api.weather.exceptions.WeatherException
import io.mockk.every
import io.mockk.spyk
import org.junit.Before
import org.junit.Test

class NetworkCheckTest {

    lateinit var networkCheck: NetworkCheck

    @Before
    fun setUp() {
        val application = ApplicationProvider.getApplicationContext<Application>()
        val context = application.applicationContext
        networkCheck = NetworkCheck(context)
    }

    @Test
    fun shouldBeNetworkConnection() {

        val mockedNetworkCheck = spyk(networkCheck)
        every { mockedNetworkCheck.cm.activeNetworkInfo.isConnected } returns true

        mockedNetworkCheck.isConnectedToNetwork
            .test()
            .assertResult(true)
            .dispose()
    }

    @Test
    fun shouldNotBeNetworkConnection() {

        val mockedNetworkCheck = spyk(networkCheck)
        every { mockedNetworkCheck.cm.activeNetworkInfo.isConnected } returns false

        mockedNetworkCheck.isConnectedToNetwork
            .test()
            .assertError(WeatherException::class.java)
            .dispose()
    }

    @Test
    fun shouldNetworkInfoBeNull() {

        val mockedNetworkCheck = spyk(networkCheck)
        every { mockedNetworkCheck.cm.activeNetworkInfo } returns null

        mockedNetworkCheck.isConnectedToNetwork
            .test()
            .assertError(WeatherException::class.java)
            .dispose()
    }

}