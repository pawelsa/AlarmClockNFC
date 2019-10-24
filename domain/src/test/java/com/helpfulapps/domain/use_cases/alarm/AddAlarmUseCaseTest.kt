package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.exceptions.AlarmException
import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.repository.AlarmClockManager
import com.helpfulapps.domain.use_cases.alarm.definition.AddAlarmUseCase
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import org.junit.Test

class AddAlarmUseCaseTest {

    val alarmRepository: AlarmClockManager = mockk {}
    val useCase = AddAlarmUseCaseImpl(alarmRepository)

    @Test
    fun `adding alarm was succcessful`() {

        every { alarmRepository.setAlarm(any()) } returns Completable.complete()
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
            .assertComplete()
            .dispose()
    }

    @Test
    fun `adding alarm was not successful`() {
        every { alarmRepository.setAlarm(any()) } returns Completable.error(AlarmException("failed"))
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