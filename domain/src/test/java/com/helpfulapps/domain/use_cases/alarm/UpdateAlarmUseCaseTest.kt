package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.exceptions.AlarmException
import com.helpfulapps.domain.repository.AlarmClockManager
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.BaseUseCaseTest
import com.helpfulapps.domain.use_cases.mockData.MockData
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.jupiter.api.Test

class UpdateAlarmUseCaseTest : BaseUseCaseTest<UpdateAlarmUseCase>() {

    private val alarmRepository: AlarmRepository = mockk {}
    private val clockManager: AlarmClockManager = mockk {}
    override val useCase = UpdateAlarmUseCaseImpl(alarmRepository, clockManager)

    @Test
    fun `should update alarm`() {

        val testAlarm = MockData.defaultAlarm

        every { alarmRepository.updateAlarm(any()) } returns Single.just(testAlarm)
        every { clockManager.setAlarm(any()) } returns Completable.complete()
        every { clockManager.stopAlarm(any()) } returns Completable.complete()

        useCase(
            UpdateAlarmUseCase.Params(testAlarm)
        )
            .test()
            .assertComplete()
            .dispose()
    }


    @Test
    fun `should stopping alarm fail`() {

        val testAlarm = MockData.defaultAlarm

        every { alarmRepository.updateAlarm(any()) } returns Single.just(testAlarm)
        every { clockManager.setAlarm(any()) } returns Completable.complete()
        every { clockManager.stopAlarm(any()) } returns Completable.error(AlarmException("k"))

        useCase(
            UpdateAlarmUseCase.Params(testAlarm)
        )
            .test()
            .assertError(AlarmException::class.java)
            .dispose()
    }


    @Test
    fun `should setting alarm fail`() {

        val testAlarm = MockData.defaultAlarm

        every { alarmRepository.updateAlarm(any()) } returns Single.just(testAlarm)
        every { clockManager.stopAlarm(any()) } returns Completable.complete()
        every { clockManager.setAlarm(any()) } returns Completable.error(AlarmException("k"))

        useCase(
            UpdateAlarmUseCase.Params(testAlarm)
        )
            .test()
            .assertError(AlarmException::class.java)
            .dispose()
    }

}