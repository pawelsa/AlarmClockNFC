package com.helpfulapps.base.base

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent

abstract class BaseService : Service(), KoinComponent {

    protected val disposables: CompositeDisposable = CompositeDisposable()

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}