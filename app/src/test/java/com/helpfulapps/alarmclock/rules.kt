package com.helpfulapps.alarmclock

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class RxOverrideScheduleRule : TestRule {
    private val immediate = object : Scheduler() {
        override fun scheduleDirect(@NonNull run: Runnable, delay: Long, @NonNull unit: TimeUnit): Disposable {
            return super.scheduleDirect(run, 0, unit)
        }

        override fun createWorker(): Worker {
            return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, false)
        }
    }

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setInitIoSchedulerHandler { immediate }
                RxJavaPlugins.setInitComputationSchedulerHandler { immediate }
                RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
                RxJavaPlugins.setInitSingleSchedulerHandler { immediate }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }
                RxAndroidPlugins.setMainThreadSchedulerHandler { immediate }

                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}

class RxSchedulerExtensionForJunit5 : AfterEachCallback, BeforeEachCallback {

    private val mSchedulerInstance = Schedulers.trampoline()
    private val schedulerFunc: (Scheduler) -> Scheduler = { mSchedulerInstance }
    private val schedulerLazyFunc: (Callable<Scheduler>) -> Scheduler = { mSchedulerInstance }

    @Throws(Exception::class)
    override fun beforeEach(context: ExtensionContext) {
        RxAndroidPlugins.reset()
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerLazyFunc)

        RxJavaPlugins.reset()
        RxJavaPlugins.setIoSchedulerHandler(schedulerFunc)
        RxJavaPlugins.setNewThreadSchedulerHandler(schedulerFunc)
        RxJavaPlugins.setComputationSchedulerHandler(schedulerFunc)
    }

    @Throws(Exception::class)
    override fun afterEach(context: ExtensionContext) {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }
}