package com.helpfulapps.data.db.extensions

import com.helpfulapps.data.db.alarm.exceptions.AlarmException
import io.reactivex.Completable
import io.reactivex.CompletableSource


fun Boolean.completed(throwableMessage: String) = Completable.create { subscriber ->
    when (this) {
        true -> subscriber.onComplete()
        else -> subscriber.onError(AlarmException(throwableMessage))
    }
}
