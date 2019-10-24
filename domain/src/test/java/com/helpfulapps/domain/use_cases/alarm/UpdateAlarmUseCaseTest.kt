package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.exceptions.AlarmException
import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.alarm.definition.UpdateAlarmUseCase
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import org.junit.Test

class UpdateAlarmUseCaseTest {

    val alarmRepository: AlarmRepository = mockk {}
    val useCase = UpdateAlarmUseCaseImpl(alarmRepository)

    @Test
    fun `should update alarm`() {

        every { alarmRepository.updateAlarm(any()) } returns Completable.complete()
        useCase(
            UpdateAlarmUseCase.Params(
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
    fun `updating alarm should fail`() {

        every { alarmRepository.updateAlarm(any()) } returns Completable.error(AlarmException("failed"))
        useCase(
            UpdateAlarmUseCase.Params(
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