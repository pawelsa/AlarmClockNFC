package com.helpfulapps.alarmclock.helpers

import com.helpfulapps.base.helpers.Message

object AlarmRemoved : Message.ExtMessage()
data class AlarmTurnedOn(val timeToAlarm: Long) : Message.ExtMessage()