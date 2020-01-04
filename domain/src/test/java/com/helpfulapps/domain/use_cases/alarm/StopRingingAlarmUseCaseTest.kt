package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.helpers.singleOf
import com.helpfulapps.domain.repository.AlarmClockManager
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.repository.StatsRepository
import com.helpfulapps.domain.use_cases.BaseUseCaseTest
import com.helpfulapps.domain.use_cases.mockData.MockData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import org.junit.jupiter.api.Test

class StopRingingAlarmUseCaseTest : BaseUseCaseTest<StopRingingAlarmUseCase>() {

    private val alarmRepository: AlarmRepository = mockk {}
    private val alarmClockManager: AlarmClockManager = mockk {}
    private val statsRepository: StatsRepository = mockk {}
    override val useCase: StopRingingAlarmUseCase =
        StopRingingAlarmUseCaseImpl(alarmRepository, alarmClockManager, statsRepository)

    @Test
    fun `not repeating alarm should be stopped`() {
        every { alarmRepository.updateAlarm(any()) } returns singleOf {
            MockData.defaultAlarm.copy(
                isTurnedOn = false
            )
        }
        every { statsRepository.saveInfo(any()) } returns Completable.complete()

        useCase(StopRingingAlarmUseCase.Param(MockData.defaultAlarm))
            .test()
            .assertComplete()
            .dispose()
        verify(exactly = 0) { alarmClockManager.setAlarm(any()) }
    }

    @Test
    fun `repeating alarm should be started again`() {
        every { alarmRepository.updateAlarm(any()) } returns singleOf {
            MockData.alarmList[3].copy(
                isTurnedOn = false
            )
        }
        every { alarmClockManager.setAlarm(any()) } returns Completable.complete()
        every { statsRepository.saveInfo(any()) } returns Completable.complete()

        useCase(StopRingingAlarmUseCase.Param(MockData.alarmList[3]))
            .test()
            .assertComplete()
            .dispose()
        verify(exactly = 1) { alarmClockManager.setAlarm(any()) }
    }

}