package com.helpfulapps.data.db.alarm

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.helpfulapps.data.db.alarm.exceptions.AlarmException
import com.helpfulapps.data.db.alarm.model.AlarmEntry
import com.helpfulapps.domain.models.alarm.Alarm
import com.nhaarman.mockitokotlin2.whenever
import com.raizlabs.android.dbflow.config.FlowManager
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import java.util.*


@RunWith(AndroidJUnit4::class)
class AlarmRepositoryImplTest {

    lateinit var alarmRepositoryImpl: AlarmRepositoryImpl
    lateinit var context: Context

    @Before
    fun setUp() {

        val application = ApplicationProvider.getApplicationContext<Application>()
        context = application.applicationContext
        //FlowManager.init(application.applicationContext)
        alarmRepositoryImpl = AlarmRepositoryImpl(application.applicationContext)
    }

    @After
    fun destroy() {
        FlowManager.destroy()
    }

    //todo here is also sth to repair - details inside
    @Test
    fun getNoAlarmsTest() {

        val repoMock = Mockito.spy(alarmRepositoryImpl)

        //todo simulate better getAlarmsQuery to obtain correct test
        whenever(repoMock.getAlarmsQuery()).thenReturn(Single.just(emptyList()))
        whenever(repoMock.getSchedulerIO()).thenReturn(Schedulers.trampoline())

        repoMock.getAlarms()
            .test()
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

        val repoMock = Mockito.spy(alarmRepositoryImpl)

        whenever(repoMock.getAlarmsQuery()).thenReturn(Single.just(listOf(alarmEntry)))
        whenever(repoMock.getSchedulerIO()).thenReturn(Schedulers.trampoline())

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

        val repoMock = Mockito.spy(alarmRepositoryImpl)

        whenever(repoMock.getAlarmsQuery()).thenReturn(
            Single.just(
                listOf(
                    alarmEntry1,
                    alarmEntry2,
                    alarmEntry3
                )
            )
        )
        whenever(repoMock.getSchedulerIO()).thenReturn(Schedulers.trampoline())

        repoMock.getAlarms()
            .test()
            .assertResult(listOf(alarm1, alarm2, alarm3))
            .dispose()
    }

    @Test
    fun addAlarmTest() {

        val repoMock = Mockito.spy(alarmRepositoryImpl)

        whenever(repoMock.getSchedulerIO()).thenReturn(Schedulers.trampoline())

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

        val repoMock = Mockito.spy(alarmRepositoryImpl)

        whenever(repoMock.getSchedulerIO()).thenReturn(Schedulers.trampoline())

        repoMock.removeAlarm(5)
            .test()
            .assertError(NoSuchElementException::class.java)
            .dispose()
    }

    @Test
    fun removeAlarmTest() {

        val repoMock = Mockito.spy(alarmRepositoryImpl)

        whenever(repoMock.getSchedulerIO()).thenReturn(Schedulers.trampoline())

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

        val repoMock = Mockito.spy(alarmRepositoryImpl)

        whenever(repoMock.getSchedulerIO()).thenReturn(Schedulers.trampoline())

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
//todo test when there is update made if there is not a new object added to db

    @Test
    fun switchWhenThereIsAlarmInDbTest() {

        val repoMock = Mockito.spy(alarmRepositoryImpl)

        whenever(repoMock.getSchedulerIO()).thenReturn(Schedulers.trampoline())

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

        val repoMock = Mockito.spy(alarmRepositoryImpl)

        whenever(repoMock.getSchedulerIO()).thenReturn(Schedulers.trampoline())

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

        val repoMock = Mockito.spy(alarmRepositoryImpl)

        whenever(repoMock.getSchedulerIO()).thenReturn(Schedulers.trampoline())

        repoMock.switchAlarm(2)
            .test()
            .assertError(NoSuchElementException::class.java)
            .dispose()
    }

    @Test
    fun updateWhenThereIsAlarmInDbTest() {

        val repoMock = Mockito.spy(alarmRepositoryImpl)

        whenever(repoMock.getSchedulerIO()).thenReturn(Schedulers.trampoline())
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

        val repoMock = Mockito.spy(alarmRepositoryImpl)

        whenever(repoMock.getSchedulerIO()).thenReturn(Schedulers.trampoline())

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

        val repoMock = Mockito.spy(alarmRepositoryImpl)

        whenever(repoMock.getSchedulerIO()).thenReturn(Schedulers.trampoline())

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


}