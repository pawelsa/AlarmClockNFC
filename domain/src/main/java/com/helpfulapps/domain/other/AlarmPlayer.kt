package com.helpfulapps.domain.other

interface AlarmPlayer {
    fun startPlaying(ringtoneUri: String)
    fun startPlayingAlarm()
    fun stopPlaying()
    fun destroyPlayer()
}

