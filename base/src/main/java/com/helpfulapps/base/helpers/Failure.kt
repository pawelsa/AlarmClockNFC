package com.helpfulapps.base.helpers

import android.content.res.Resources

sealed class Failure {
    object UnknownError : Failure()
    object NotFoundError : Failure()

    abstract class FeatureError : Failure()
}

fun Throwable.toFailure(): Failure {
    return when (this) {
        is Resources.NotFoundException -> Failure.NotFoundError
        else -> Failure.UnknownError
    }
}