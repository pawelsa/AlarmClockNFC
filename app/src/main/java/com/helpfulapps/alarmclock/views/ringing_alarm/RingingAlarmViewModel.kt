package com.helpfulapps.alarmclock.views.ringing_alarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.helpfulapps.alarmclock.helpers.timeToString
import com.helpfulapps.base.base.BaseViewModel

class RingingAlarmViewModel : BaseViewModel() {

    private val _title: MutableLiveData<String> = MutableLiveData()
    val title: LiveData<String>
        get() = _title

    private val _time: MutableLiveData<String> = MutableLiveData()
    val time: LiveData<String>
        get() = _time

    fun getAlarm() {
        val hour = 15
        val minute = 5
        _time.value = timeToString(hour, minute)
        _title.value = "Customowy tytuł"
    }

}