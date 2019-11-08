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

class AddAlarmUseCaseTest {

    private val alarmRepository: AlarmRepository = mockk {}
    private val alarmManager: AlarmClockManager = mockk {}
    val useCase = AddAlarmUseCaseImpl(alarmManager, alarmRepository)

    @Test
    fun `adding alarm was succcessful`() {

        every { alarmManager.setAlarm(any()) } returns Completable.complete()
        every { alarmRepository.addAlarm(any()) } returns Single.create {
            it.onSuccess(
                MockData.defaultAlarm
            )
        }

        useCase(
            AddAlarmUseCase.Params(
                MockData.defaultAlarm
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
                MockData.defaultAlarm
            )
        )
            .test()
            .assertError(AlarmException::class.java)
            .dispose()
    }

}