package com.helpfulapps.data.db.alarm.model

import com.helpfulapps.data.mockData.MockData
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test


class AlarmMappingTest {



    @Test
    fun `from domain to data mapping test`() {

        val domainAlarm = MockData.createDomainAlarm()
        val dataAlarm = AlarmEntity(domainAlarm)

        val expected = MockData.createEntityAlarm()

        assertEquals(expected, dataAlarm)
    }

    @Test
    fun `from data to domain mapping test`() {

        val entityAlarm = MockData.createEntityAlarm()
        val domainAlarm = entityAlarm.toDomain()

        val expected = MockData.createDomainAlarm()

        assertEquals(expected, domainAlarm)
    }

    @Test
    fun `from domain to data days of week mapping test`() {

        val domainDays = Array(7) { false }
        domainDays[6] = true
        val dataDaysOfWeek = DaysOfWeekEntity(domainDays)

        val expected = DaysOfWeekEntity(sunday = true)
        assertEquals(expected, dataDaysOfWeek)
    }

    @Test
    fun `from data to domain days of week mapping test`() {

        val dataDaysOfWeekEntry = DaysOfWeekEntity()
        val domainDaysOfWeek = dataDaysOfWeekEntry.toDomain()

        val expected = Array(7) { false }

        assertArrayEquals(expected, domainDaysOfWeek)
    }

}