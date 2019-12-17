package com.helpfulapps.device.alarms.helpers

/*
class NetworkCheckTest {

    lateinit var networkCheck: NetworkCheck

    @Before
    fun setUp() {
        val application = ApplicationProvider.getApplicationContext<Application>()
        val context = application.applicationContext
        networkCheck = mockk { NetworkCheck(context) }
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

}*/
