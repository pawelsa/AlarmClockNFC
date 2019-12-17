package com.helpfulapps.data.repositories

import com.helpfulapps.data.db.alarm.dao.AlarmDao
import com.helpfulapps.data.db.alarm.model.AlarmData
import com.helpfulapps.domain.exceptions.AlarmException
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.reactivex.Single
import org.junit.Test
import com.helpfulapps.data.mockData.MockDataIns as MockData


class AlarmRepositoryImplTest {

    private val alarmDao: AlarmDao = mockk {}
    private val alarmRepositoryImpl = AlarmRepositoryImpl(alarmDao)


    @Test
    fun getNoAlarmsTest() {

        val repoMock = spyk(alarmRepositoryImpl)

        every { repoMock.getAlarmsQuery() } returns Single.never()
//        every { repoMock.getSchedulerIO() } returns Schedulers.trampoline()


        val testObserver = repoMock.getAlarms()
            .test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertResult(emptyList())
            .dispose()
    }

    @Test
    fun getAlarmsTest() {

        val alarm = MockData.defaultAlarm
        val alarmEntry = AlarmData(alarm)

        val repoMock = spyk(alarmRepositoryImpl)

        every { repoMock.getAlarmsQuery() } returns Single.just(listOf(alarmEntry))
//        every { repoMock.getSchedulerIO() } returns Schedulers.trampoline()

        repoMock.getAlarms()
            .test()
            .assertResult(listOf(alarm))
            .dispose()
    }

    @Test
    fun getAlarmCountTest() {

        val alarm1 = MockData.defaultAlarm
        val alarm2 = MockData.createDomainAlarm(id = 2)
        val alarm3 = MockData.createDomainAlarm(id = 3)

        val alarmEntry1 = AlarmData(alarm1)
        val alarmEntry2 = AlarmData(alarm2)
        val alarmEntry3 = AlarmData(alarm3)

        val repoMock = spyk(alarmRepositoryImpl)

        every { repoMock.getAlarmsQuery() } returns
                Single.just(
                    listOf(
                        alarmEntry1,
                        alarmEntry2,
                        alarmEntry3
                    )
                )

//        every { repoMock.getSchedulerIO() } returns Schedulers.trampoline()

        repoMock.getAlarms()
            .test()
            .assertResult(listOf(alarm1, alarm2, alarm3))
            .dispose()
    }

    @Test
    fun addAlarmTest() {

        val repoMock = spyk(alarmRepositoryImpl)

//        every { repoMock.getSchedulerIO() } returns Schedulers.trampoline()

        val alarm1 = repoMock.addAlarm(
            MockData.defaultAlarm
        )
        val alarm2 = repoMock.addAlarm(
            MockData.createDomainAlarm(id = 2)
        )
        val alarm3 = repoMock.addAlarm(
            MockData.createDomainAlarm(id = 3)
        )

        val alarmList = listOf(alarm1, alarm2, alarm3)

        Single.merge(alarmList)
            .test()
            .assertResult(
                MockData.defaultAlarm,
                MockData.createDomainAlarm(id = 2),
                MockData.createDomainAlarm(id = 3)
            )
            .dispose()
    }

    @Test
    fun removeAlarmWhenDbTableIsEmptyTest() {

        val repoMock = spyk(alarmRepositoryImpl)

//        every { repoMock.getSchedulerIO() } returns Schedulers.trampoline()

        repoMock.removeAlarm(5)
            .test()
            .assertError(NoSuchElementException::class.java)
            .dispose()
    }

    @Test
    fun removeAlarmTest() {

        val repoMock = spyk(alarmRepositoryImpl)

//        every { repoMock.getSchedulerIO() } returns Schedulers.trampoline()

        val saveAlarmInDb = repoMock.addAlarm(
            MockData.defaultAlarm
        )

        saveAlarmInDb.blockingGet()

        repoMock.removeAlarm(5)
            .test()
            .assertResult()
            .dispose()
    }

