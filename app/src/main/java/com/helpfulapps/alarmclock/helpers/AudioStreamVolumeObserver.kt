package com.helpfulapps.alarmclock.helpers

import android.content.Context
import android.database.ContentObserver
import android.media.AudioManager
import android.os.Handler
import android.provider.Settings

typealias OnAudioStreamVolumeChangedListener = (Boolean) -> Unit

class AudioStreamVolumeObserver(
    private val mContext: Context
) {

    private lateinit var audioStreamVolumeContentObserver: AudioStreamVolumeContentObserver


    private class AudioStreamVolumeContentObserver(
        handler: Handler,
        private val audioManager: AudioManager,
        private val mListener: OnAudioStreamVolumeChangedListener
    ) : ContentObserver(handler) {

        private var lastVolume: Int = audioManager.getStreamVolume(AudioManager.STREAM_ALARM)

        override fun onChange(selfChange: Boolean) {
            val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM)

            if (currentVolume != lastVolume) {
                lastVolume = currentVolume

                mListener(currentVolume == 1)
            }
        }
    }

    fun startObserving(
        listener: OnAudioStreamVolumeChangedListener
    ) {
        stopObserving()

        initAudioStreamVolumeObserver(listener)

        mContext.contentResolver
            .registerContentObserver(
                Settings.System.CONTENT_URI,
                true,
                audioStreamVolumeContentObserver
            )
    }

    private fun initAudioStreamVolumeObserver(listener: OnAudioStreamVolumeChangedListener) {
        val handler = Handler()
        val audioManager = mContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        audioStreamVolumeContentObserver =
            AudioStreamVolumeContentObserver(handler, audioManager, listener)
    }

    fun stopObserving() {
        if (::audioStreamVolumeContentObserver.isInitialized) {
            mContext.contentResolver
                .unregisterContentObserver(audioStreamVolumeContentObserver)
        }
    }
}