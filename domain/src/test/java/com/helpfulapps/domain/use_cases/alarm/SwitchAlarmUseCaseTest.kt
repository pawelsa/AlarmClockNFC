package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.exceptions.AlarmException
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.alarm.definition.SwitchAlarmUseCase
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import org.junit.Test

class SwitchAlarmUseCaseTest {

    val alarmRepository: AlarmRepository = mockk {}
    val useCase = SwitchAlarmUseCaseImpl(alarmRepository)

    @Test
    fun `should switch alarm`() {

        every { alarmRepository.switchAlarm(any()) } returns Completable.complete()
        useCase(SwitchAlarmUseCase.Params(1))
            .test()
            .assertComplete()
            .dispose()
    }

    @Test
    fun `should alarm switch fail`() {
        every { alarmRepository.switchAlarm(any()) } returns Completable.error(AlarmException("failed"))
        useCase(SwitchAlarmUseCase.Params(1))
            .test()
            .assertError(AlarmException::class.java)
            .dispose()
    }

}