package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.exceptions.AlarmException
import com.helpfulapps.domain.repository.AlarmClockManager
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.mockData.MockData
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Test

class SwitchAlarmUseCaseTest {

    private val alarmRepository: AlarmRepository = mockk {}
    private val clockManager: AlarmClockManager = mockk {}
    val useCase = SwitchAlarmUseCaseImpl(alarmRepository, clockManager)

    /**
     * alarm to be switched off, is returned from repository as turnedOff
     * that is why in constructor I set isTurnedOn to false
     */
    @Test
    fun `should switch off alarm`() {

        val alarm = MockData.defaultAlarm

        every { alarmRepository.switchAlarm(any()) } returns Single.just(alarm)
        every { clockManager.stopAlarm(any()) } returns Completable.complete()

        useCase(SwitchAlarmUseCase.Params(alarmId = alarm.id))
            .test()
            .assertComplete()
            .dispose()
    }

    /**
     * there is the opposite of the @see `should switch off alarm`
     * that is why alarm is set to isTurnedOn = true
     */
    @Test
    fun `should switch on alarm`() {

        val onAlarm = MockData.defaultAlarm.copy(isTurnedOn = true)

        every { alarmRepository.switchAlarm(any()) } returns Single.just(onAlarm)
        every { clockManager.setAlarm(any()) } returns Completable.complete()

        useCase(SwitchAlarmUseCase.Params(1))
            .test()
            .assertComplete()
            .dispose()
    }

    @Test
    fun `should alarm switch fail`() {

        every { alarmRepository.switchAlarm(any()) } returns Single.error(AlarmException("failed"))

        useCase(SwitchAlarmUseCase.Params(1))
            .test()
            .assertError(AlarmException::class.java)
            .dispose()
    }

}