package com.helpfulapps.alarmclock.views.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.BarEntry
import com.helpfulapps.base.base.BaseViewModel
import com.helpfulapps.base.extensions.rx.backgroundTask
import com.helpfulapps.domain.use_cases.stats.GetAllStatsUseCase
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

class StatisticsViewModel(
    private val getAllStatsUseCase: GetAllStatsUseCase
) : BaseViewModel() {


    private val _snoozesADay: MutableLiveData<List<BarEntry>> = MutableLiveData()
    val snoozesADay: LiveData<List<BarEntry>>
        get() = _snoozesADay

    private val _stopTimeADay: MutableLiveData<List<BarEntry>> = MutableLiveData()
    val stopTimeADay: LiveData<List<BarEntry>>
        get() = _stopTimeADay


    fun getAllStats() {
        disposables += getAllStatsUseCase()
            .backgroundTask()
            .subscribeBy {
                _snoozesADay.value = it.averageSnoozesByDay.mapIndexed { index, entry ->
                    BarEntry(
                        index.toFloat(),
                        entry.toFloat()
                    )
                }
                _stopTimeADay.value = it.averageStopTimeByDay.mapIndexed { index, entry ->
                    BarEntry(
                        index.toFloat(),
                        entry
                    )
                }
            }
    }

}