package io.github.wykopmobilny.util.rx

import io.github.wykopmobilny.base.Schedulers
import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler

class TestSchedulerProvider : Schedulers {

    val mTestScheduler: TestScheduler = TestScheduler()
    override fun mainThread(): Scheduler {
        return mTestScheduler
    }

    override fun backgroundThread(): Scheduler {
        return mTestScheduler
    }
}
