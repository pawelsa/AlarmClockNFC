package com.helpfulapps.alarmclock.views

import com.github.mikephil.charting.data.BarEntry
import com.helpfulapps.alarmclock.base.BaseViewModelTest
import com.helpfulapps.alarmclock.test_extensions.contentEqual
import com.helpfulapps.alarmclock.views.statistics.StatisticsViewModel
import com.helpfulapps.domain.helpers.singleOf
import com.helpfulapps.domain.models.stats.AnalysedAlarmStats
import com.helpfulapps.domain.use_cases.stats.GetAllStatsUseCase
import com.jraska.livedata.test
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

class StatisticsViewModelTest : BaseViewModelTest<StatisticsViewModel>() {

    private val _getStatisticsUseCase: GetAllStatsUseCase = mockk {}
    override var viewModel = StatisticsViewModel(_getStatisticsUseCase)

    @Test
    fun `should obtain empty data`() {
        every { _getStatisticsUseCase() } returns singleOf {
            AnalysedAlarmStats(
                Array(7) { 0f },
                Array(7) { 0 })
        }

        viewModel.getAllStats()

        viewModel.snoozesADay
            .test()
            .contentEqual(List(7) { index -> BarEntry(index.toFloat(), 0f) }) { e1, e2 ->
                e1.equalTo(e2)
            }
            .assertValue(true)

        viewModel.stopTimeADay
            .test()
            .contentEqual(List(7) { index -> BarEntry(index.toFloat(), 0f) }) { e1, e2 ->
                e1.equalTo(e2)
            }
            .assertValue(true)

    }

    @Test
    fun `should obtain some data`() {
        every { _getStatisticsUseCase() } returns singleOf {
            AnalysedAlarmStats(
                Array(7) { 0f }.also { it[2] = 15f },
                Array(7) { 0 }.also { it[2] = 1 })
        }

        viewModel.getAllStats()

        viewModel.snoozesADay
            .test()
            .contentEqual(List(7) { index ->
                BarEntry(
                    index.toFloat(),
                    if (index == 2) 1f else 0f
                )
            }) { e1, e2 ->
                e1.equalTo(e2)
            }
            .assertValue(true)

        viewModel.stopTimeADay
            .test()
            .contentEqual(List(7) { index ->
                BarEntry(
                    index.toFloat(),
                    if (index == 2) 15f else 0f
                )
            }) { e1, e2 ->
                e1.equalTo(e2)
            }
            .assertValue(true)
    }

}