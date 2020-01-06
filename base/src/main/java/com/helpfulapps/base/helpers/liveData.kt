package com.helpfulapps.base.helpers

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T : Any> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, block: (T) -> Unit) =
    observe(lifecycleOwner, Observer(block))