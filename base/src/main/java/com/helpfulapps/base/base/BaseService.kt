package com.helpfulapps.base.base

import android.app.Service
import android.content.Intent
import android.os.IBinder
import org.koin.core.KoinComponent

abstract class BaseService : Service(), KoinComponent {
    override fun onBind(intent: Intent?): IBinder? = null
}