package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.repository.AlarmClockManager
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.type.CompletableUseCaseWithParameter
import io.reactivex.Completable

interface UpdateAlarmUseCase : CompletableUseCaseWithParameter<UpdateAlarmUseCase.Params> {
    data class Params(val alarm: Alarm)
}

class UpdateAlarmUseCaseImpl(
    private val repository: AlarmRepository,
    private val clockManager: AlarmClockManager
) :
    UpdateAlarmUseCase {

    override fun invoke(parameter: UpdateAlarmUseCase.Params): Completable =
        repository.updateAlarm(parameter.alarm)
            .flatMapCompletable {
                return@flatMapCompletable Completable.concat(
                    listOf(
                        clockManager.stopAlarm(it.id),
                        clockManager.setAlarm(parameter.alarm)
                    )
                )
            }
            .doOnComplete { println("UpdateAlarmUseCase, completed") }
}