package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.helpers.TimeSetter
import com.helpfulapps.domain.repository.AlarmClockManager
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.type.CompletableUseCase
import io.reactivex.Completable
import java.util.*

interface SetupAllAlarmsUseCase : CompletableUseCase

class SetupAllAlarmsUseCaseImpl(
    private val alarmRepository: AlarmRepository,
    private val alarmClockManager: AlarmClockManager
) : SetupAllAlarmsUseCase {

    override fun invoke(): Completable {
        return alarmRepository.getAlarms()
            .onErrorResumeNext { alarmRepository.getAlarms() }
            .map { alarmList ->
                val currentDayOfWeek = GregorianCalendar.getInstance().get(Calendar.DAY_OF_WEEK)
                val timeSetter = TimeSetter()
                alarmList
                    .filter { alarm ->
                        if (!alarm.isTurnedOn || alarm.isRepeating) return@filter true

                        val calendar = timeSetter.getAlarmStartingCalendar(alarm)
                        calendar.get(Calendar.DAY_OF_WEEK) == currentDayOfWeek
                    }
                    .map(alarmClockManager::setAlarm)
            }
            .flatMapCompletable {
                return@flatMapCompletable Completable.merge(it)
            }
    }
}