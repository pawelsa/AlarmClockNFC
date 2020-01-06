package com.helpfulapps.alarmclock.views.clock_fragment.add_alarm_bs

import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.helpfulapps.alarmclock.helpers.AlarmChanged
import com.helpfulapps.alarmclock.helpers.extensions.timeToString
import com.helpfulapps.alarmclock.helpers.getDefaultRingtone
import com.helpfulapps.base.base.BaseViewModel
import com.helpfulapps.base.extensions.rx.backgroundTask
import com.helpfulapps.domain.eventBus.RxBus
import com.helpfulapps.domain.extensions.singleOf
import com.helpfulapps.domain.helpers.Settings
import com.helpfulapps.domain.helpers.Time
import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.use_cases.alarm.AddAlarmUseCase
import com.helpfulapps.domain.use_cases.alarm.UpdateAlarmUseCase
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

class AddAlarmBottomSheetViewModel(
    private val _addAlarmUseCase: AddAlarmUseCase,
    private val _updateAlarmUseCase: UpdateAlarmUseCase,
    _settings: Settings
) : BaseViewModel() {

    private val _alarmTime: MutableLiveData<String> = MutableLiveData()
    val alarmTime: LiveData<String>
        get() = _alarmTime

    private val _ringtoneTitle: MutableLiveData<String> = MutableLiveData()
    val ringtoneTitle: LiveData<String>
        get() = _ringtoneTitle

    private val _alarmTitle: MutableLiveData<String> = MutableLiveData()
    val alarmTitle: LiveData<String>
        get() = _alarmTitle

    var repeatingDays = MutableLiveData<Array<Boolean>>().apply { value = Array(7) { false } }
    val vibrating = ObservableBoolean(true)
    val repeating = ObservableBoolean(false)
    val usingNfc = ObservableBoolean(false)
    val hasNfc = _settings.hasNfc

    private val _alarmSaved = MutableLiveData<Boolean>()
    val alarmSaved: LiveData<Boolean>
        get() = _alarmSaved

    var ringtone: Pair<String, String> = Pair("", "")
        set(value) {
            field = value
            _ringtoneTitle.value = value.first
        }

    var time: Pair<Int, Int> = Pair(8, 30)
        set(value) {
            field = value
            _alarmTime.value =
                timeToString(value)
        }

    private var alarmId = -1L

    fun setAlarm(alarm: Alarm) {
        alarmId = alarm.id
        repeating.set(alarm.isRepeating)
        _alarmTitle.value = alarm.title
        ringtone = alarm.ringtoneTitle to alarm.ringtoneUrl
        vibrating.set(alarm.isVibrationOn)
        repeatingDays.value = alarm.repetitionDays
        time = Time(alarm.hour, alarm.minute)
        usingNfc.set(alarm.isUsingNFC)
    }


    fun saveAlarm(isUpdating: Boolean) {
        val repetitionDays = repeatingDays.value ?: Array(7) { false }
        val finalAlarm = Alarm(
            id = alarmId,
            title = alarmTitle.value ?: "",
            isVibrationOn = vibrating.get(),
            isRepeating = repetitionDays.any { it },
            isTurnedOn = true,
            ringtoneTitle = ringtone.first,
            ringtoneUrl = ringtone.second,
            isUsingNFC = usingNfc.get(),
            hour = time.first,
            minute = time.second,
            repetitionDays = repetitionDays
        )
        RxBus.publish(AlarmChanged(finalAlarm))

        when {
            isUpdating -> updateAlarm(finalAlarm)
            else -> saveAlarm(finalAlarm)
        }
    }

    private fun updateAlarm(alarm: Alarm) {
        disposables += _updateAlarmUseCase(UpdateAlarmUseCase.Params(alarm))
            .backgroundTask()
            .subscribeBy(
                onComplete = { _alarmSaved.value = true },
                onError = { _alarmSaved.value = false }
            )
    }

    private fun saveAlarm(alarm: Alarm) {
        disposables += _addAlarmUseCase(AddAlarmUseCase.Params(alarm))
            .backgroundTask()
            .subscribeBy(
                onComplete = { _alarmSaved.value = true },
                onError = { _alarmSaved.value = false }
            )
    }

    fun getDefaultRingtoneTitle(context: Context) {
        if (ringtone.first.isEmpty() || ringtone.second.isEmpty()) {
            disposables += singleOf {
                getDefaultRingtone(context)
            }
                .backgroundTask()
                .subscribe { defaultRingtone ->
                    ringtone = defaultRingtone
                }
        }
    }

    fun setAlarmTitle(newTitle: String) {
        _alarmTitle.value = newTitle
    }

    fun setupData() {
        _alarmTime.value =
            timeToString(time)
    }
}