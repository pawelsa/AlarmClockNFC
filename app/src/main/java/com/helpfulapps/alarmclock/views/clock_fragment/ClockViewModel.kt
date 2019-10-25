package com.helpfulapps.alarmclock.views.clock_fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.helpfulapps.alarmclock.helpers.timeToString
import com.helpfulapps.base.base.BaseViewModel
import com.helpfulapps.domain.use_cases.alarm.GetAlarmsUseCase
import com.helpfulapps.domain.use_cases.alarm.SwitchAlarmUseCase
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy


class ClockViewModel(
    private val getAlarmsUseCase: GetAlarmsUseCase,
    private val switchAlarmUseCase: SwitchAlarmUseCase
) : BaseViewModel() {
    private val TAG = ClockViewModel::class.java.simpleName

    lateinit var adapter: ClockListAdapter

    private val _alarmList: MutableLiveData<List<AlarmData>> = MutableLiveData()
    val alarmList: LiveData<List<AlarmData>>
        get() = _alarmList

    fun getAlarms() {
        disposables += getAlarmsUseCase()
            .map { list ->
                list.map {
                    AlarmData(
                        alarmTime = timeToString(
                            it.alarm.hour,
                            it.alarm.minute
                        ),
                        title = it.alarm.name,
                        isRepeating = it.alarm.isRepeating,
                        isExpanded = it.alarm.isRepeating,
                        weatherIcon = android.R.drawable.ic_notification_clear_all
                    )
                }
            }
            .subscribeBy {
                Log.d(TAG, "Received list of alarms, size : ${it.size}")
                _alarmList.value = it
                adapter.items = it
            }
    }

}