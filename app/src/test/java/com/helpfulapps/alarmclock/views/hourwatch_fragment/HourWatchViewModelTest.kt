package com.helpfulapps.alarmclock.views.hourwatch_fragment

import com.helpfulapps.alarmclock.BaseViewModelTest
import com.helpfulapps.alarmclock.service.TimerService
import com.helpfulapps.domain.eventBus.ServiceBus
import com.helpfulapps.domain.helpers.Settings
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class HourWatchViewModelTest : BaseViewModelTest<HourWatchViewModel>() {
    private val settings: Settings = mockk<Settings> {}.also {
        every { it.timeLeft } returns -1L
    }

    override val viewModel = HourWatchViewModel(settings)


    @Test
    fun `should start timer`() {

    }

    @Test
    fun `should stop timer`() {

    }

    @Test
    fun `timer is up`() {

        viewModel.listenToTimer()
        ServiceBus.publish(TimerService.TimerServiceEvent.TimeIsUpTimer)
/*
        assertFalse(viewModel.isFinished.value ?: true)
        assertFalse(viewModel.isRunning.value ?: true)
        assertFalse(viewModel.isPaused.value ?: true)*/
    }

    @Test
    fun `timer is reset`() {

    }

    @Test
    fun `timer is finished`() {

    }

}