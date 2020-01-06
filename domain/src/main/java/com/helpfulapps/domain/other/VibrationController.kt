package com.helpfulapps.domain.other

interface VibrationController {
    fun startVibrating(shouldPlay: Boolean)
    fun stopVibrating()
}