package com.helpfulapps.data.db.extensions

import com.helpfulapps.data.extensions.checkCompleted
import com.helpfulapps.domain.exceptions.AlarmException
import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.observers.TestObserver
import org.junit.jupiter.api.Test

class ExtensionsKtTest {

    @Test
    fun doesBooleanCompleteExtensionOnCompleteReturnCorrectAnswer() {
        val falseBoolean = true

        val result: CompletableSource = falseBoolean.checkCompleted(Throwable("It's ok"))

        val observer = TestObserver<Completable>()

        result
            .subscribe(observer)

        observer.assertComplete()
            .dispose()
    }

    @Test
    fun doesBooleanCompleteExtensionOnErrorReturnCorrectAnswer() {
        val falseBoolean = false

        val result: CompletableSource = falseBoolean.checkCompleted(
            AlarmException(
                "It's ok"
            )
        )

        val observer = TestObserver<Completable>()

        result
            .subscribe(observer)

        observer.assertError(AlarmException::class.java)
            .dispose()
    }

}