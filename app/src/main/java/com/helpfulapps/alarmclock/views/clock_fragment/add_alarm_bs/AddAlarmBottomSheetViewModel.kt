package com.helpfulapps.alarmclock.views.clock_fragment.add_alarm_bs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.helpfulapps.base.base.BaseViewModel
import com.helpfulapps.domain.use_cases.alarm.definition.AddAlarmUseCase

class AddAlarmBottomSheetViewModel(private val _addAlarmUseCase: AddAlarmUseCase) :
    BaseViewModel() {

    private val _alarmTitle: MutableLiveData<String> = MutableLiveData()
    val alarmTitle: LiveData<String>
        get() = _alarmTitle

    private val _alarmTime: MutableLiveData<String> = MutableLiveData()
    val alarmTime: LiveData<String>
        get() = _alarmTime

    private val _alarmSaved = MutableLiveData<Boolean>()
    val alarmSaved: LiveData<Boolean>
        get() = _alarmSaved

    val newAlarm = NewAlarmModel()


    fun saveAlarm() {
        /*
          disposables += _addAlarmUseCase(AddAlarmUseCase.Params(alarm))
              .backgroundTask()
              .subscribeBy {
                  _alarmSaved.value = true
              }
  */
    }

}