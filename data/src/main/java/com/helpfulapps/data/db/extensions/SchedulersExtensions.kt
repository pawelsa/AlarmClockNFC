package com.helpfulapps.data.db.extensions

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

fun getSchedulerIO() : Scheduler = Schedulers.io()