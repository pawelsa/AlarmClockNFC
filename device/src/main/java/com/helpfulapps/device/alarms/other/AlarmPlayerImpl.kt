package com.helpfulapps.device.alarms.other

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import com.helpfulapps.device.R
import com.helpfulapps.domain.other.AlarmPlayer

class AlarmPlayerImpl(private val context: Context) :
    AlarmPlayer {

    private var mediaPlayer: MediaPlayer =
        MediaPlayer()

    override fun startPlaying(ringtoneUri: String) {
        startPlaying(true) {
            MediaPlayer().apply {
                setDataSource(context, Uri.parse(ringtoneUri))
            }
        }
    }

    override fun startPlayingAlarm() {
        startPlaying(false) {
            MediaPlayer.create(
                context,
                R.raw.alarm_sound
            )
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
        try {
            mediaPlayer.stop()
            mediaPlayer.release()
        } catch (e: Exception) {
            mediaPlayer.release()
        }
    }

    override fun destroyPlayer() {
        mediaPlayer.release()
    }
}