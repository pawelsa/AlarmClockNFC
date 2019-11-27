package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.helpers.singleOf
import com.helpfulapps.domain.repository.AlarmClockManager
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.mockData.MockData
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Test

class SnoozeAlarmUseCaseTest {

    private val alarmRepository: AlarmRepository = mockk {}
    private val clockManager: AlarmClockManager = mockk {}
    private val useCase = SnoozeAlarmUseCaseImpl(alarmRepository, clockManager)

    @Test
    fun `should snooze alarm`() {
        every { alarmRepository.getAlarm(any()) } returns singleOf { MockData.defaultAlarm }
        every { clockManager.snoozeAlarm(any()) } returns Completable.complete()

        useCase(SnoozeAlarmUseCase.Param(1L))
            .test()
            .assertComplete()
            .dispose()
    }

    @Test
    fun `should snooze alarm fail, because of repository`() {
        every { alarmRepository.getAlarm(any()) } returns Single.error(Exception())

        useCase(SnoozeAlarmUseCase.Param(1L))
            .test()
            .assertError(Exception::class.java)
            .dispose()
    }

    @Test
    fun `should snooze alarm fail, because of manager`() {
        every { alarmRepository.getAlarm(any()) } returns singleOf { MockData.defaultAlarm }
        every { clockManager.snoozeAlarm(any()) } returns Completable.error(Exception())

        useCase(SnoozeAlarmUseCase.Param(1L))
            .test()
            .assertError(Exception::class.java)
            .dispose()
    }

}