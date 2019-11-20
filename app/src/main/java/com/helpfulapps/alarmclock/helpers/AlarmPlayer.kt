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

    private lateinit var mMediaPlayer: MediaPlayer

    override fun startPlaying(ringtoneUri: Uri) {
        mMediaPlayer = MediaPlayer()
        with(mMediaPlayer) {
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
        mMediaPlayer.stop()
        mMediaPlayer.release()
    }

    override fun destroyPlayer() {
        mMediaPlayer.release()
    }
}