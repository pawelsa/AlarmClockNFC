package com.helpfulapps.data.db.alarm

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.raizlabs.android.dbflow.config.FlowManager
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.helpfulapps.domain.model.Alarm
import io.reactivex.Completable
import org.junit.Test
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
public class AlarmRepositoryImplTest {

    lateinit var alarmRepositoryImpl: AlarmRepositoryImpl

    @Before
    public fun setUp() {
        val application = ApplicationProvider.getApplicationContext<Application>()
        FlowManager.init(application.applicationContext)
        alarmRepositoryImpl = AlarmRepositoryImpl(application.applicationContext)
    }

    @After
    public fun destroy() {
        FlowManager.destroy()
    }

    @Test
    public fun getNoAlarmsTest(){

        val alarmList = alarmRepositoryImpl.getAlarms().blockingGet()

        assertEquals(0, alarmList.size)
    }

    @Test
    public fun getAlarmsTest(){

        val test1 = alarmRepositoryImpl.addAlarm(Alarm(0,"",false,true,false,15,0L,15L, IntArray(0)))
        val test2 = alarmRepositoryImpl.addAlarm(Alarm(0,"co tam slychac",true,true,false,15,0L,15L, IntArray(0, {3})))
        val test3 = alarmRepositoryImpl.addAlarm(Alarm(0,"test",false,true,false,15,0L,15L, IntArray(0)))
        val arrayOfElements = arrayListOf(test1, test2, test3)
        Completable.merge(arrayOfElements).blockingGet()

        val alarmList = alarmRepositoryImpl.getAlarms().blockingGet()

        assertEquals(3, alarmList.size)
    }

    @Test
    public fun removeNoAlarm(){

        val result = alarmRepositoryImpl.removeAlarm(0L).test()
        
    }

}