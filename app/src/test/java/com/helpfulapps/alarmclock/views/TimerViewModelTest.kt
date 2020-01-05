package com.helpfulapps.alarmclock.views

import com.helpfulapps.alarmclock.base.BaseViewModelTest
import com.helpfulapps.alarmclock.service.TimerService
import com.helpfulapps.alarmclock.views.timer_fragment.TimerViewModel
import com.helpfulapps.domain.eventBus.ServiceBus
import com.helpfulapps.domain.helpers.Settings
import com.jraska.livedata.test
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TimerViewModelTest : BaseViewModelTest<TimerViewModel>() {

    private val settings: Settings = mockk<Settings> {}.also {
        every { it.timeLeft } returns -1L
    }

    override var viewModel =
        TimerViewModel(settings)

    @BeforeEach
    fun setup() {
        viewModel =
            TimerViewModel(
                settings
            )
        viewModel.listenToTimer()
    }

    @Test
    fun `should start timer`() {

        ServiceBus.publish(TimerService.TimerServiceEvent.StartTimer)

        viewModel.timerStates
            .test()
            .assertValue(
                TimerViewModel.TimerState.Start(
                    -1
                )
            )
    }

    @Test
    fun `should pause timer`() {

        ServiceBus.publish(TimerService.TimerServiceEvent.PauseTimer)

        viewModel.timerStates
            .test()
            .assertValue(TimerViewModel.TimerState.Paused)
    }

    @Test
    fun `timer is up`() {

        ServiceBus.publish(TimerService.TimerServiceEvent.TimeIsUpTimer)

        viewModel.timerStates
            .test()
            .assertValue(TimerViewModel.TimerState.TimeIsUp)
    }

    @Test
    fun `timer is reset`() {

        ServiceBus.publish(TimerService.TimerServiceEvent.RestartTimer)

        viewModel.timerStates
            .test()
            .assertValue(TimerViewModel.TimerState.Restart)
    }

    @Test
    fun `timer is finished`() {
        every { settings.timeLeft } returns -1
        ServiceBus.publish(TimerService.TimerServiceEvent.FinishTimer)

        viewModel.timerStates
            .test()
            .assertValue(
                TimerViewModel.TimerState.Finished(
                    -1
                )
            )
    }

    @Test
    fun `timer is updated`() {

        ServiceBus.publish(TimerService.TimerServiceEvent.UpdateTimer(15))

        viewModel.timerStates
            .test()
            .assertValue(
                TimerViewModel.TimerState.Update(
                    15
                )
            )
    }

}