package com.helpfulapps.domain.extensions

inline fun whenFalse(expression: Boolean?, action: () -> Unit) {
    if (expression != true) {
        action()
    }
}