package com.helpfulapps.data.db.extensions

import com.helpfulapps.data.db.alarm.exceptions.AlarmException
import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.observers.TestObserver
import org.junit.Test

class ExtensionsKtTest {

    @Test
    public fun doesBooleanCompleteExtensionOnCompleteReturnCorrectAnswer() {
        val falseBoolean = true

        val result: CompletableSource = falseBoolean.completed("It's ok")

        val observer = TestObserver<Completable>()

        result
            .subscribe(observer)

        observer.assertComplete()
            .dispose()
    }

    @Test
    public fun doesBooleanCompleteExtensionOnErrorReturnCorrectAnswer() {
        val falseBoolean = false

        val result: CompletableSource = falseBoolean.completed("It's ok")

        val observer = TestObserver<Completable>()

        result
            .subscribe(observer)

        observer.assertError(AlarmException::class.java)
            .dispose()
    }

}