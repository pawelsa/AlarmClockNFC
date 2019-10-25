package com.helpfulapps.alarmclock.views.clock_fragment

import android.util.Log
import com.helpfulapps.base.base.BaseViewModel
import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.use_cases.alarm.AddAlarmUseCase
import com.helpfulapps.domain.use_cases.alarm.SwitchAlarmUseCase
import io.reactivex.rxkotlin.plusAssign


class ClockViewModel(
    private val startAlarm: AddAlarmUseCase,
    private val switchAlarmUseCase: SwitchAlarmUseCase
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
                    id = 5,
                    hour = 19,
                    minute = 34,
                    repetitionDays = arrayOf(),
                    ringtoneUrl = ""
                )
            )
        ).subscribe {
            Log.d(TAG, "Completed")
        }
    }

    fun stopAlarm() {
        disposables += switchAlarmUseCase(
            SwitchAlarmUseCase.Params(
                5
            )
        ).subscribe {
            Log.d(TAG, "Alarm should be stopped")
        }
    }

}