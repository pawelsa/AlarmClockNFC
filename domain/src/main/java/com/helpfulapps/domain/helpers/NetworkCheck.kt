package com.helpfulapps.domain.helpers

import io.reactivex.Maybe


interface NetworkCheck {
    val isConnectedToNetwork: Maybe<Boolean>
}