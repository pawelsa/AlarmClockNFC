package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.exceptions.AlarmException
import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.repository.AlarmClockManager
import com.helpfulapps.domain.repository.AlarmRepository
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Test

class AddAlarmUseCaseTest {

    val alarmRepository: AlarmRepository = mockk {}
    val alarmManager: AlarmClockManager = mockk {}
    val useCase = AddAlarmUseCaseImpl(alarmManager, alarmRepository)

    @Test
    fun `adding alarm was succcessful`() {

        every { alarmManager.setAlarm(any()) } returns Completable.complete()
        every { alarmRepository.addAlarm(any()) } returns Single.create {
            it.onSuccess(
                Alarm(
                    id = 1,
                    hour = 1,
                    minute = 2,
                    ringtoneUrl = "ringtoneUrl",
                    repetitionDays = arrayOf(),
                    isVibrationOn = false,
                    isTurnedOn = true,
                    isRepeating = false,
                    name = "Alarm 1"
                )
            )
        }

        useCase(
            AddAlarmUseCase.Params(
                Alarm(
                    hour = 1,
                    ringtoneUrl = "ringtoneUrl",
                    repetitionDays = arrayOf(),
                    name = "Alarm 1",
                    isVibrationOn = false,
                    isTurnedOn = true,
                    isRepeating = false,
                    minute = 2
                )
            )
        )
            .test()
            .assertComplete()
            .dispose()
    }

    @Test
    fun `adding alarm was not successful`() {
        every { alarmRepository.addAlarm(any()) } returns Single.error(AlarmException("failed"))

        useCase(
            AddAlarmUseCase.Params(
                Alarm(
                    hour = 1,
                    id = 1,
                    ringtoneUrl = "ringtoneUrl",
                    repetitionDays = arrayOf(),
                    name = "Alarm 1",
                    isVibrationOn = false,
                    isTurnedOn = true,
                    isRepeating = false,
                    minute = 2
                )
            )
        )
            .test()
            .assertError(AlarmException::class.java)
            .dispose()
    }

}