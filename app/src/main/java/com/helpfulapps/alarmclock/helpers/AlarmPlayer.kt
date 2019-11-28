package com.helpfulapps.alarmclock.helpers

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri

interface AlarmPlayer {
    fun startPlaying(ringtoneUri: Uri)
    fun stopPlaying()
    fun destroyPlayer()
}

class AlarmPlayerImpl(private val context: Context) : AlarmPlayer {

    private var mediaPlayer: MediaPlayer = MediaPlayer()

    override fun startPlaying(ringtoneUri: Uri) {
        stopPlaying()
        mediaPlayer = MediaPlayer()
        with(mediaPlayer) {
            setDataSource(context, ringtoneUri)

            setAudioAttributes(
                builtAudioAttributes()
            )
            isLooping = true
            setOnPreparedListener {
                it.start()
            }
            prepareAsync()
        }
    }

    private fun builtAudioAttributes(): AudioAttributes? {
        return AudioAttributes.Builder()
            .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
            .setLegacyStreamType(AudioManager.STREAM_ALARM)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
    }

    override fun stopPlaying() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    override fun destroyPlayer() {
        mediaPlayer.release()
    }
}