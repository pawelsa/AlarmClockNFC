package com.helpfulapps.data.db.alarm

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import com.raizlabs.android.dbflow.config.FlowManager
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.helpfulapps.data.db.alarm.model.AlarmEntry
import com.helpfulapps.domain.model.Alarm
import com.helpfulapps.domain.repository.AlarmRepository
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test
import org.junit.Assert.*
import io.reactivex.observers.TestObserver
import org.mockito.Mockito
import io.reactivex.schedulers.TestScheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.Scheduler
import io.reactivex.exceptions.CompositeException
import io.reactivex.plugins.RxJavaPlugins
import java.util.concurrent.Callable


@RunWith(AndroidJUnit4::class)
public class AlarmRepositoryImplTest {

    lateinit var alarmRepositoryImpl: AlarmRepositoryImpl
    lateinit var context: Context

    @Before
    public fun setUp() {

        val application = ApplicationProvider.getApplicationContext<Application>()
        context = application.applicationContext
        FlowManager.init(application.applicationContext)
        alarmRepositoryImpl = AlarmRepositoryImpl(application.applicationContext)
    }

    @After
    public fun destroy() {
        FlowManager.destroy()
    }

    @Test
    public fun getNoAlarmsTest(){

        val repoMock = Mockito.spy(alarmRepositoryImpl)

        whenever(repoMock.getAlarms()).thenReturn(Single.never())

        repoMock.getAlarms()
            .test()
            .assertValueCount(0)
            .dispose()
    }

    @Test
    public fun getAlarmsTest(){

        val alarm1 = Alarm(0,"",false,true,false,15,0L,15L, IntArray(0))

        val repositoryMock = mock<AlarmRepository>()

        whenever(repositoryMock.getAlarms()).thenReturn(Single.just(listOf(alarm1)))

        repositoryMock.getAlarms()
            .test()
            .assertValueCount(1)
            .dispose()
    }

    @Test
    public fun getAlarmCountTest(){

        val alarm1 = Alarm(0,"",false,true,false,15,0L,15L, IntArray(0))
        val alarm2 = Alarm(0,"co tam slychac",true,true,false,15,0L,15L, IntArray(0, {3}))
        val alarm3 = Alarm(0,"test",false,true,false,15,0L,15L, IntArray(0))

        val repositoryMock = mock<AlarmRepository>{
            on { getAlarms() } doReturn Single.just(listOf(alarm1, alarm2, alarm3))
        }

        repositoryMock.getAlarms()
            .flatMap { list -> Single.just(list.size) }
            .test()
            .assertValue(3)
            .dispose()
    }

    @Test
    public fun addAlarmsIdsTest(){

        val alarm1 = alarmRepositoryImpl.addAlarm(Alarm(0,"",false,true,false,15,0L,15L, IntArray(0)))
        val alarm2 = alarmRepositoryImpl.addAlarm(Alarm(0,"co tam slychac",true,true,false,15,0L,15L, IntArray(0, {3})))
        val alarm3 = alarmRepositoryImpl.addAlarm(Alarm(0,"test",false,true,false,15,0L,15L, IntArray(0)))

        val alarmList = listOf(alarm1,alarm2, alarm3)
        Completable.merge(alarmList).blockingGet()

        alarmRepositoryImpl.getAlarms()
            .flatMapObservable { list -> Observable.fromIterable(list) }
            .map { alarm -> alarm.id }
            .test()
            .assertValues(0,0,0)
            .dispose()

    }

    @Test
    public fun addAlarmTest(){

        val repoMock = Mockito.spy(alarmRepositoryImpl)

        whenever(repoMock.getSchedulerIO()).thenReturn(Schedulers.trampoline())

        val alarm1 = repoMock.addAlarm(Alarm(0,"",false,true,false,15,0L,15L, IntArray(0)))
        val alarm2 = repoMock.addAlarm(Alarm(0,"co tam slychac",true,true,false,15,0L,15L, IntArray(0, {3})))
        val alarm3 = repoMock.addAlarm(Alarm(0,"test",false,true,false,15,0L,15L, IntArray(0)))

        val alarmList = listOf(alarm1,alarm2, alarm3)

        Completable.merge(alarmList)
            .test()
            .assertResult()
            .dispose()
    }

    @Test
    public fun removeNoAlarm(){

        val result = alarmRepositoryImpl.removeAlarm(0L).test()
        
    }

}