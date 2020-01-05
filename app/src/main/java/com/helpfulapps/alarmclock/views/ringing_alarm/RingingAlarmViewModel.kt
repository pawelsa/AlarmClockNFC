package com.helpfulapps.alarmclock.views.ringing_alarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.helpfulapps.base.base.BaseViewModel
import com.helpfulapps.domain.models.alarm.WeatherAlarm
import com.helpfulapps.domain.use_cases.alarm.GetAlarmUseCase
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

class RingingAlarmViewModel(
    private val getAlarmUseCase: GetAlarmUseCase
) : BaseViewModel() {

    private val _weatherAlarm: MutableLiveData<WeatherAlarm> = MutableLiveData()
    val weatherAlarm: LiveData<WeatherAlarm>
        get() = _weatherAlarm

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

}