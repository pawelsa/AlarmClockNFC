package com.helpfulapps.alarmclock.helpers

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator


interface VibrationController {
    fun startVibrating(shouldPlay: Boolean)
    fun stopVibrating()
}

class VibrationControllerImpl(context: Context) : VibrationController {

    val vibrator: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    val scheme = LongArray(2) { 500L }

    override fun startVibrating(shouldPlay: Boolean) {
        if (shouldPlay) {
            fromBuildVersion(Build.VERSION_CODES.O,
                matching = {
                    vibrator.vibrate(VibrationEffect.createWaveform(scheme, 0))
                }, otherwise = {
                    vibrator.vibrate(scheme, 0)
                }
            )
        }
    }

    override fun stopVibrating() {
        vibrator.cancel()
    }

}