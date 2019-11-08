package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.exceptions.AlarmException
import com.helpfulapps.domain.repository.AlarmClockManager
import com.helpfulapps.domain.repository.AlarmRepository
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import org.junit.Test

class RemoveAlarmUseCaseTest {

    private val alarmRepository: AlarmRepository = mockk {}
    private val alarmManager: AlarmClockManager = mockk {}
    val useCase = RemoveAlarmUseCaseImpl(alarmManager, alarmRepository)

    @Test
    fun `alarm should be removed`() {

        every { alarmRepository.removeAlarm(any()) } returns Completable.complete()
        every { alarmManager.stopAlarm(any()) } returns Completable.complete()

        useCase(RemoveAlarmUseCase.Params(1))
            .test()
            .assertComplete()
            .dispose()
    }

    @Test
    fun `alarm removal should fail`() {

        every { alarmManager.stopAlarm(any()) } returns Completable.complete()
        every { alarmRepository.removeAlarm(any()) } returns Completable.error(AlarmException("failed"))

        useCase(RemoveAlarmUseCase.Params(1))
            .test()
            .assertError(AlarmException::class.java)
            .dispose()
    }

    @Test
    fun `alarm stopping should fail`() {

        every { alarmManager.stopAlarm(any()) } returns Completable.error(Exception())
        every { alarmRepository.removeAlarm(any()) } returns Completable.complete()

        useCase(RemoveAlarmUseCase.Params(1))
            .test()
            .assertError(Exception::class.java)
            .dispose()
    }

}