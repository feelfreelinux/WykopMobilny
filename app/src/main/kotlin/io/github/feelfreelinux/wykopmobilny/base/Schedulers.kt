package io.github.feelfreelinux.wykopmobilny.base

import io.reactivex.Scheduler

interface Schedulers {
    fun mainThread() : Scheduler
    fun backgroundThread() : Scheduler
}