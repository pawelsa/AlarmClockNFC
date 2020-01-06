package com.helpfulapps.base.helpers

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun <T : Any> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, block: (T) -> Unit) =
    observe(lifecycleOwner, Observer(block))

class LiveDataDelegate<T> {

    private val _data: MutableLiveData<T> = MutableLiveData()
    private val data: LiveData<T>
        get() = _data

    fun observe(owner: LifecycleOwner, observer: (T) -> Unit) =
        data.observe(owner, Observer { observer(it!!) })

    var value: T?
        set(value) {
            _data.value = value
        }
        get() = data.value
}
