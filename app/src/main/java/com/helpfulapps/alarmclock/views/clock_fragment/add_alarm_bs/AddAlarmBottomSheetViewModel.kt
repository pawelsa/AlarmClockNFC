package com.helpfulapps.alarmclock.views.clock_fragment.add_alarm_bs

import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.helpfulapps.alarmclock.helpers.getDefaultRingtone
import com.helpfulapps.alarmclock.helpers.timeToString
import com.helpfulapps.base.base.BaseViewModel
import com.helpfulapps.base.extensions.rx.backgroundTask
import com.helpfulapps.base.extensions.rx.singleOf
import com.helpfulapps.domain.use_cases.alarm.definition.AddAlarmUseCase
import io.reactivex.rxkotlin.plusAssign

class AddAlarmBottomSheetViewModel(private val _addAlarmUseCase: AddAlarmUseCase) :
    BaseViewModel() {

    private val _alarmTitle: MutableLiveData<String> = MutableLiveData()
    val alarmTitle: LiveData<String>
        get() = _alarmTitle

    private val _alarmTime: MutableLiveData<String> = MutableLiveData()
    val alarmTime: LiveData<String>
        get() = _alarmTime

    val vibrating = ObservableBoolean(true)
    val repeating = ObservableBoolean(false)

    var time: Pair<Int, Int> = Pair(8, 30)
        set(value) {
            field = value
            _alarmTime.value = timeToString(value)
        }
    var alarm: Pair<String, String> = Pair("", "")
        set(value) {
            field = value
            _alarmTitle.value = value.first
        }
    var repeatingDays: Array<Boolean> = Array(7) { false }

    private val _alarmSaved = MutableLiveData<Boolean>()
    val alarmSaved: LiveData<Boolean>
        get() = _alarmSaved


    fun saveAlarm() {
        /*
          disposables += _addAlarmUseCase(AddAlarmUseCase.Params(alarm))
              .backgroundTask()
              .subscribeBy {
                  _alarmSaved.value = true
              }
  */
    }

    fun getDefaultAlarmTitle(context: Context) {
        disposables += singleOf {
            getDefaultRingtone(context)
        }
            .backgroundTask()
            .subscribe { title ->
                _alarmTitle.value = title.first
            }
    }

    fun getAlarm() {
        disposables += singleOf {
            8 to 30
        }
            .backgroundTask()
            .subscribe { timePair ->
                time = timePair
                _alarmTime.value = timeToString(time)
            }
    }

}