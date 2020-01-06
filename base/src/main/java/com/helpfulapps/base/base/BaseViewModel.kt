package com.helpfulapps.base.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.helpfulapps.base.helpers.Failure
import com.helpfulapps.base.helpers.Message
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {
    val TAG = this.javaClass.simpleName

    protected val _error: LiveEvent<Failure> = LiveEvent()
    val error: LiveData<Failure>
        get() = _error

    protected val _message: LiveEvent<Message> = LiveEvent()
    val message: LiveData<Message>
        get() = _message

    protected val disposables = CompositeDisposable()

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}