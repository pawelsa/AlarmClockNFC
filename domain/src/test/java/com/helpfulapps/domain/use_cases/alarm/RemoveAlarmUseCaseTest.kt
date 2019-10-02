package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.exceptions.AlarmException
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.alarm.definition.RemoveAlarmUseCase
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import org.junit.Test

class RemoveAlarmUseCaseTest {

    val alarmRepository: AlarmRepository = mockk {}
    val useCase = RemoveAlarmUseCaseImpl(alarmRepository)

    @Test
    fun `alarm should be removed`() {

        every { alarmRepository.removeAlarm(any()) } returns Completable.complete()
        useCase(RemoveAlarmUseCase.Params(1))
            .test()
            .assertComplete()
            .dispose()
    }

    @Test
    fun `alarm removal should fail`() {

        every { alarmRepository.removeAlarm(any()) } returns Completable.error(AlarmException("failed"))
        useCase(RemoveAlarmUseCase.Params(1))
            .test()
            .assertError(AlarmException::class.java)
            .dispose()
    }

}