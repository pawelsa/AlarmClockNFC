package com.helpfulapps.alarmclock.views.statistics

import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.snackbar.Snackbar
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.ActivityStatisticsBinding
import com.helpfulapps.alarmclock.helpers.extensions.observe
import com.helpfulapps.base.base.BaseActivity
import kotlinx.android.synthetic.main.activity_statistics.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.DateFormatSymbols

class StatisticsActivity : BaseActivity<StatisticsViewModel, ActivityStatisticsBinding>() {

    override val layoutId: Int = R.layout.activity_statistics
    override val viewModel: StatisticsViewModel by viewModel()

    private val labels: List<String> by lazy {
        val formatSymbols = DateFormatSymbols.getInstance()
        (1..7).map { day -> formatSymbols.shortWeekdays[day].capitalize() }
    }

    override fun init() {
        viewModel.getAllStats()

        setupStopTimeChart()
        setupSnoozeTimeChart()
        subscribeToSnoozesADay()
        subscribeToStopTime()
    }

    private fun setupStopTimeChart() {
        bc_statistics_stop_time.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        bc_statistics_stop_time.setTouchEnabled(false)
        bc_statistics_stop_time.xAxis.setDrawGridLines(false)
        bc_statistics_stop_time.xAxis.position = XAxis.XAxisPosition.BOTTOM
        bc_statistics_stop_time.description.isEnabled = false
    }

    private fun setupSnoozeTimeChart() {
        bc_statistics_snoozed.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        bc_statistics_snoozed.setTouchEnabled(false)
        bc_statistics_snoozed.xAxis.setDrawGridLines(false)
        bc_statistics_snoozed.xAxis.position = XAxis.XAxisPosition.BOTTOM
        bc_statistics_snoozed.description.isEnabled = false
    }

    private fun subscribeToStopTime() {
        viewModel.stopTimeADay.observe(this) {

            val dataSet = BarDataSet(it, "Average time to stop alarm (seconds)")
            val barData = BarData(dataSet)

            bc_statistics_stop_time.data = barData
            bc_statistics_stop_time.invalidate()
        }
    }

    private fun subscribeToSnoozesADay() {
        viewModel.snoozesADay.observe(this) {

            val dataSet = BarDataSet(it, "Number of snoozes")
            val barData = BarData(dataSet)

            bc_statistics_snoozed.data = barData
            bc_statistics_snoozed.invalidate()
        }
    }


    override fun showMessage(text: String) {
        Snackbar.make(ll_statistics_base, text, Snackbar.LENGTH_LONG).show()
    }

}
