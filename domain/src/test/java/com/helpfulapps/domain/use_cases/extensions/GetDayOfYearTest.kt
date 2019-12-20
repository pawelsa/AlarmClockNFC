package com.helpfulapps.domain.use_cases.extensions

import com.helpfulapps.domain.extensions.dayOfYear
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetDayOfYearTest {

    @Test
    fun `should return 20 06 2019`() {

        val dt: Long = 1560988800000

        assertEquals(dt.dayOfYear, 171)
    }

    @Test
    fun `should return 22 05 2019`() {

        val dt: Long = 1558483200000
        assertEquals(dt.dayOfYear, 142)
    }

    @Test
    fun `should return 10 05 2018`() {

        val dt: Long = 1525910400000
        assertEquals(dt.dayOfYear, 130)
    }

}