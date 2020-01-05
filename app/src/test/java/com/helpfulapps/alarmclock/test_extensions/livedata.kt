package com.helpfulapps.alarmclock.test_extensions

import com.helpfulapps.alarmclock.helpers.extensions.contentEqualInOrder
import com.jraska.livedata.TestObserver


fun <O, T : List<O>> TestObserver<T>.contentEqualInOrder(
    comparisonList: List<O>,
    comparator: (O, O) -> Boolean = { first, second -> first == second }
): TestObserver<Boolean> {
    return this.map {
        return@map it.contentEqualInOrder(comparisonList, comparator)
    }
}