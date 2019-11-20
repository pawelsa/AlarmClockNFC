package com.helpfulapps.alarmclock.views.ringing_alarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.helpfulapps.base.base.BaseViewModel
import com.helpfulapps.base.extensions.rx.backgroundTask
import com.helpfulapps.domain.models.alarm.WeatherAlarm
import com.helpfulapps.domain.use_cases.alarm.GetAlarmUseCase
import com.helpfulapps.domain.use_cases.alarm.SnoozeAlarmUseCase
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

class RingingAlarmViewModel(
    private val getAlarmUseCase: GetAlarmUseCase,
    private val snoozeAlarmUseCase: SnoozeAlarmUseCase
) : BaseViewModel() {
    val TAG = this.javaClass.simpleName

    private val _weatherAlarm: MutableLiveData<WeatherAlarm> = MutableLiveData()
    val weatherAlarm: LiveData<WeatherAlarm>
        get() = _weatherAlarm

    private val _alarmSnoozed: MutableLiveData<Boolean> = MutableLiveData()
    val alarmSnoozed: LiveData<Boolean>
        get() = _alarmSnoozed

    private var alarmId: Long = -1

    fun getAlarm(alarmId: Long) {
        this.alarmId = alarmId
        disposables += getAlarmUseCase(GetAlarmUseCase.Params(alarmId))
            .subscribeBy(
                onSuccess = {
                    _weatherAlarm.value = it
                },
                onError = {
                    it.printStackTrace()
                }
            )
    }

    fun snoozeAlarm() {
        if (alarmId != -1L) {
            disposables += snoozeAlarmUseCase(SnoozeAlarmUseCase.Param(alarmId))
                .backgroundTask()
                .subscribeBy(
                    onComplete = {
                        _alarmSnoozed.value = true
                    },
                    onError = {
                        _alarmSnoozed.value = false
                    }
                )
        }
    }

}