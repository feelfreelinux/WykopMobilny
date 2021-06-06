package io.github.wykopmobilny

import io.github.wykopmobilny.base.Schedulers
import io.reactivex.Scheduler

class TestSchedulers : Schedulers {
    override fun backgroundThread(): Scheduler {
        return io.reactivex.schedulers.Schedulers.trampoline()
    }

    override fun mainThread(): Scheduler {
        return io.reactivex.schedulers.Schedulers.trampoline()
    }
}
