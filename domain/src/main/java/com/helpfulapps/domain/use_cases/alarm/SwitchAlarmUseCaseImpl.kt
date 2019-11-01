package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.repository.AlarmClockManager
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.type.CompletableUseCaseWithParameter
import io.reactivex.Completable


interface SwitchAlarmUseCase : CompletableUseCaseWithParameter<SwitchAlarmUseCase.Params> {
    data class Params(val alarmId: Long)
}

class SwitchAlarmUseCaseImpl(
    private val _repository: AlarmRepository,
    private val _clockManager: AlarmClockManager
) : SwitchAlarmUseCase {

    private val TAG = SwitchAlarmUseCaseImpl::class.java.simpleName

    override fun invoke(parameter: SwitchAlarmUseCase.Params): Completable {
        println("$TAG, parameter is ${parameter.alarmId}")

        return _repository.switchAlarm(parameter.alarmId)
            .doOnError {
                println("$TAG, error after repository")
            }
            .flatMapCompletable { alarm ->
                when (alarm.isTurnedOn) {
                    true -> _clockManager.setAlarm(alarm)
                        .doOnError {
                            println("$TAG, error after setAlarm")
                        }
                    false -> _clockManager.stopAlarm(alarm.id)
                        .doOnError {
                            println("$TAG, error after stopAlarm")
                        }
                }
            }
    }
}