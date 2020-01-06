package com.helpfulapps.device.alarms.other

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.helpfulapps.device.alarms.helpers.fromBuildVersion
import com.helpfulapps.domain.other.VibrationController


class VibrationControllerImpl(context: Context) :
    VibrationController {

    private val vibrator: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    private val scheme = LongArray(2) { 500L }

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