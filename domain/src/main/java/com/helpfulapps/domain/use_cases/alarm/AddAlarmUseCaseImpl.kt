package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.repository.AlarmClockManager
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.type.CompletableUseCaseWithParameter
import io.reactivex.Completable


interface AddAlarmUseCase : CompletableUseCaseWithParameter<AddAlarmUseCase.Params> {
    data class Params(val alarm: Alarm)
}

/**
 * This usecase should add newly created alarm to the DB and setAlarmClock
 */
class AddAlarmUseCaseImpl(
    private val _alarmManager: AlarmClockManager,
    private val _alarmRepository: AlarmRepository
) : AddAlarmUseCase {

    override fun invoke(parameter: AddAlarmUseCase.Params): Completable {
        return _alarmRepository.addAlarm(parameter.alarm)
            .flatMapCompletable { alarm ->
                _alarmManager.setAlarm(alarm)
            }
    }
}