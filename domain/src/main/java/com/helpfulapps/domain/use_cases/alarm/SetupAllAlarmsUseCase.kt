package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.repository.AlarmClockManager
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.type.CompletableUseCase
import io.reactivex.Completable

interface SetupAllAlarmsUseCase : CompletableUseCase

class SetupAllAlarmsUseCaseImpl(
    private val alarmRepository: AlarmRepository,
    private val alarmClockManager: AlarmClockManager
) : SetupAllAlarmsUseCase {

    override fun invoke(): Completable {
        return alarmRepository.getAlarms()
            .onErrorResumeNext { alarmRepository.getAlarms() }
            .map {
                it.map { alarm ->
                    alarmClockManager.setAlarm(alarm)
                }
            }
            .flatMapCompletable {
                return@flatMapCompletable Completable.merge(it)
            }
    }
}