package io.github.feelfreelinux.wykopmobilny.base

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

class WykopSchedulers : Schedulers {
    override fun backgroundThread(): Scheduler {
        return io.reactivex.schedulers.Schedulers.io()
    }

    override fun mainThread(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}