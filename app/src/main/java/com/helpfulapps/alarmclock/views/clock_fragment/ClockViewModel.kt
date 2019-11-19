package com.helpfulapps.alarmclock.views.clock_fragment

import android.util.Log
import com.helpfulapps.base.base.BaseViewModel
import com.helpfulapps.domain.eventBus.DatabaseNotifiers
import com.helpfulapps.domain.eventBus.RxBus
import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.use_cases.alarm.GetAlarmsUseCase
import com.helpfulapps.domain.use_cases.alarm.RemoveAlarmUseCase
import com.helpfulapps.domain.use_cases.alarm.SwitchAlarmUseCase
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy


class ClockViewModel(
    private val getAlarmsUseCase: GetAlarmsUseCase,
    private val switchAlarmUseCase: SwitchAlarmUseCase,
    private val removeAlarmUseCase: RemoveAlarmUseCase
) : BaseViewModel() {

    private val TAG = ClockViewModel::class.java.simpleName

    lateinit var adapter: ClockListAdapter

    fun getAlarms() {
        disposables += getAlarmsUseCase()
            .map { list ->
                list.map { AlarmData(it) }
            }
            .subscribeBy {
                //                it[it.size -2].toChange = !it[it.size -2].toChange
                it[it.size - 1].toChange = !it[it.size - 1].toChange
                adapter.submitList(it)
            }
    }

    fun subscribeToDatabaseChanges() {
        disposables += RxBus.listen(DatabaseNotifiers::class.java)
            .subscribe {
                getAlarms()
            }
    }

    // TODO this should inform view about successful change
    fun switchAlarm(alarm: Alarm) {
        disposables += switchAlarmUseCase(SwitchAlarmUseCase.Params(alarm.id))
            .subscribe(
                { },
                { it.printStackTrace() })
    }

    // TODO this should inform view about successful change
    fun removeAlarm(alarm: Alarm) {
        disposables += removeAlarmUseCase(RemoveAlarmUseCase.Params(alarm.id))
            .subscribe {
                Log.d(TAG, "removed alarm successfully")
            }
    }


}