    @Test
    fun removeAlarmThatIsNotInDbTest() {

        val repoMock = spyk(alarmRepositoryImpl)

//        every { repoMock.getSchedulerIO() } returns Schedulers.trampoline()

        val saveAlarmInDb = repoMock.addAlarm(
            MockData.defaultAlarm
        )

        saveAlarmInDb.blockingGet()

        repoMock.removeAlarm(2)
            .test()
            .assertError(NoSuchElementException::class.java)
            .dispose()
    }

    @Test
    fun switchWhenThereIsAlarmInDbTest() {

        val repoMock = spyk(alarmRepositoryImpl)

//        every { repoMock.getSchedulerIO() } returns Schedulers.trampoline()

        val saveAlarmInDb = repoMock.addAlarm(
            MockData.defaultAlarm
        )

        saveAlarmInDb.blockingGet()

        repoMock.switchAlarm(5)
            .test()
            .assertResult(MockData.defaultAlarm)
            .dispose()
    }

    @Test
    fun switchWhenThereIsNoAlarmInDbTest() {

        val repoMock = spyk(alarmRepositoryImpl)

//        every { repoMock.getSchedulerIO() } returns Schedulers.trampoline()

        val alarm1 = repoMock.addAlarm(
            MockData.defaultAlarm
        )

        alarm1.concatWith(repoMock.switchAlarm(2))
            .test()
            .assertError(NoSuchElementException::class.java)
            .dispose()
    }

    @Test
    fun switchWhenDbTableIsEmptyTest() {

        val repoMock = spyk(alarmRepositoryImpl)

//        every { repoMock.getSchedulerIO() } returns Schedulers.trampoline()

        repoMock.switchAlarm(2)
            .test()
            .assertError(NoSuchElementException::class.java)
            .dispose()
    }

    @Test
    fun updateWhenThereIsAlarmInDbTest() {

        val repoMock = spyk(alarmRepositoryImpl)

//        every { repoMock.getSchedulerIO() } returns Schedulers.trampoline()
        val alarm = MockData.defaultAlarm
        val saveAlarmInDb = repoMock.addAlarm(alarm)

        saveAlarmInDb.blockingGet()

        repoMock.updateAlarm(alarm)
            .test()
            .assertResult(alarm)
            .dispose()
    }

    @Test
    fun updateWhenDbTableIsEmptyTest() {

        val repoMock = spyk(alarmRepositoryImpl)

//        every { repoMock.getSchedulerIO() } returns Schedulers.trampoline()

        val alarm1 = MockData.defaultAlarm

        repoMock.updateAlarm(alarm1)
            .test()
            .assertError(AlarmException::class.java)
            .dispose()
    }

    @Test
    fun updateWhenThereIsNotAlarmInDbTest() {

        val repoMock = spyk(alarmRepositoryImpl)

//        every { repoMock.getSchedulerIO() } returns Schedulers.trampoline()

        val alarm = MockData.defaultAlarm
        val alarmNotInDb = MockData.createDomainAlarm(id = 4)
        val alarm1 = repoMock.addAlarm(alarm)

        alarm1.concatWith(repoMock.updateAlarm(alarmNotInDb))
            .test()
            .assertError(AlarmException::class.java)
            .dispose()
    }

    @Test
    fun moreAddedAlarmsShouldIncrementAlarmId() {

        val repoMock = spyk(alarmRepositoryImpl)

        val alarm1 = MockData.defaultAlarm
        val alarm2 = MockData.createDomainAlarm(id = 4)

//        every { repoMock.getSchedulerIO() } returns Schedulers.trampoline()

        Single.merge(repoMock.addAlarm(alarm1), repoMock.addAlarm(alarm2)).blockingLast()

        repoMock.getAlarms().map { alarmList -> alarmList.count() }
            .test()
            .assertResult(2)
            .dispose()

    }

}
