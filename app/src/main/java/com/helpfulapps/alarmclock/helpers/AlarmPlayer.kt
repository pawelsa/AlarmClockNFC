package com.helpfulapps.alarmclock.helpers

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import com.helpfulapps.alarmclock.R

interface AlarmPlayer {
    fun startPlaying(ringtoneUri: Uri)
    fun startPlayingAlarm()
    fun stopPlaying()
    fun destroyPlayer()
}

class AlarmPlayerImpl(private val context: Context) : AlarmPlayer {

    private var mediaPlayer: MediaPlayer = MediaPlayer()

    override fun startPlaying(ringtoneUri: Uri) {
        startPlaying(true) {
            MediaPlayer().apply {
                setDataSource(context, ringtoneUri)
            }
        }
    }

    override fun startPlayingAlarm() {
        startPlaying(false) {
            MediaPlayer.create(context, R.raw.alarm_sound)
        }
    }

    private fun startPlaying(needToPrepare: Boolean, createMediaPlayer: () -> MediaPlayer) {
        stopPlaying()
        mediaPlayer = createMediaPlayer()
        with(mediaPlayer) {
            setAudioAttributes(
                builtAudioAttributes()
            )
            isLooping = true
            if (needToPrepare) {
                setOnPreparedListener {
                    it.start()
                }
                prepareAsync()
            } else {
                start()
            }
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
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }

    override fun destroyPlayer() {
        mediaPlayer.release()
    }
}