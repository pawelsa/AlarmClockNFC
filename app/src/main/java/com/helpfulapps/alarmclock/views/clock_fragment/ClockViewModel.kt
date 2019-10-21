package com.helpfulapps.alarmclock.views.clock_fragment

import android.util.Log
import com.helpfulapps.alarmclock.views.ringing_alarm.RingingAlarmActivity
import com.helpfulapps.base.base.BaseViewModel
import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.use_cases.alarm.definition.AddAlarmUseCase
import io.reactivex.rxkotlin.plusAssign


class ClockViewModel(
    private val startAlarm: AddAlarmUseCase
) : BaseViewModel() {

    private val TAG = ClockViewModel::class.java.simpleName

    private lateinit var adapter: ClockListAdapter

    fun setAdapter(adapter: ClockListAdapter) {
        this.adapter = adapter
    }

    fun getAdapter() = adapter

    fun startAlarmMng() {

        disposables += startAlarm(
            AddAlarmUseCase.Params(
                Alarm(
                    hours = 23,
                    minutes = 27,
                    repetitionDays = arrayOf(),
                    ringtoneId = 0
                ), RingingAlarmActivity::class.java, RingingAlarmActivity::class.java
            )
        ).subscribe {
            Log.d(TAG, "Completed")
        }
    }

}