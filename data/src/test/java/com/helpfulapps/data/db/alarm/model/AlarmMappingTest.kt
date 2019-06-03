package com.helpfulapps.data.db.alarm.model

import com.helpfulapps.domain.model.Alarm
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito
import com.nhaarman.mockitokotlin2.whenever


class AlarmMappingTest {

    val baseDomainDaysOfWeek = arrayOf(true, true, false, false, false, false, false)
    val baseDataDaysOfWeek = DaysOfWeekEntry(baseDomainDaysOfWeek)
    val baseDataAlarm = AlarmEntry(0, "", false, true, false, 15, 0L, 15L, baseDomainDaysOfWeek)
    val baseDomainAlarm = Alarm(0, "", false, true, false, 15, 0L, 15L, baseDomainDaysOfWeek)


    @Test
    public fun fromDomainToDataMappingTest(){

        val dataAlarm = AlarmEntry(baseDomainAlarm)
        dataAlarm.daysOfWeek = baseDataDaysOfWeek

        baseDataAlarm.daysOfWeek = baseDataDaysOfWeek

        assertEquals(baseDataAlarm, dataAlarm)
    }

    @Test
    public fun fromDataToDomainMappingTest(){
        val domainAlarm = baseDataAlarm.toDomain()
        val mockedTestedDomainAlarm = Mockito.spy(domainAlarm)
        whenever(mockedTestedDomainAlarm.repetitionDays).thenReturn(baseDomainDaysOfWeek)

        assertEquals(baseDomainAlarm, mockedTestedDomainAlarm)
    }

    @Test
    public fun fromDomainToDataDaysOfWeekMappingTest(){
        val dataDaysOfWeek = DaysOfWeekEntry(baseDomainDaysOfWeek)

        assertEquals(baseDataDaysOfWeek, dataDaysOfWeek)
    }

    @Test
    public fun fromDataToDomainDaysOfWeekMappingTest(){
        val domainDaysOfWeek = baseDataDaysOfWeek.toDomain()

        assertArrayEquals(baseDomainDaysOfWeek, domainDaysOfWeek)
    }

}