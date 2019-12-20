package com.helpfulapps.domain.use_cases.extensions

import com.helpfulapps.domain.extensions.dayOfWeek
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetDayOfWeekTest {

    @Test
    fun `should return saturday`() {

        val timestamp = 1561161600000L
        val dayOfWeek = timestamp.dayOfWeek

        assertEquals(dayOfWeek, 7)
    }

    @Test
    fun `should return sunday`() {

        val timestamp = 1561248000000L
        val dayOfWeek = timestamp.dayOfWeek

        assertEquals(dayOfWeek, 1)
    }

    @Test
    fun `should return monday`() {

        val timestamp = 1561334400000L
        val dayOfWeek = timestamp.dayOfWeek

        assertEquals(dayOfWeek, 2)
    }


}