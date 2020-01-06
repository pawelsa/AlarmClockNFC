package com.helpfulapps.alarmclock.views.clock_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.helpfulapps.alarmclock.helpers.*
import com.helpfulapps.base.base.BaseViewModel
import com.helpfulapps.base.extensions.rx.backgroundTask
import com.helpfulapps.domain.eventBus.DatabaseNotifiers
import com.helpfulapps.domain.eventBus.RxBus
import com.helpfulapps.domain.helpers.Settings
import com.helpfulapps.domain.helpers.TimeSetter
import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.use_cases.alarm.GetAlarmsUseCase
import com.helpfulapps.domain.use_cases.alarm.RemoveAlarmUseCase
import com.helpfulapps.domain.use_cases.alarm.SwitchAlarmUseCase
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import java.util.*
import kotlin.math.abs


class ClockViewModel(
    private val getAlarmsUseCase: GetAlarmsUseCase,
    private val switchAlarmUseCase: SwitchAlarmUseCase,
    private val removeAlarmUseCase: RemoveAlarmUseCase,
    private val settings: Settings
) : BaseViewModel() {

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
                    if (alarmList.value != null
                        && alarmList.value!!.isNotEmpty()
                        && it.size > alarmList.value?.size ?: 0
                    ) {
                        _message.value = AlarmTurnedOn(getTimeToAlarm(it[it.size - 1].toDomain()))
                    }
                    _alarmList.value = it
                },
                onError = { _error.value = CouldNotObtainAlarms }
            )
    }

    fun batteryOptimizationTurnedOff() {
        settings.askForBatteryOptimization = false
    }

    fun subscribeToDatabaseChanges() {
        disposables += RxBus.listen(DatabaseNotifiers::class.java)
            .backgroundTask()
            .subscribe { getAlarms() }
    }

    fun subscribeToAlarmChange() {
        disposables += RxBus.listen(AlarmChanged::class.java)
            .backgroundTask()
            .subscribe { _message.value = AlarmTurnedOn(getTimeToAlarm(it.alarm)) }
    }

    private fun getTimeToAlarm(alarm: Alarm): Long {
        val timeSetter = TimeSetter()
        val startTime = timeSetter.getAlarmStartingTime(alarm)
        val currentTime = GregorianCalendar.getInstance().timeInMillis
        return abs(startTime - currentTime)
    }

    fun switchAlarm(alarm: Alarm) {
        disposables += switchAlarmUseCase(SwitchAlarmUseCase.Params(alarm.id))
            .backgroundTask()
            .subscribe(
                { if (alarm.isTurnedOn) _message.value = AlarmTurnedOn(getTimeToAlarm(alarm)) },
                { _error.value = CouldNotSwitchAlarm(!alarm.isTurnedOn) })
    }

    fun removeAlarm(alarm: Alarm) {
        disposables += removeAlarmUseCase(RemoveAlarmUseCase.Params(alarm.id))
            .backgroundTask()
            .subscribe(
                { _message.value = AlarmRemoved },
                { _error.value = CouldNotRemoveAlarm }
            )
    }


}