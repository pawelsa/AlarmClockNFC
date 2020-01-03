package com.helpfulapps.alarmclock.views.clock_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.helpfulapps.base.base.BaseViewModel
import com.helpfulapps.base.extensions.rx.backgroundTask
import com.helpfulapps.domain.eventBus.DatabaseNotifiers
import com.helpfulapps.domain.eventBus.RxBus
import com.helpfulapps.domain.helpers.Settings
import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.use_cases.alarm.GetAlarmsUseCase
import com.helpfulapps.domain.use_cases.alarm.RemoveAlarmUseCase
import com.helpfulapps.domain.use_cases.alarm.SwitchAlarmUseCase
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy


class ClockViewModel(
    private val getAlarmsUseCase: GetAlarmsUseCase,
    private val switchAlarmUseCase: SwitchAlarmUseCase,
    private val removeAlarmUseCase: RemoveAlarmUseCase,
    private val settings: Settings
) : BaseViewModel() {

    private val TAG = ClockViewModel::class.java.simpleName

    private val _alarmList: MutableLiveData<List<AlarmData>> = MutableLiveData()
    val alarmList: LiveData<List<AlarmData>>
        get() = _alarmList

    val askForBatteryOptimization = settings.askForBatteryOptimization

    fun getAlarms() {
        disposables += getAlarmsUseCase()
            .map { list ->
                list.map { AlarmData(it) }
            }
            .backgroundTask()
            .subscribeBy(
                onSuccess = {
                    val lastButOne = it.size - 1
                    if (lastButOne >= 0) {
                        it[lastButOne].toChange = !it[lastButOne].toChange
                    }
                    _alarmList.value = it
                },
                onError = {

                }
            )
    }

    fun batteryOptimizationTurnedOff() {
        settings.askForBatteryOptimization = false
    }

    fun subscribeToDatabaseChanges() {
        disposables += RxBus.listen(DatabaseNotifiers::class.java)
            .backgroundTask()
            .subscribe {
                getAlarms()
            }
    }

    // TODO this should inform view about successful change
    fun switchAlarm(alarm: Alarm) {
        disposables += switchAlarmUseCase(SwitchAlarmUseCase.Params(alarm.id))
            .backgroundTask()
            .subscribe(
                { },
                { it.printStackTrace() })
    }

    // TODO this should inform view about successful change
    fun removeAlarm(alarm: Alarm) {
        disposables += removeAlarmUseCase(RemoveAlarmUseCase.Params(alarm.id))
            .backgroundTask()
            .subscribe(
                { },
                { it.printStackTrace() }
            )
    }


}