package com.helpfulapps.alarmclock.helpers

import com.helpfulapps.base.helpers.Failure

object AlarmFaildedToAdd : Failure.FeatureError()
object CouldNotObtainAlarms : Failure.FeatureError()
object CouldNotRemoveAlarm : Failure.FeatureError()
data class CouldNotSwitchAlarm(val isTurningOn: Boolean) : Failure.FeatureError()