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
import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.use_cases.alarm.AddAlarmUseCase
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

class AddAlarmBottomSheetViewModel(private val _addAlarmUseCase: AddAlarmUseCase) :
    BaseViewModel() {

    private val _alarmTime: MutableLiveData<String> = MutableLiveData()
    val alarmTime: LiveData<String>
        get() = _alarmTime

    val vibrating = ObservableBoolean(true)

    var time: Pair<Int, Int> = Pair(8, 30)
        set(value) {
            field = value
            _alarmTime.value = timeToString(value)
        }

    private val _ringtoneTitle: MutableLiveData<String> = MutableLiveData()
    val ringtoneTitle: LiveData<String>
        get() = _ringtoneTitle

    var ringtone: Pair<String, String> = Pair("", "")
        set(value) {
            field = value
            _ringtoneTitle.value = value.first
        }

    val repeating = ObservableBoolean(false)
    var repeatingDays: Array<Boolean> = Array(7) { false }

    private val _alarmSaved = MutableLiveData<Boolean>()
    val alarmSaved: LiveData<Boolean>
        get() = _alarmSaved

    val alarm: Alarm
        get() = Alarm(
            isRepeating = repeating.get(),
            ringtoneUrl = ringtone.second,
            isVibrationOn = vibrating.get(),
            repetitionDays = repeatingDays,
            hour = time.first,
            minute = time.second
        )


    fun saveAlarm() {
        disposables += _addAlarmUseCase(AddAlarmUseCase.Params(alarm))
            .backgroundTask()
            .subscribeBy(
                onComplete = { _alarmSaved.value = true },
                onError = { _alarmSaved.value = false }
            )
    }

    fun getDefaultAlarmTitle(context: Context) {
        disposables += singleOf {
            getDefaultRingtone(context)
        }
            .backgroundTask()
            .subscribe { defaultRingtone ->
                ringtone = defaultRingtone
            }
    }

    fun setupData() {
        _alarmTime.value = timeToString(time)
    }

}