package com.helpfulapps.data.db.alarm.model

import com.helpfulapps.domain.models.alarm.Alarm
import io.mockk.every
import io.mockk.spyk
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test


class AlarmMappingTest {

    val baseDomainDaysOfWeek = arrayOf(true, true, false, false, false, false, false)
    val baseDataDaysOfWeek = DaysOfWeekEntry(baseDomainDaysOfWeek)
    val baseDataAlarm =
        AlarmEntity(0, "", false, true, false, "ringtoneUrl", 0, 15, baseDomainDaysOfWeek)
    val baseDomainAlarm = Alarm(
        0,
        "",
        false,
        true,
        false,
        "ringtoneUrl",
        0,
        15,
        baseDomainDaysOfWeek
    )


    @Test
    fun fromDomainToDataMappingTest() {

        val dataAlarm = AlarmEntity(baseDomainAlarm)
        dataAlarm.daysOfWeek = baseDataDaysOfWeek

        baseDataAlarm.daysOfWeek = baseDataDaysOfWeek

        assertEquals(baseDataAlarm, dataAlarm)
    }

    @Test
    fun fromDataToDomainMappingTest() {
        val domainAlarm = baseDataAlarm.toDomain()
        val mockedTestedDomainAlarm = spyk(domainAlarm)
        every { mockedTestedDomainAlarm.repetitionDays } returns baseDomainDaysOfWeek

        assertEquals(baseDomainAlarm, mockedTestedDomainAlarm)
    }

    @Test
    fun fromDomainToDataDaysOfWeekMappingTest() {
        val dataDaysOfWeek = DaysOfWeekEntry(baseDomainDaysOfWeek)

        assertEquals(baseDataDaysOfWeek, dataDaysOfWeek)
    }

    @Test
    fun fromDataToDomainDaysOfWeekMappingTest() {
        val domainDaysOfWeek = baseDataDaysOfWeek.toDomain()

        assertArrayEquals(baseDomainDaysOfWeek, domainDaysOfWeek)
    }

}