package com.helpfulapps.data.repositories

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.helpfulapps.data.db.alarm.model.AlarmEntry
import com.helpfulapps.domain.exceptions.AlarmException
import com.helpfulapps.domain.models.alarm.Alarm
import com.raizlabs.android.dbflow.config.FlowManager
import io.mockk.every
import io.mockk.spyk
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


@RunWith(AndroidJUnit4::class)
class AlarmRepositoryImplTest {

    lateinit var alarmRepositoryImpl: AlarmRepositoryImpl
    lateinit var context: Context

    @Before
    fun setUp() {

        val application = ApplicationProvider.getApplicationContext<Application>()
        context = application.applicationContext
        alarmRepositoryImpl =
            AlarmRepositoryImpl(application.applicationContext)
    }

    @After
    fun destroy() {
        FlowManager.destroy()
    }

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

        val alarm = Alarm(
            0,
            "",
            false,
            true,
            false,
            15,
            0L,
            15L,
            arrayOf(true, true, false, false, false, false, false)
        )
        val alarmEntry = AlarmEntry(alarm)

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

        val alarm1 = Alarm(
            0,
            "",
            false,
            true,
            false,
            15,
            0L,
            15L,
            arrayOf(true, true, false, false, false, false, false)
        )
        val alarm2 = Alarm(
            1,
            "co tam slychac",
            true,
            true,
            false,
            15,
            0L,
            15L,
            arrayOf(true, true, false, false, false, false, false)
        )
        val alarm3 = Alarm(
            2,
            "test",
            false,
            true,
            false,
            15,
            0L,
            15L,
            arrayOf(true, true, false, false, false, false, false)
        )

        val alarmEntry1 = AlarmEntry(alarm1)
        val alarmEntry2 = AlarmEntry(alarm2)
        val alarmEntry3 = AlarmEntry(alarm3)

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
            Alarm(
                0,
                "",
                false,
                true,
                false,
                15,
                0L,
                15L,
                arrayOf(true, true, false, false, false, false, false)
            )
        )
        val alarm2 = repoMock.addAlarm(
            Alarm(
                0,
                "co tam slychac",
                true,
                true,
                false,
                15,
                0L,
                15L,
                arrayOf(true, true, false, false, false, false, false)
            )
        )
        val alarm3 =
            repoMock.addAlarm(
                Alarm(
                    0,
                    "test",
                    false,
                    true,
                    false,
                    15,
                    0L,
                    15L,
                    arrayOf(true, true, false, false, false, false, false)
                )
            )

        val alarmList = listOf(alarm1, alarm2, alarm3)

        Completable.merge(alarmList)
            .test()
            .assertResult()
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

        val alarm1 = repoMock.addAlarm(
            Alarm(
                5,
                "",
                false,
                true,
                false,
                15,
                0L,
                15L,
                arrayOf(true, true, false, false, false, false, false)
            )
        )

        alarm1.concatWith(repoMock.removeAlarm(5))
            .test()
            .assertResult()
            .dispose()
    }

    @Test
    fun removeAlarmThatIsNotInDbTest() {

        val repoMock = spyk(alarmRepositoryImpl)

//        every { repoMock.getSchedulerIO() } returns Schedulers.trampoline()

        val alarm1 = repoMock.addAlarm(
            Alarm(
                5,
                "",
                false,
                true,
                false,
                15,
                0L,
                15L,
                arrayOf(true, true, false, false, false, false, false)
            )
        )

        alarm1.concatWith(repoMock.removeAlarm(2))
            .test()
            .assertError(NoSuchElementException::class.java)
            .dispose()
    }

    @Test
    fun switchWhenThereIsAlarmInDbTest() {

        val repoMock = spyk(alarmRepositoryImpl)

//        every { repoMock.getSchedulerIO() } returns Schedulers.trampoline()

        val alarm1 = repoMock.addAlarm(
            Alarm(
                5,
                "",
                false,
                true,
                false,
                15,
                0L,
                15L,
                arrayOf(true, true, false, false, false, false, false)
            )
        )

        alarm1.concatWith(repoMock.switchAlarm(5))
            .test()
            .assertResult()
            .dispose()
    }

    @Test
    fun switchWhenThereIsNoAlarmInDbTest() {

        val repoMock = spyk(alarmRepositoryImpl)

//        every { repoMock.getSchedulerIO() } returns Schedulers.trampoline()

        val alarm1 = repoMock.addAlarm(
            Alarm(
                5,
                "",
                false,
                true,
                false,
                15,
                0L,
                15L,
                arrayOf(true, true, false, false, false, false, false)
            )
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
        val alarm = Alarm(
            5,
            "",
            false,
            true,
            false,
            15,
            0L,
            15L,
            arrayOf(true, true, false, false, false, false, false)
        )
        val alarm1 = repoMock.addAlarm(alarm)

        alarm1.concatWith(repoMock.updateAlarm(alarm))
            .test()
            .assertResult()
            .dispose()
    }

    @Test
    fun updateWhenDbTableIsEmptyTest() {

        val repoMock = spyk(alarmRepositoryImpl)

//        every { repoMock.getSchedulerIO() } returns Schedulers.trampoline()

        val alarm1 = Alarm(
            5,
            "",
            false,
            true,
            false,
            15,
            0L,
            15L,
            arrayOf(true, true, false, false, false, false, false)
        )

        repoMock.updateAlarm(alarm1)
            .test()
            .assertError(AlarmException::class.java)
            .dispose()
    }

    @Test
    fun updateWhenThereIsNotAlarmInDbTest() {

        val repoMock = spyk(alarmRepositoryImpl)

//        every { repoMock.getSchedulerIO() } returns Schedulers.trampoline()

        val alarm = Alarm(
            5,
            "",
            false,
            true,
            false,
            15,
            0L,
            15L,
            arrayOf(true, true, false, false, false, false, false)
        )
        val alarmNotInDb = Alarm(
            7,
            "",
            false,
            true,
            false,
            15,
            0L,
            15L,
            arrayOf(true, true, false, false, false, false, false)
        )
        val alarm1 = repoMock.addAlarm(alarm)

        alarm1.concatWith(repoMock.updateAlarm(alarmNotInDb))
            .test()
            .assertError(AlarmException::class.java)
            .dispose()
    }

    @Test
    fun moreAddedAlarmsShouldIncrementAlarmId() {

        val repoMock = spyk(alarmRepositoryImpl)

        val alarm1 = Alarm(
            5,
            "",
            false,
            true,
            false,
            15,
            0L,
            15L,
            arrayOf(true, true, false, false, false, false, false)
        )
        val alarm2 = Alarm(
            7,
            "",
            false,
            true,
            false,
            15,
            0L,
            15L,
            arrayOf(true, true, false, false, false, false, false)
        )

//        every { repoMock.getSchedulerIO() } returns Schedulers.trampoline()

        repoMock.addAlarm(alarm1).concatWith(repoMock.addAlarm(alarm2)).blockingGet()

        repoMock.getAlarms().map { alarmList -> alarmList.count() }
            .test()
            .assertResult(2)
            .dispose()

    }

}