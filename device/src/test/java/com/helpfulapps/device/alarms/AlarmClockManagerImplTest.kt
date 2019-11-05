package com.helpfulapps.device.alarms

import android.app.AlarmManager
import android.content.Context
import io.mockk.mockk
import org.junit.Test

class AlarmClockManagerImplTest {

    val alarmManager: AlarmManager = mockk {}
    val context: Context = mockk {}
    val manager = AlarmClockManagerImpl(context, alarmManager)

    @Test
    fun `should pick monday`() {

    }

    fun `should pick wednesday`() {

    }

}