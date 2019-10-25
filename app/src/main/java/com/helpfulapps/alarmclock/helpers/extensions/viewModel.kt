package com.helpfulapps.alarmclock.helpers.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T : Any> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, block: (T) -> Unit) =
    observe(lifecycleOwner, Observer(block))