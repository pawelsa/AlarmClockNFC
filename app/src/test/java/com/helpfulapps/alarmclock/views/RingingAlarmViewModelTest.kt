package com.helpfulapps.alarmclock.views

import com.helpfulapps.alarmclock.base.BaseViewModelTest
import com.helpfulapps.alarmclock.mock_data.MockData
import com.helpfulapps.alarmclock.views.ringing_alarm.RingingAlarmViewModel
import com.helpfulapps.domain.helpers.singleOf
import com.helpfulapps.domain.use_cases.alarm.GetAlarmUseCase
import com.jraska.livedata.test
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.jupiter.api.Test

class RingingAlarmViewModelTest : BaseViewModelTest<RingingAlarmViewModel>() {

    private val _getAlarmUseCase: GetAlarmUseCase = mockk {}
    override val viewModel: RingingAlarmViewModel = RingingAlarmViewModel(_getAlarmUseCase)

    @Test
    fun `should obtain alarm`() {
        every { _getAlarmUseCase(any()) } returns singleOf { MockData.pairs[0] }

        viewModel.getAlarm(5)

        viewModel.weatherAlarm
            .test()
            .assertValue(MockData.pairs[0])
    }

    @Test
    fun `should not obtain alarm`() {
        every { _getAlarmUseCase(any()) } returns Single.error(Exception())

        viewModel.getAlarm(5)

        viewModel.weatherAlarm
            .test()
            .assertNoValue()
    }

}