package com.helpfulapps.alarmclock.views.statistics

import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.snackbar.Snackbar
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.ActivityStatisticsBinding
import com.helpfulapps.base.base.BaseActivity
import com.helpfulapps.base.helpers.observe
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
        bc_statistics_stop_time.setTouchEnabled(false)
        bc_statistics_stop_time.xAxis.let { xAxis ->
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            xAxis.setDrawGridLines(false)
            xAxis.textSize = XAXIS_TEXT_SIZE
            xAxis.textColor = ContextCompat.getColor(baseContext, R.color.textColor)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
        }
        bc_statistics_stop_time.axisLeft.textColor =
            ContextCompat.getColor(baseContext, R.color.textColor)
        bc_statistics_stop_time.axisRight.isEnabled = false
        bc_statistics_stop_time.description.isEnabled = false
        bc_statistics_stop_time.legend.textColor =
            ContextCompat.getColor(baseContext, R.color.textColor)
        bc_statistics_stop_time.legend.textSize = LEGEND_TEXT_SIZE
    }

    private fun setupSnoozeTimeChart() {
        bc_statistics_snoozed.setTouchEnabled(false)
        bc_statistics_snoozed.xAxis.let { xAxis ->
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            xAxis.setDrawGridLines(false)
            xAxis.textColor = ContextCompat.getColor(baseContext, R.color.textColor)
            xAxis.textSize = XAXIS_TEXT_SIZE
            xAxis.position = XAxis.XAxisPosition.BOTTOM
        }
        bc_statistics_snoozed.axisLeft.textColor =
            ContextCompat.getColor(baseContext, R.color.textColor)
        bc_statistics_snoozed.axisRight.isEnabled = false
        bc_statistics_snoozed.description.isEnabled = false
        bc_statistics_snoozed.legend.textColor =
            ContextCompat.getColor(baseContext, R.color.textColor)
        bc_statistics_snoozed.legend.textSize = LEGEND_TEXT_SIZE
    }

    private fun subscribeToStopTime() {
        viewModel.stopTimeADay.observe(this) {

            val dataSet = BarDataSet(it, "Average time to stop alarm (seconds)")
            dataSet.valueTextColor = ContextCompat.getColor(baseContext, R.color.textColor)
            dataSet.valueTextSize = VALUE_TEXT_SIZE
            val barData = BarData(dataSet)

            bc_statistics_stop_time.data = barData
            bc_statistics_stop_time.invalidate()
        }
    }

    private fun subscribeToSnoozesADay() {
        viewModel.snoozesADay.observe(this) {

            val dataSet = BarDataSet(it, "Number of snoozes")
            dataSet.valueTextColor = ContextCompat.getColor(baseContext, R.color.textColor)
            dataSet.valueTextSize = VALUE_TEXT_SIZE
            val barData = BarData(dataSet)

            bc_statistics_snoozed.data = barData
            bc_statistics_snoozed.invalidate()
        }
    }


    override fun showMessage(text: String) {
        Snackbar.make(ll_statistics_base, text, Snackbar.LENGTH_LONG).show()
    }

    companion object {
        private const val LEGEND_TEXT_SIZE = 18f
        private const val VALUE_TEXT_SIZE = 14f
        private const val XAXIS_TEXT_SIZE = 14f
    }

}
