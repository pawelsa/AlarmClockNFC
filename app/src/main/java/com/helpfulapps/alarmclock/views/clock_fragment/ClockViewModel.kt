package com.helpfulapps.alarmclock.views.clock_fragment

import android.util.Log
import com.helpfulapps.alarmclock.helpers.timeToString
import com.helpfulapps.base.base.BaseViewModel
import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.use_cases.alarm.GetAlarmsUseCase
import com.helpfulapps.domain.use_cases.alarm.RemoveAlarmUseCase
import com.helpfulapps.domain.use_cases.alarm.SwitchAlarmUseCase
import com.helpfulapps.domain.use_cases.alarm.UpdateAlarmUseCase
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy


class ClockViewModel(
    private val getAlarmsUseCase: GetAlarmsUseCase,
    private val switchAlarmUseCase: SwitchAlarmUseCase,
    private val updateAlarmUseCase: UpdateAlarmUseCase,
    private val removeAlarmUseCase: RemoveAlarmUseCase
) : BaseViewModel() {
    private val TAG = ClockViewModel::class.java.simpleName

    lateinit var adapter: NewClockListAdapter

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
                        weatherIcon = android.R.drawable.ic_notification_clear_all,
                        isTurnedOn = it.alarm.isTurnedOn,
                        ringtoneTitle = it.alarm.ringtoneTitle,
                        hour = it.alarm.hour,
                        minute = it.alarm.minute,
                        repetitionDays = it.alarm.repetitionDays,
                        ringtoneUrl = it.alarm.ringtoneUrl
                    )
                }
            }
            .subscribeBy {
                adapter.submitList(it)
            }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "OnCleared")
    }

    // TODO this should inform view about successful change
    fun switchAlarm(alarm: Alarm) {
        disposables += switchAlarmUseCase(SwitchAlarmUseCase.Params(alarm.id))
            .subscribe({
                Log.d(TAG, "alarm switched")
                getAlarms()
            }, {
                Log.e(TAG, it.message ?: "")
                it.printStackTrace()
            })
    }

    // TODO this should inform view about successful change
    fun updateAlarm(alarm: Alarm) {
        /*disposables += updateAlarmUseCase(UpdateAlarmUseCase.Params(alarm))
            .subscribe { Log.d(TAG, "successfully updated alarm")
            getAlarms()}*/
    }

    // TODO this should inform view about successful change
    fun removeAlarm(alarm: Alarm) {
        /*disposables += removeAlarmUseCase(RemoveAlarmUseCase.Params(alarm.id))
            .subscribe {
                Log.d(TAG, "removed alarm successfully")
                getAlarms()
            }*/
    }


}