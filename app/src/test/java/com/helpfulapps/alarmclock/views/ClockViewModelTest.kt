package com.helpfulapps.alarmclock.views

import com.helpfulapps.alarmclock.base.BaseViewModelTest
import com.helpfulapps.alarmclock.mock_data.MockData
import com.helpfulapps.alarmclock.views.clock_fragment.AlarmData
import com.helpfulapps.alarmclock.views.clock_fragment.ClockViewModel
import com.helpfulapps.domain.eventBus.DatabaseNotifiers
import com.helpfulapps.domain.eventBus.RxBus
import com.helpfulapps.domain.helpers.Settings
import com.helpfulapps.domain.helpers.singleOf
import com.helpfulapps.domain.models.alarm.WeatherAlarm
import com.helpfulapps.domain.use_cases.alarm.GetAlarmsUseCase
import com.helpfulapps.domain.use_cases.alarm.RemoveAlarmUseCase
import com.helpfulapps.domain.use_cases.alarm.SwitchAlarmUseCase
import com.jraska.livedata.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ClockViewModelTest : BaseViewModelTest<ClockViewModel>() {

    private val getAlarmsUseCase: GetAlarmsUseCase = mockk {}
    private val switchAlarmUseCase: SwitchAlarmUseCase = mockk {}
    private val removeAlarmUseCase: RemoveAlarmUseCase = mockk {}
    private val settings: Settings = mockk<Settings> {}.also {
        every { it.askForBatteryOptimization } returns false
    }

    override var viewModel: ClockViewModel =
        ClockViewModel(getAlarmsUseCase, switchAlarmUseCase, removeAlarmUseCase, settings)

    @Nested
    inner class GetAlarms {

        @Test
        fun `should obtain empty list`() {
            every { getAlarmsUseCase() } returns singleOf { emptyList<WeatherAlarm>() }

            viewModel.getAlarms()

            viewModel.alarmList
                .test()
                .assertValue(emptyList())
        }

        @Test
        fun `should obtain exception`() {
            every { getAlarmsUseCase() } returns Single.error(Exception())

            viewModel.getAlarms()

            viewModel.alarmList
                .test()
                .assertNoValue()
        }

        @Test
        fun `should obtain data list`() {
            every { getAlarmsUseCase() } returns singleOf { MockData.pairs }

            viewModel.getAlarms()

            viewModel.alarmList
                .test()
                .assertValue(MockData.pairs.map { AlarmData(it) }
                    // last one is changed, to make it true it has to be changed
                    .also { list ->
                        list[list.size - 1].let {
                            it.toChange = !it.toChange
                        }
                    })
        }

    }

    @Nested
    inner class SwitchAlarm {

        @Test
        fun `should switch alarm successfully`() {
            every { switchAlarmUseCase(any()) } returns Completable.complete()
            every { getAlarmsUseCase() } returns singleOf { emptyList<WeatherAlarm>() }

            val spyViewModel = spyk(viewModel)

            spyViewModel.switchAlarm(MockData.defaultAlarm)
            spyViewModel.subscribeToDatabaseChanges()
            RxBus.publish(DatabaseNotifiers.Updated)

            verify(exactly = 1) { spyViewModel.getAlarms() }
        }

        @Test
        fun `should switch alarm fail`() {
            every { switchAlarmUseCase(any()) } returns Completable.error(Exception())

            val spyViewModel = spyk(viewModel)

            spyViewModel.switchAlarm(MockData.defaultAlarm)
            spyViewModel.subscribeToDatabaseChanges()

            verify(exactly = 0) { spyViewModel.getAlarms() }
        }

    }

    @Nested
    inner class RemoveAlarm {

        @Test
        fun `should remove alarm successfully`() {
            every { removeAlarmUseCase(any()) } returns Completable.complete()
            every { getAlarmsUseCase() } returns singleOf { emptyList<WeatherAlarm>() }

            val spyViewModel = spyk(viewModel)

            spyViewModel.removeAlarm(MockData.defaultAlarm)
            spyViewModel.subscribeToDatabaseChanges()
            RxBus.publish(DatabaseNotifiers.Removed)

            verify(exactly = 1) { spyViewModel.getAlarms() }
        }

        @Test
        fun `should remove alarm fail`() {
            every { removeAlarmUseCase(any()) } returns Completable.error(Exception())

            val spyViewModel = spyk(viewModel)

            spyViewModel.removeAlarm(MockData.defaultAlarm)
            spyViewModel.subscribeToDatabaseChanges()

            verify(exactly = 0) { spyViewModel.getAlarms() }
        }

    }

    @Nested
    inner class ListenDatabaseChanges {

        @Test
        fun `should receive successful save`() {
            every { getAlarmsUseCase() } returns singleOf { emptyList<WeatherAlarm>() }

            val spyViewModel = spyk(viewModel)

            spyViewModel.subscribeToDatabaseChanges()
            RxBus.publish(DatabaseNotifiers.Saved)

            verify(exactly = 1) { spyViewModel.getAlarms() }
        }

        @Test
        fun `should receive successful remove`() {
            every { getAlarmsUseCase() } returns singleOf { emptyList<WeatherAlarm>() }

            val spyViewModel = spyk(viewModel)

            spyViewModel.subscribeToDatabaseChanges()
            RxBus.publish(DatabaseNotifiers.Removed)

            verify(exactly = 1) { spyViewModel.getAlarms() }
        }

        @Test
        fun `should receive successful update`() {
            every { getAlarmsUseCase() } returns singleOf { emptyList<WeatherAlarm>() }

            val spyViewModel = spyk(viewModel)

            spyViewModel.subscribeToDatabaseChanges()
            RxBus.publish(DatabaseNotifiers.Updated)

            verify(exactly = 1) { spyViewModel.getAlarms() }
        }

    }

}