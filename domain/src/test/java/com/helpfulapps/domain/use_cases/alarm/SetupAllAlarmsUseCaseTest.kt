package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.exceptions.AlarmException
import com.helpfulapps.domain.helpers.singleOf
import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.repository.AlarmClockManager
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.BaseUseCaseTest
import com.helpfulapps.domain.use_cases.mockData.MockData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.jupiter.api.Test

class SetupAllAlarmsUseCaseTest : BaseUseCaseTest<SetupAllAlarmsUseCase>() {

    private val alarmRepository: AlarmRepository = mockk {}
    private val alarmManager: AlarmClockManager = mockk {}
    override val useCase: SetupAllAlarmsUseCase =
        SetupAllAlarmsUseCaseImpl(alarmRepository, alarmManager)

    @Test
    fun `when alarm list is empty, should complete`() {
        every { alarmRepository.getAlarms() } returns singleOf { emptyList<Alarm>() }

        useCase()
            .test()
            .assertComplete()
            .dispose()

        verify(exactly = 0) { alarmManager.setAlarm(any()) }
    }

    @Test
    fun `when alarm list not empty, alarms should be started, and complete`() {
        every { alarmRepository.getAlarms() } returns singleOf { MockData.lateAlarmList }
        every { alarmManager.setAlarm(any()) } returns Completable.complete()

        useCase()
            .test()
            .assertComplete()
            .dispose()

        verify(exactly = 5) { alarmManager.setAlarm(any()) }
    }

    @Test
    fun `when obtaining alarms fails, should try again`() {
        every { alarmRepository.getAlarms() } returns Single.error(AlarmException("a"))

        useCase()
            .test()
            .assertError(AlarmException::class.java)
            .dispose()

        verify(exactly = 2) { alarmRepository.getAlarms() }
    }

    @Test
    fun `non repeating alarm, that should be started in meantime, should not be set up`() {
        every { alarmRepository.getAlarms() } returns singleOf { MockData.alarmList }
        every { alarmManager.setAlarm(any()) } returns Completable.complete()

        useCase()
            .test()
            .assertComplete()
            .dispose()

        verify(exactly = 3) { alarmManager.setAlarm(any()) }
    }

}