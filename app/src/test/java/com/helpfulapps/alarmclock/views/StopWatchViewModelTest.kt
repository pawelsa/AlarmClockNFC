package com.helpfulapps.alarmclock.views

import com.helpfulapps.alarmclock.BaseViewModelTest
import com.helpfulapps.alarmclock.service.StopwatchService
import com.helpfulapps.alarmclock.views.stopwatch_fragment.StopWatchViewModel
import com.helpfulapps.domain.eventBus.ServiceBus
import com.jraska.livedata.test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class StopWatchViewModelTest : BaseViewModelTest<StopWatchViewModel>() {

    override var viewModel = StopWatchViewModel()

    @BeforeEach
    fun setUp() {
        viewModel = StopWatchViewModel()
        viewModel.observeStopwatch()
    }

    @Test
    fun `should start, pause and restart`() {


        val testObserver = viewModel.stopwatchState
            .test()


        ServiceBus.publish(StopwatchService.StopWatchEvent.Start)
        ServiceBus.publish(StopwatchService.StopWatchEvent.Paused)
        ServiceBus.publish(StopwatchService.StopWatchEvent.Resume)

        testObserver
            .assertValueHistory(
                StopWatchViewModel.StopWatchState.Started,
                StopWatchViewModel.StopWatchState.Paused,
                StopWatchViewModel.StopWatchState.Resumed
            )

    }

    @Test
    fun `should start`() {

        ServiceBus.publish(StopwatchService.StopWatchEvent.Start)

        viewModel.stopwatchState
            .test()
            .assertValue(StopWatchViewModel.StopWatchState.Started)
    }

    @Test
    fun `should update time`() {

        ServiceBus.publish(StopwatchService.StopWatchEvent.Update(15))

        viewModel.currentTime
            .test()
            .assertValue(15)
    }

    @Test
    fun `should pause stopwatch`() {

        ServiceBus.publish(StopwatchService.StopWatchEvent.Paused)

        viewModel.stopwatchState
            .test()
            .assertValue(StopWatchViewModel.StopWatchState.Paused)
    }

    @Test
    fun `should resume stopwatch`() {

        ServiceBus.publish(StopwatchService.StopWatchEvent.Resume)

        viewModel.stopwatchState
            .test()
            .assertValue(StopWatchViewModel.StopWatchState.Resumed)
    }

    @Test
    fun `should take a lap`() {

        ServiceBus.publish(StopwatchService.StopWatchEvent.Lap(listOf(15, 30)))

        viewModel.lapTimes
            .test()
            .assertValue(listOf(15L, 30L))
    }

    @Test
    fun `should stop stopwatch`() {

        ServiceBus.publish(StopwatchService.StopWatchEvent.Stop)

        viewModel.stopwatchState
            .test()
            .assertValue(StopWatchViewModel.StopWatchState.Stopped)
        viewModel.lapTimes
            .test()
            .assertValue(listOf())
        viewModel.currentTime
            .test()
            .assertValue(0)
    }

}